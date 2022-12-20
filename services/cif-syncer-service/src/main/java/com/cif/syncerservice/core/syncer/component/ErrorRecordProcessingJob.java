package com.cif.syncerservice.core.syncer.component;

import com.cif.syncerservice.core.syncer.domain.ApiDetails;
import com.cif.syncerservice.core.syncer.domain.ChangeConfig;
import com.cif.syncerservice.core.syncer.domain.PartyXref;
import com.cif.syncerservice.core.syncer.domain.SyncerTransaction;
import com.cif.syncerservice.core.syncer.dto.ApiResponse;
import com.cif.syncerservice.core.syncer.dto.appian.AppianResponse;
import com.cif.syncerservice.core.syncer.dto.tm.TMCustomer;
import com.cif.syncerservice.core.syncer.dto.tm.TmRequest;
import com.cif.syncerservice.db.SyncerRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public class ErrorRecordProcessingJob implements Job {
    public static final String ERROR = "ERROR";
    private Logger logger = LoggerFactory.getLogger(ErrorRecordProcessingJob.class);

    private final ObjectMapper objectMapper = new ObjectMapper();
    public static final String TM = "TM";
    private static final String APPIAN = "APPIAN";


    public void execute(JobExecutionContext jobContext) throws JobExecutionException {

        SchedulerContext schedulerContext = new SchedulerContext();
        try {
            schedulerContext = jobContext.getScheduler().getContext();
        } catch (SchedulerException e1) {
            logger.error("Error Occurred while getting scheduler context. Error Message {}", e1.getMessage());
        }
        try {
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
            SyncerRepository syncerRepository = (SyncerRepository) schedulerContext.get("repository");
            HttpClient httpClient = (HttpClient) schedulerContext.get("httpclient");
            logger.info("Error Record Processing start {} ", jobContext.getFireTime());
            List<ChangeConfig> changeConfigList = syncerRepository.fetchChangeConfigData();
            List<SyncerTransaction> syncerTransactionList = syncerRepository.fetchSyncerTransactionByTransactionStatusOrderByUpdatedAt(ERROR);
            syncerTransactionList.stream()
                    .filter(transactionRecord -> transactionRecord.getStatus().equalsIgnoreCase(ERROR))
                    .forEach(transactionRecord -> recordProcessor(transactionRecord, changeConfigList, httpClient, syncerRepository));

            logger.info("Error Record Processing end {} ", jobContext.getJobRunTime());
        } catch (Exception e) {
            logger.error("Error Occurred while building error record processor. Error Message {}", e.getMessage());
        }
    }

    private void recordProcessor(SyncerTransaction transactionRecord, List<ChangeConfig> changeConfigList, HttpClient httpClient, SyncerRepository syncerRepository) {
        Optional<ChangeConfig> changeConfig = changeConfigList.stream()
                .filter(configRecord -> configRecord != null)
                .filter(configRecord -> configRecord.getSystemId() == transactionRecord.getSystemId())
                .findAny();

        if (changeConfig.isPresent()) {
            ChangeConfig configData = changeConfig.get();
            PartyXref partyXref = syncerRepository.fetchPartyXrefByPartyId(transactionRecord.getPartyId(), transactionRecord.getSystemId());
            try {
                String payload = null;
                if (configData.getSystemCode().equalsIgnoreCase(TM)) {
                    TmRequest request = objectMapper.readValue(transactionRecord.getApiRequest(), TmRequest.class);
                    request.setRequestId(request.getRequestId() + "RE-P");
                    payload = objectMapper.writeValueAsString(request);
                } else {
                    payload = objectMapper.writeValueAsString(transactionRecord.getApiRequest());
                }
                configData.setApiObject(objectMapper.readValue(configData.getApiDetails(), ApiDetails.class));
                ApiResponse apiResponse = makeApiCall(configData, transactionRecord.getEventType(), payload, httpClient);
                transactionRecord.setApiResponse(objectMapper.writeValueAsString(apiResponse.getResponseBody()));
                if (apiResponse.getIsSuccess()) {
                    if (apiResponse.getStatusCode() == 500 || apiResponse.getStatusCode() == 403) {
                        transactionRecord.setStatus(ERROR);
                    } else if (apiResponse.getStatusCode() == 200) {
                        buildXrefIdAndTransactionStatusForUpdate(transactionRecord, configData, apiResponse);
                    } else {
                        transactionRecord.setStatus("UNSUCCESSFUL");
                    }
                    transactionRecord.setUpdatedAt(LocalDateTime.now());
                    syncerRepository.updateTransactionHistory(transactionRecord);
                    logger.info("{} event syncer transaction record update complete for {}", transactionRecord.getEventType(), configData.getSystemCode());
                    partyXref.setXrefId(apiResponse.getxRefId());
                    syncerRepository.updatePartyXrefRecord(partyXref);
                    logger.info("Party X-Ref record update complete for party id{}", transactionRecord.getPartyId());
                } else {
                    transactionRecord.setStatus(ERROR);
                    transactionRecord.setUpdatedAt(LocalDateTime.now());
                    syncerRepository.updateTransactionHistory(transactionRecord);
                    logger.info("{} event syncer transaction record update complete for {}", transactionRecord.getEventType(), configData.getSystemCode());
                }
            } catch (JsonProcessingException e) {
                logger.error("Error occurred while converting api detail {}", e.getMessage());
            }
        }
    }

    private void buildXrefIdAndTransactionStatusForUpdate(SyncerTransaction transactionRecord, ChangeConfig configData, ApiResponse apiResponse) throws JsonProcessingException {
        if (configData.getSystemCode().equalsIgnoreCase(TM)) {
            var response = objectMapper.readValue(apiResponse.getResponseBody(), TMCustomer.class);
            if (response != null)
                apiResponse.setxRefId(response.getId());
        } else if (configData.getSystemCode().equalsIgnoreCase(APPIAN)) {
            var response = objectMapper.readValue(apiResponse.getResponseBody(), AppianResponse.class);
            if (response != null && response.getSuccess() != null)
                apiResponse.setxRefId(response.getSuccess().get(0).getPartyId());
        }
        transactionRecord.setStatus("SUCCESSFUL");
    }

    public ApiResponse makeApiCall(ChangeConfig changeConfig, String event, String
            payload, HttpClient httpClient) {
        HttpRequest httpRequest = null;
        try {
            StringBuilder endPointBuilder = new StringBuilder(changeConfig.getApiObject().getBaseUrl());
            if (changeConfig.getApiObject().getCreate() != null && "CREATE".equalsIgnoreCase(event)) {
                endPointBuilder.append(changeConfig.getApiObject().getCreate().getEndPoint());
                var authToken = changeConfig.getApiObject().getCreate().getAuthToken();
                httpRequest = HttpRequest.newBuilder().uri(new URI(endPointBuilder.toString())).headers("Content-Type", "application/json").header("X-Auth-Token", authToken).POST(HttpRequest.BodyPublishers.ofString(payload)).build();
            } else if (changeConfig.getApiObject().getUpdate() != null && "UPDATE".equalsIgnoreCase(event)) {
                endPointBuilder.append(changeConfig.getApiObject().getCreate().getEndPoint());
                var authToken = changeConfig.getApiObject().getUpdate().getAuthToken();
                httpRequest = HttpRequest.newBuilder().uri(new URI(endPointBuilder.toString())).headers("Content-Type", "application/json").header("X-Auth-Token", authToken).PUT(HttpRequest.BodyPublishers.ofString(payload)).build();
            }
        } catch (URISyntaxException | NullPointerException e) {
            logger.error("Error occurred while creating {} {} http request ", changeConfig.getSystemCode(), event);
        }

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setIsSuccess(false);
        HttpResponse<String> httpResponse = null;
        try {
            httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            logger.error("Exception occurred while calling TM API .Error Message{}", e.getMessage());
        }
        try {
            if (httpResponse != null) {
                apiResponse.setStatusCode(httpResponse.statusCode());
                apiResponse.setResponseBody(httpResponse.body());
                if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 400)
                    apiResponse.setIsSuccess(true);
            }
        } catch (NullPointerException e) {
            logger.error("Exception occurred while building api response .Error Message{}", e.getMessage());
        }
        return apiResponse;
    }
}
