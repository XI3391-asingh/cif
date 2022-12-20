package com.cif.syncerservice.core.syncer.services;

import com.cif.syncerservice.api.*;
import com.cif.syncerservice.core.syncer.adapter.AppianAdapter;
import com.cif.syncerservice.core.syncer.adapter.IntegrationAdapter;
import com.cif.syncerservice.core.syncer.adapter.TmAdapter;
import com.cif.syncerservice.core.syncer.component.KafkaProducer;
import com.cif.syncerservice.core.syncer.domain.ApiDetails;
import com.cif.syncerservice.core.syncer.domain.*;
import com.cif.syncerservice.core.syncer.dto.ApiResponse;
import com.cif.syncerservice.core.syncer.dto.PartyRequest;
import com.cif.syncerservice.core.syncer.dto.TopicData;
import com.cif.syncerservice.core.syncer.helper.SyncerMapper;
import com.cif.syncerservice.db.SyncerRepository;
import com.cif.syncerservice.resources.exceptions.RecordNotFoundException;
import com.cif.syncerservice.resources.exceptions.RecordPersistException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import liquibase.pro.packaged.T;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.Header;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import java.net.http.HttpClient;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import static com.cif.syncerservice.core.syncer.helper.ResponseHelper.buildErrorResponse;
import static com.cif.syncerservice.core.syncer.helper.ResponseHelper.buildSuccessResponse;
import static java.util.Collections.singletonList;
import static org.slf4j.LoggerFactory.getLogger;

public class SyncerService {
    private final Logger log = getLogger(SyncerService.class);
    public static final String CREATE = "CREATE";
    public static final String UPDATE = "UPDATE";
    public static final String SAVE = "SAVE";
    public static final String TM = "TM";
    private static final String APPIAN = "APPIAN";

    public static final String STATUS_IN_PROGRESS = "In-Progress";
    public static final String STATUS_SUCCESSFUL = "SUCCESSFUL";
    public static final String STATUS_UNSUCCESSFUL = "UNSUCCESSFUL";
    public static final String STATUS_ERROR = "ERROR";
    public static final String STATUS_UPDATE_NOT_AVAILABLE = "Update Not Available";
    private final SyncerRepository syncerRepository;
    private final ObjectMapper objectMapper;
    private final KafkaProducer producer;
    private final HttpClient httpClient;
    private final CircuitBreaker circuitBreaker;
    private final ExecutorService executor;

    public SyncerService(SyncerRepository syncerRepository, ObjectMapper objectMapper, KafkaProducer producer, HttpClient httpClient, CircuitBreaker circuitBreaker, ExecutorService executor) {
        this.syncerRepository = syncerRepository;
        this.objectMapper = objectMapper;
        this.producer = producer;
        this.httpClient = httpClient;
        this.circuitBreaker = circuitBreaker;
        this.executor = executor;
    }

    public void processRecord(ConsumerRecord<String, Object> consumerRecord, List<AdapterConfig> adapterConfigs, List<ChangeConfig> changeConfigs) {
        try {
            String event = consumerRecord.key();
            var topicData = objectMapper.readValue(consumerRecord.value().toString(), TopicData.class);
            var partyRequest = topicData.payload();
            //This will be remove in near future
            partyRequest.getParty().setSourceSystem("Onboarding");
            partyRequest.setEvent(event);
            String kafkaMessageTransactionId = null;
            for (Header header : consumerRecord.headers().headers("transactionId")) {
                kafkaMessageTransactionId = new String(header.value(), StandardCharsets.UTF_8);
                partyRequest.setKafkaTransactionId(kafkaMessageTransactionId);
            }
            var systemAdapter = buildAllActiveAdapter(adapterConfigs, changeConfigs);
            CompletableFuture
                    .runAsync(() -> systemAdapter.entrySet().forEach((obj -> {
                        if (obj.getValue().validateAdapter(adapterConfigs, partyRequest.getParty().getSourceSystem(), obj.getKey().getSystemCode(), event)) {
                            invokeAdapter(partyRequest, obj.getKey(), obj.getValue(), topicData.metaFields());
                        }
                    }
                    )), executor)
                    .exceptionally(exception -> {
                        log.error("Error while invoking adapter. Error message {}", exception.getMessage());
                        return null;
                    });
        } catch (JsonProcessingException e) {
            log.error("Error while building party object from kafka topic value .Error message {}", e.getMessage());
        }
    }

    private void invokeAdapter(PartyRequest partyRequest, ChangeConfig changeConfig, IntegrationAdapter<T> integrationAdapter, Set<String> metaFields) {
        log.info("{} Adapter processing start", changeConfig.getSystemCode());
        SyncerTransaction syncerTransaction = syncerTransactionSaveOrUpdate(changeConfig, partyRequest, SAVE, null, null);
        PartyXref partyXref = partyXRefSaveOrUpdate(changeConfig, partyRequest, SAVE, null, null);
        try {
            partyRequest.getParty().setxRefIf(partyXref.getXrefId());
            var apiResponse = integrationAdapter.adapterProcessRecord(partyRequest, partyRequest.getEvent(), metaFields);
            apiResponse.setSystemCode(changeConfig.getSystemCode());
            apiResponse.setEvent(partyRequest.getEvent());
            apiResponse.setPartyId(partyRequest.getParty().getPartyIdentifier());
            if (apiResponse == null) {
                syncerTransaction.setStatus(STATUS_UPDATE_NOT_AVAILABLE);
                syncerRepository.updateTransactionHistory(syncerTransaction);
            } else {
                if (apiResponse.getIsSuccess()) {
                    syncerTransactionSaveOrUpdate(changeConfig, partyRequest, UPDATE, apiResponse, syncerTransaction);
                    partyXRefSaveOrUpdate(changeConfig, partyRequest, UPDATE, apiResponse, partyXref);
                } else {
                    syncerTransactionSaveOrUpdate(changeConfig, partyRequest, UPDATE, apiResponse, syncerTransaction);
                }
                producer.sendMessageToTopic(syncerTransaction.getEventTransactionId(), apiResponse);
            }
        } catch (Exception e) {
            log.error("Error Message {}", e.getMessage());
        }

        log.info("{} Adapter processing end", changeConfig.getSystemCode());
    }

    HashMap<ChangeConfig, IntegrationAdapter<T>> buildAllActiveAdapter(List<AdapterConfig> adapterConfigs, List<ChangeConfig> changeConfigs) {
        HashMap<ChangeConfig, IntegrationAdapter<T>> systemAdapter = new HashMap<>();
        try {

            for (ChangeConfig changeConfig : changeConfigs) {
                changeConfig.setApiObject(objectMapper.readValue(changeConfig.getApiDetails(), ApiDetails.class));
                if (StringUtils.isBlank(changeConfig.getApiObject().getBaseUrl())
                        || ObjectUtils.anyNull(changeConfig.getApiObject().getCreate())
                        || ObjectUtils.anyNull(changeConfig.getApiObject().getUpdate())) {
                    log.error("Change Config is not present for {}", changeConfig.getSystemCode());
                    break;
                }
                if (changeConfig.getSystemCode().equalsIgnoreCase(TM)) {
                    IntegrationAdapter<T> tmAdapter = new TmAdapter(adapterConfigs, changeConfig, httpClient, objectMapper, circuitBreaker);
                    systemAdapter.put(changeConfig, tmAdapter);
                }
                if (changeConfig.getSystemCode().equalsIgnoreCase(APPIAN)) {
                    IntegrationAdapter<T> appianAdapter = new AppianAdapter(adapterConfigs, changeConfig, httpClient, objectMapper, circuitBreaker);
                    systemAdapter.put(changeConfig, appianAdapter);
                }
                //Add more integration here
            }
        } catch (JsonProcessingException e) {
            log.error("Error while building adapter. {}", e.getMessage());
        }
        return systemAdapter;
    }

    private PartyXref partyXRefSaveOrUpdate(ChangeConfig changeConfig, PartyRequest partyRequest, String type, ApiResponse apiResponse, PartyXref partyXrefUpdate) {
        PartyXref partyXref = new PartyXref();
        try {
            if (SAVE.equalsIgnoreCase(type)) {
                partyXref = syncerRepository.fetchPartyXrefByPartyId(partyRequest.getParty().getPartyIdentifier(), changeConfig.getSystemId());
                if (partyXref == null) {
                    partyXref = buildPartyRefData(partyRequest, changeConfig.getSystemId());
                    int id = syncerRepository.savePartyXrefRecord(partyXref);
                    partyXref.setPartyXrefId(id);
                    log.info("Party record save complete for party id{}", partyRequest.getParty().getPartyIdentifier());
                }
            } else {
                partyXrefUpdate.setXrefId(apiResponse.getStatusCode() == 200 ? apiResponse.getxRefId() : null);
                syncerRepository.updatePartyXrefRecord(partyXrefUpdate);
                log.info("Party X-Ref record update complete for party id{}", partyRequest.getParty().getPartyIdentifier());
            }
        } catch (Exception e) {
            log.error("partyXRefSaveOrUpdate . Error while {}ING party-x-ref for {}. Message {} ", type, changeConfig.getSystemCode(), e.getMessage());
        }
        return partyXref;
    }

    private SyncerTransaction syncerTransactionSaveOrUpdate(ChangeConfig changeConfig, PartyRequest partyRequest, String type, ApiResponse apiResponse, SyncerTransaction syncerTransactionUpdate) {
        SyncerTransaction syncerTransaction = new SyncerTransaction();
        try {
            if (SAVE.equalsIgnoreCase(type)) {
                syncerTransaction = syncerRepository.fetchSyncerTransactionDataByEventTransactionID(partyRequest.getKafkaTransactionId(), changeConfig.getSystemId());
                if (syncerTransaction == null) {
                    syncerTransaction = buildTransactionRecord(partyRequest, changeConfig.getSystemId());
                    int transactionId = syncerRepository.saveTransactionHistory(syncerTransaction);
                    log.info("{} event syncer transaction record save complete for {}", partyRequest.getEvent(), changeConfig.getSystemCode());
                    syncerTransaction.setSyncerTransactionId(transactionId);
                }
            } else {
                syncerTransactionUpdate.setApiRequest(apiResponse.getRequestBody());
                syncerTransactionUpdate.setApiResponse(objectMapper.writeValueAsString(apiResponse.getResponseBody()));
                syncerTransactionUpdate.setApiEndpoint(apiResponse.getRequestUrl());
                if (apiResponse.getStatusCode() == 500 || apiResponse.getStatusCode() == 403) {
                    syncerTransactionUpdate.setStatus(STATUS_ERROR);
                } else {
                    syncerTransactionUpdate.setStatus(apiResponse.getStatusCode() == 200 ? STATUS_SUCCESSFUL : STATUS_UNSUCCESSFUL);
                }
                syncerTransactionUpdate.setUpdatedAt(LocalDateTime.now());
                syncerRepository.updateTransactionHistory(syncerTransactionUpdate);
                log.info("{} event syncer transaction record update complete for {}", partyRequest.getEvent(), changeConfig.getSystemCode());
            }
        } catch (Exception e) {
            log.error("syncerTransactionSaveOrUpdate . Error while {}ING syncer transaction for {}. Message {}", type, changeConfig.getSystemCode(), e.getMessage());
        }
        return syncerTransaction;
    }

    private PartyXref buildPartyRefData(PartyRequest partyRequest, int systemId) {
        PartyXref partyXref = new PartyXref();
        partyXref.setPartyId(partyRequest.getParty().getPartyIdentifier());
        partyXref.setXrefId(partyRequest.getParty().getPartyIdentifier());
        partyXref.setSystemId(systemId);
        return partyXref;
    }

    private SyncerTransaction buildTransactionRecord(PartyRequest partyRequest, int systemId) {
        SyncerTransaction syncerTransaction = new SyncerTransaction();
        syncerTransaction.setPartyId(partyRequest.getParty().getPartyIdentifier());
        syncerTransaction.setEventType(partyRequest.getEvent());
        syncerTransaction.setEventTransactionId(partyRequest.getKafkaTransactionId());
        syncerTransaction.setStatus(STATUS_IN_PROGRESS);
        syncerTransaction.setSystemId(systemId);
        syncerTransaction.setCreatedAt(partyRequest.getParty().getCreatedAt());
        return syncerTransaction;
    }

    public Response createConfigData(CreateConfigRequest configRequest) {
        try {
            int systemId = 0;
            Map<String, Integer> systemDetails = new HashMap<>();
            List<SystemDetail> systemDetailList = syncerRepository.fetchSystemDetailsBySystemCode(singletonList(configRequest.getSystemCode()));
            systemDetails = systemDetailList.stream().filter(systemDetail -> systemDetail != null)
                    .collect(Collectors.toMap(systemDetail -> systemDetail.getSystemCode(), systemDetail -> systemDetail.getSystemId()));
            if (systemDetails.isEmpty()) {
                SystemDetail systemDetail = new SystemDetail(configRequest.getSystemCode(), configRequest.getSystemType(), String.format("%s %s System ", configRequest.getSystemCode(), configRequest.getSystemType()));
                if (systemDetail.getSystemType().equalsIgnoreCase("SOURCE")) {
                    systemId = syncerRepository.saveSystemDetail(systemDetail);
                    if (systemId != 0) {
                        return Response.status(HttpStatus.CREATED_201)
                                .entity(buildSuccessResponse("CONFIG_CREATED", "Config record created successfully!",
                                        configRequest)).build();
                    }
                }
                systemId = syncerRepository.saveSystemDetail(systemDetail);
            } else {
                systemId = systemDetails.get(configRequest.getSystemCode());
            }
            if (ObjectUtils.anyNull(configRequest.getApiDetails())
                    || CollectionUtils.isEmpty(configRequest.getConfigFields())
                    || configRequest.getIsActive() == null) {
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("CONFIG_ERROR", "Mandatory field is not present ")).build();
            }
            configRequest.setSystemId(systemId);
            var changeConfig = SyncerMapper.MAPPER.mapChangeConfig(configRequest);
            changeConfig.setApiDetails(objectMapper.writeValueAsString(configRequest.getApiDetails()));
            var configId = syncerRepository.saveConfigData(changeConfig);
            if (configId != 0) {
                configRequest.setConfigId(configId);
                return Response.status(HttpStatus.CREATED_201)
                        .entity(buildSuccessResponse("CONFIG_CREATED", "Config record created successfully!",
                                configRequest)).build();
            }
        } catch (Exception e) {
            throw new RecordPersistException("Failed to create config record", e);
        }
        return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("CONFIG_ERROR", "Error occurred while creating config record!")).build();
    }

    public Response updateConfigData(UpdateConfigRequest configRequest) {
        log.info("Starting config update process for [configId={}] ", configRequest.getConfigId());
        try {
            var existingConfigRecord = syncerRepository.findChangeConfigByConfigId(configRequest.getConfigId());
            var changeConfigRecordNeedToBeUpdated = SyncerMapper.MAPPER.updateWithNullAsNoChangeConfig(configRequest, existingConfigRecord);
            if (configRequest.getApiDetails() != null) {
                changeConfigRecordNeedToBeUpdated.setApiDetails(objectMapper.writeValueAsString(configRequest.getApiDetails()));
            }
            boolean updateStatus = syncerRepository.updateConfigData(changeConfigRecordNeedToBeUpdated);
            if (!updateStatus) {
                log.info("Update failed for [configId={}] for [systemCode={}] ", configRequest.getConfigId(), configRequest.getSystemCode());
                return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse(STATUS_ERROR, "Update Change Config Failed")).build();
            } else {
                log.info("Update success for [configId={}] for [systemCode={}] ", configRequest.getConfigId(), configRequest.getSystemCode());
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONFIG_UPDATED", "Update Config record Success", configRequest)).build();
            }
        } catch (Exception e) {
            throw new RecordPersistException("Failed to update config record", e);
        }
    }

    public Response fetchConfigDetailsBySystemCode(String systemCode) {
        try {
            if (StringUtils.isNoneBlank(systemCode)) {
                ChangeConfig changeConfig = null;
                SystemDetail systemDetail = syncerRepository.fetchSystemDetailsBySystemCodeAndSystemType(systemCode, "SOURCE");
                if (ObjectUtils.isNotEmpty(systemDetail)) {
                    changeConfig = new ChangeConfig(0, systemDetail.getSystemCode(), systemDetail.getSystemType(), systemDetail.getSystemId());
                } else {
                    changeConfig = syncerRepository.findChangeConfigBySystemCode(systemCode);
                }
                if (changeConfig == null)
                    return Response.status(HttpStatus.NO_CONTENT_204).entity(buildErrorResponse("NO CONTENT", "No Record Exist")).build();

                ApiDetails apiDetails = StringUtils.isNoneBlank(changeConfig.getApiDetails()) ? objectMapper.readValue(changeConfig.getApiDetails(), ApiDetails.class) : null;
                changeConfig.setApiObject(apiDetails);
                ConfigResponseCmd responseCmd = SyncerMapper.MAPPER.mapConfigResponse(changeConfig);

                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("SUCCESS", "Fetch Change Config Success", responseCmd)).build();
            } else {
                List<ChangeConfig> changeConfigs = syncerRepository.fetchChangeConfigDataAll();
                if (!CollectionUtils.isNotEmpty(changeConfigs)) {
                    return Response.status(HttpStatus.NO_CONTENT_204).entity(buildErrorResponse("NO CONTENT", "No Record Exist")).build();
                }
                List<ConfigResponseCmd> configResponseCmds = changeConfigs.stream().map(obj -> {
                    ApiDetails apiDetails = null;
                    try {
                        apiDetails = objectMapper.readValue(obj.getApiDetails(), ApiDetails.class);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    obj.setApiObject(apiDetails);
                    return SyncerMapper.MAPPER.mapConfigResponse(obj);
                }).collect(Collectors.toList());
                List<SystemDetail> systemDetails = syncerRepository.fetchSystemDetails();
                configResponseCmds.addAll(systemDetails.stream().filter(Objects::nonNull).filter(systemDetail -> systemDetail.getSystemType().equalsIgnoreCase("SOURCE"))
                        .map(systemDetail -> SyncerMapper.MAPPER.mapConfigResponse(new ChangeConfig(0, systemDetail.getSystemCode(), systemDetail.getSystemType(), systemDetail.getSystemId())))
                        .collect(Collectors.toList()));
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("SUCCESS", "Fetch Change Config Success", configResponseCmds)).build();

            }
        } catch (Exception e) {
            throw new RecordNotFoundException("Failed to fetch config record", e);
        }
    }

    public Response fetchSystemDetails() {
        try {
            List<SystemDetail> systemDetails = syncerRepository.fetchSystemDetails();
            if (!CollectionUtils.isNotEmpty(systemDetails)) {
                return Response.status(HttpStatus.NO_CONTENT_204).entity(buildErrorResponse("NO_CONTENT", "No Record Exist")).build();
            }
            return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("SUCCESS", "Fetch Change Config Success", systemDetails)).build();
        } catch (Exception e) {
            throw new RecordNotFoundException("Failed to fetch system details", e);
        }
    }

    public Response createAdapterData(CreateAdapterRequest adapterRequest) {
        try {
            int sourceSystemId = 0;
            int integrationSystemId = 0;
            Map<String, Integer> systemDetails = new HashMap<>();
            List<SystemDetail> systemDetailList = syncerRepository.fetchSystemDetailsBySystemCode(Arrays.asList(adapterRequest.getSourceSystemCode(), adapterRequest.getIntegrationSystemCode()));
            systemDetails = systemDetailList.stream().filter(systemDetail -> systemDetail != null)
                    .collect(Collectors.toMap(systemDetail -> systemDetail.getSystemCode(), systemDetail -> systemDetail.getSystemId()));

            if (systemDetails.isEmpty()) {
                SystemDetail sourceSystemDetail = new SystemDetail(adapterRequest.getSourceSystemCode(), "SOURCE", String.format("%s Source System ", adapterRequest.getSourceSystemCode()));
                sourceSystemId = syncerRepository.saveSystemDetail(sourceSystemDetail);
                SystemDetail integrationSystemDetail = new SystemDetail(adapterRequest.getIntegrationSystemCode(), "INTEGRATION", String.format("%s Integration System ", adapterRequest.getIntegrationSystemCode()));
                integrationSystemId = syncerRepository.saveSystemDetail(integrationSystemDetail);
            } else if (!systemDetails.isEmpty()) {
                if (systemDetails.get(adapterRequest.getSourceSystemCode()) == null) {
                    SystemDetail sourceSystemDetail = new SystemDetail(adapterRequest.getSourceSystemCode(), "SOURCE", String.format("%s Source System ", adapterRequest.getSourceSystemCode()));
                    sourceSystemId = syncerRepository.saveSystemDetail(sourceSystemDetail);
                } else {
                    sourceSystemId = systemDetails.get(adapterRequest.getSourceSystemCode());
                }
                if (systemDetails.get(adapterRequest.getIntegrationSystemCode()) == null) {
                    SystemDetail integrationSystemDetail = new SystemDetail(adapterRequest.getIntegrationSystemCode(), "INTEGRATION", String.format("%s Integration System ", adapterRequest.getIntegrationSystemCode()));
                    integrationSystemId = syncerRepository.saveSystemDetail(integrationSystemDetail);
                } else {
                    integrationSystemId = systemDetails.get(adapterRequest.getIntegrationSystemCode());
                }
            }
            adapterRequest.setSourceSystemId(sourceSystemId);
            adapterRequest.setIntegrationSystemId(integrationSystemId);

            var adapterConfig = SyncerMapper.MAPPER.mapAdapterConfig(adapterRequest);
            var adapterConfigId = syncerRepository.saveAdapterConfig(adapterConfig);
            if (adapterConfigId != 0) {
                adapterRequest.setAdapterConfigId(adapterConfigId);
                return Response.status(HttpStatus.CREATED_201)
                        .entity(buildSuccessResponse("ADAPTER_CREATED", "Adapter record created successfully!",
                                adapterRequest)).build();
            }
        } catch (Exception e) {
            throw new RecordPersistException("Failed to create adapter record", e);
        }
        return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("ADAPTER_ERROR", "Error occurred while creating adapter record!")).build();
    }

    public Response updateAdapterData(UpdateAdapterRequest adapterRequest) {
        log.info("Starting adapter update process for [adapterConfigId={}] ", adapterRequest.getAdapterConfigId());
        try {
            var existingAdapterRecord = syncerRepository.findAdapterConfigByAdapterConfigId(adapterRequest.getAdapterConfigId());
            var adapterConfigRecordNeedToBeUpdated = SyncerMapper.MAPPER.updateWithNullAsNoChangeAdapter(adapterRequest, existingAdapterRecord);

            boolean updateStatus = syncerRepository.updateAdapterData(adapterConfigRecordNeedToBeUpdated);
            if (!updateStatus) {
                log.info("Update failed for [adapterConfigId={}] for [sourceSystem={}] and [integrationSystem={}] ", adapterRequest.getAdapterConfigId(), adapterConfigRecordNeedToBeUpdated.getSourceSystemCode(), adapterConfigRecordNeedToBeUpdated.getIntegrationSystemCode());
                return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("ERROR", "Update Adapter Config Failed")).build();
            } else {
                log.info("Update success for [adapterConfigId={}] for [sourceSystem={}] and [integrationSystem={}] ", adapterRequest.getAdapterConfigId(), adapterConfigRecordNeedToBeUpdated.getSourceSystemCode(), adapterConfigRecordNeedToBeUpdated.getIntegrationSystemCode());
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADAPTER_UPDATED", "Update Adapter record Success", adapterConfigRecordNeedToBeUpdated)).build();
            }
        } catch (Exception e) {
            throw new RecordPersistException("Failed to update adapter record", e);
        }
    }

    public Response fetchAdapterDetailsBySystemCodeOrSystemId(int adapterId) {
        List<AdapterConfig> adapterConfigs = new ArrayList<>();
        try {
            if (adapterId == 0) {
                adapterConfigs = syncerRepository.fetchAdapterConfigData();
            } else {
                var adapterConfig = syncerRepository.findAdapterConfigByAdapterConfigId(adapterId);
                CollectionUtils.addIgnoreNull(adapterConfigs, adapterConfig);
            }
            if (CollectionUtils.isNotEmpty(adapterConfigs))
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("SUCCESS", "Fetch Adapter Config Success", adapterConfigs)).build();

            return Response.status(HttpStatus.NO_CONTENT_204).entity(buildErrorResponse("NO CONTENT", "No Record Exist")).build();
        } catch (Exception e) {
            throw new RecordNotFoundException("Failed to fetch adapter record", e);
        }
    }


    public Response updateSystemData(SystemRequestCmd systemRequestCmd) {
        try {
            if (StringUtils.isNoneBlank(systemRequestCmd.getSystemCode())) {
                log.info("Starting system update process for [systemCode={}] ", systemRequestCmd.getSystemCode());
                List<SystemDetail> systemDetails = syncerRepository.fetchSystemDetailsBySystemCode(singletonList(systemRequestCmd.getSystemCode()));
                if (CollectionUtils.isNotEmpty(systemDetails)) {
                    var systemDetail = SyncerMapper.MAPPER.updateWithNullAsNoSystemDetail(systemRequestCmd, systemDetails.get(0));
                    boolean systemUpdate = syncerRepository.updateSystemDetail(systemDetail);
                    if (systemUpdate) {
                        log.info("Update success for  [systemCode={}] ", systemRequestCmd.getSystemCode());
                        return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("SYSTEM_UPDATED",
                                "Update System Detail record Success", systemDetail)).build();
                    }
                }
            }
            return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(STATUS_ERROR, "Update System Detail Failed")).build();
        } catch (Exception e) {
            throw new RecordPersistException("Failed to update config record", e);
        }
    }
}

