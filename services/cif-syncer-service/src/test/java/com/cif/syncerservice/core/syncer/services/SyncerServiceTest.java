package com.cif.syncerservice.core.syncer.services;

import com.cif.syncerservice.api.*;
import com.cif.syncerservice.core.syncer.component.KafkaProducer;
import com.cif.syncerservice.core.syncer.domain.ApiDetails;
import com.cif.syncerservice.core.syncer.domain.*;
import com.cif.syncerservice.core.syncer.dto.PartyRequest;
import com.cif.syncerservice.core.syncer.dto.TopicData;
import com.cif.syncerservice.db.SyncerRepository;
import com.cif.syncerservice.resources.exceptions.RecordNotFoundException;
import com.cif.syncerservice.resources.exceptions.RecordPersistException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Arrays.asList;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class SyncerServiceTest {

    private final SyncerRepository syncerRepository = mock(SyncerRepository.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KafkaProducer kafkaProducer = mock(KafkaProducer.class);
    private final HttpClient mockHttpClient = mock(HttpClient.class);
    private final HttpResponse mockHttpResponse = mock(HttpResponse.class);

    private final CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");
    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    private final SyncerService syncerService = new SyncerService(syncerRepository, objectMapper, kafkaProducer, mockHttpClient, circuitBreaker, executor);

    ChangeConfig changeConfig;
    AdapterConfig adapterConfig = new AdapterConfig();
    private PartyRequest party;
    private TopicData topicData;
    private CreateConfigRequest createConfigRequest;
    private UpdateConfigRequest updateConfigRequest;

    @BeforeEach
    void setup() throws IOException {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        changeConfig = buildChangeConfig();

        adapterConfig.setIntegrationSystemCode("TM");
        adapterConfig.setIsCreate(true);
        adapterConfig.setSourceSystemCode("Onboarding");
        party = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        party.getParty().setSourceSystem("Onboarding");
        topicData = new TopicData(singleton("firstName"), party);
        createConfigRequest = objectMapper.readValue(getClass().getResource("/fixtures/config.json"), CreateConfigRequest.class);
        updateConfigRequest = objectMapper.readValue(getClass().getResource("/fixtures/config.json"), UpdateConfigRequest.class);

    }

    @Test
    @Order(1)
    void should_process_record_when_api_call_success() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        String kafkaValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(topicData);
        ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>("TEST", 1, 1, "CREATE", kafkaValue);
        String kafkaMessageTransactionId = UUID.randomUUID().toString();
        consumerRecord.headers().add(new RecordHeader("transactionId", kafkaMessageTransactionId.getBytes()));
        party.setKafkaTransactionId(kafkaMessageTransactionId);
        when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{mockHttpResponse}");
        when(syncerRepository.fetchSyncerTransactionDataByEventTransactionID(kafkaMessageTransactionId, 1)).thenReturn(null);
        when(syncerRepository.saveTransactionHistory(Mockito.any(SyncerTransaction.class))).thenReturn(1);
        when(syncerRepository.fetchPartyXrefByPartyId(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
        when(syncerRepository.savePartyXrefRecord(Mockito.any(PartyXref.class))).thenReturn(1);
        when(syncerRepository.updateTransactionHistory(Mockito.any(SyncerTransaction.class))).thenReturn(true);

        syncerService.processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));
        verify(syncerRepository, timeout(500).times(1)).fetchSyncerTransactionDataByEventTransactionID(kafkaMessageTransactionId, 1);
        verify(syncerRepository, timeout(500).times(1)).saveTransactionHistory(Mockito.any(SyncerTransaction.class));
        verify(syncerRepository, timeout(500).times(1)).fetchPartyXrefByPartyId(Mockito.anyString(), Mockito.anyInt());
        verify(syncerRepository, timeout(500).times(1)).savePartyXrefRecord(Mockito.any(PartyXref.class));
    }

    @Test
    @Order(2)
    void should_process_record_when_api_call_failed() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        String kafkaValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(topicData);
        ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>("TEST", 1, 1, "CREATE", kafkaValue);
        String kafkaMessageTransactionId = UUID.randomUUID().toString();
        consumerRecord.headers().add(new RecordHeader("transactionId", kafkaMessageTransactionId.getBytes()));
        party.setKafkaTransactionId(kafkaMessageTransactionId);

        when(syncerRepository.fetchSyncerTransactionDataByEventTransactionID(kafkaMessageTransactionId, 1)).thenReturn(null);
        when(syncerRepository.saveTransactionHistory(Mockito.any(SyncerTransaction.class))).thenReturn(1);
        when(syncerRepository.fetchPartyXrefByPartyId(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
        when(syncerRepository.savePartyXrefRecord(Mockito.any(PartyXref.class))).thenReturn(1);
        when(syncerRepository.updateTransactionHistory(Mockito.any(SyncerTransaction.class))).thenReturn(true);

        syncerService.processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));

        verify(syncerRepository, timeout(500).times(1)).fetchSyncerTransactionDataByEventTransactionID(kafkaMessageTransactionId, 1);
        verify(syncerRepository, timeout(500).times(1)).saveTransactionHistory(Mockito.any(SyncerTransaction.class));
        verify(syncerRepository, timeout(500).times(1)).fetchPartyXrefByPartyId(Mockito.anyString(), Mockito.anyInt());
        verify(syncerRepository, timeout(500).times(1)).savePartyXrefRecord(Mockito.any(PartyXref.class));
    }

    @Test
    @Order(3)
    void should_process_record_when_transaction_record_save_failed() throws IOException, InterruptedException {
        MockitoAnnotations.openMocks(this);
        String kafkaValue = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(topicData);
        ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>("TEST", 1, 1, "CREATE", kafkaValue);
        String kafkaMessageTransactionId = UUID.randomUUID().toString();
        consumerRecord.headers().add(new RecordHeader("transactionId", kafkaMessageTransactionId.getBytes()));
        party.setKafkaTransactionId(kafkaMessageTransactionId);

        when(syncerRepository.fetchSyncerTransactionDataByEventTransactionID(kafkaMessageTransactionId, 1)).thenReturn(null);
        when(syncerRepository.saveTransactionHistory(Mockito.any(SyncerTransaction.class))).thenThrow(new RuntimeException());
        when(syncerRepository.fetchPartyXrefByPartyId(Mockito.anyString(), Mockito.anyInt())).thenReturn(null);
        when(syncerRepository.savePartyXrefRecord(Mockito.any(PartyXref.class))).thenReturn(1);
        when(syncerRepository.updateTransactionHistory(Mockito.any(SyncerTransaction.class))).thenReturn(true);

        syncerService.processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));
        Thread.sleep(500);

        verify(syncerRepository, timeout(500).times(1)).fetchSyncerTransactionDataByEventTransactionID(kafkaMessageTransactionId, 1);
        verify(syncerRepository, timeout(500).times(1)).saveTransactionHistory(Mockito.any(SyncerTransaction.class));
        verify(syncerRepository, timeout(500).times(1)).fetchPartyXrefByPartyId(Mockito.anyString(), Mockito.anyInt());
        verify(syncerRepository, timeout(500).times(1)).savePartyXrefRecord(Mockito.any(PartyXref.class));
    }

    @Test
    @Order(4)
    void should_return_success_response_when_change_config_create_success() {
        SystemDetail systemDetail = new SystemDetail();
        systemDetail.setSystemId(1);
        systemDetail.setSystemCode("TEST");
        when(syncerRepository.fetchSystemDetailsBySystemCode(Mockito.anyList())).thenReturn(singletonList(systemDetail));
        when(syncerRepository.saveSystemDetail(Mockito.any(SystemDetail.class))).thenReturn(1);
        when(syncerRepository.saveConfigData(Mockito.any(ChangeConfig.class))).thenReturn(1);

        Response response = syncerService.createConfigData(createConfigRequest);

        assertEquals(HttpStatus.CREATED_201, response.getStatus());
        verify(syncerRepository, times(1)).fetchSystemDetailsBySystemCode(Mockito.anyList());
        verify(syncerRepository, times(1)).saveConfigData(Mockito.any(ChangeConfig.class));
    }

    @Test
    @Order(5)
    void should_return_success_response_when_change_config_update_success() {
        when(syncerRepository.findChangeConfigByConfigId(Mockito.anyInt())).thenReturn(buildChangeConfig());
        when(syncerRepository.updateConfigData(Mockito.any(ChangeConfig.class))).thenReturn(true);

        Response response = syncerService.updateConfigData(updateConfigRequest);

        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(syncerRepository, times(1)).findChangeConfigByConfigId(Mockito.anyInt());
        verify(syncerRepository, times(1)).updateConfigData(Mockito.any(ChangeConfig.class));
    }

    @Test
    @Order(6)
    void should_throw_exception_when_exception_occurred_while_change_config_persist() {
        when(syncerRepository.findChangeConfigByConfigId(Mockito.anyInt())).thenThrow(new RuntimeException());

        RecordPersistException exception = assertThrows(RecordPersistException.class, () -> {
            syncerService.updateConfigData(updateConfigRequest);
        });

        String expectedMessage = "Failed to update config record";
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(syncerRepository, times(1)).findChangeConfigByConfigId(Mockito.anyInt());
        verify(syncerRepository, times(0)).updateConfigData(Mockito.any(ChangeConfig.class));
    }

    @Test
    @Order(7)
    void should_return_error_response_when_change_config_update_failed() {
        when(syncerRepository.findChangeConfigByConfigId(Mockito.anyInt())).thenReturn(buildChangeConfig());
        when(syncerRepository.updateConfigData(Mockito.any(ChangeConfig.class))).thenReturn(false);

        Response response = syncerService.updateConfigData(updateConfigRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        verify(syncerRepository, times(1)).findChangeConfigByConfigId(Mockito.anyInt());
        verify(syncerRepository, times(1)).updateConfigData(Mockito.any(ChangeConfig.class));
    }

    @Test
    @Order(8)
    void should_return_success_response_when_change_config_present() {
        when(syncerRepository.findChangeConfigBySystemCode(Mockito.anyString())).thenReturn(buildChangeConfig());

        Response response = syncerService.fetchConfigDetailsBySystemCode("TEST");

        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(syncerRepository, times(1)).findChangeConfigBySystemCode(Mockito.anyString());
    }


    @Test
    @Order(9)
    void should_return_no_content_response_when_change_config_not_present() {
        when(syncerRepository.findChangeConfigBySystemCode(Mockito.anyString())).thenReturn(null);

        Response response = syncerService.fetchConfigDetailsBySystemCode("TEST");

        assertEquals(HttpStatus.NO_CONTENT_204, response.getStatus());
        verify(syncerRepository, times(1)).findChangeConfigBySystemCode(Mockito.anyString());
    }

    @Test
    @Order(10)
    void should_return_success_response_when_change_adapter_create_success() {
        SystemDetail sourceSystem = new SystemDetail();
        sourceSystem.setSystemId(1);
        sourceSystem.setSystemCode("TEST1");

        SystemDetail integrationSystem = new SystemDetail();
        integrationSystem.setSystemId(2);
        integrationSystem.setSystemCode("TE1");
        CreateAdapterRequest createAdapterRequest = CreateAdapterRequest.builder().integrationSystemCode("TE1").sourceSystemCode("TEST1").isCreate(false).build();

        when(syncerRepository.fetchSystemDetailsBySystemCode(asList(createAdapterRequest.getSourceSystemCode(), createAdapterRequest.getIntegrationSystemCode()))).thenReturn(asList(sourceSystem, integrationSystem));
        when(syncerRepository.saveAdapterConfig(Mockito.any(AdapterConfig.class))).thenReturn(1);

        Response response = syncerService.createAdapterData(createAdapterRequest);

        assertEquals(HttpStatus.CREATED_201, response.getStatus());
        verify(syncerRepository, times(1)).fetchSystemDetailsBySystemCode(asList(createAdapterRequest.getSourceSystemCode(), createAdapterRequest.getIntegrationSystemCode()));
        verify(syncerRepository, times(1)).saveAdapterConfig(Mockito.any(AdapterConfig.class));
    }

    @Test
    @Order(11)
    void should_return_success_response_when_change_adapter_create_with_system_success() {
        CreateAdapterRequest createAdapterRequest = CreateAdapterRequest.builder().integrationSystemCode("TE1").sourceSystemCode("TEST1").isCreate(false).build();
        SystemDetail sourceSystemDetail = new SystemDetail(createAdapterRequest.getSourceSystemCode(), "SOURCE", String.format("%s Source System ", createAdapterRequest.getSourceSystemCode()));
        SystemDetail integrationSystemDetail = new SystemDetail(createAdapterRequest.getIntegrationSystemCode(), "INTEGRATION", String.format("%s Integration System ", createAdapterRequest.getIntegrationSystemCode()));

        when(syncerRepository.fetchSystemDetailsBySystemCode(Mockito.anyList())).thenReturn(asList());
        when(syncerRepository.saveSystemDetail(Mockito.any(SystemDetail.class))).thenReturn(1);
        when(syncerRepository.saveSystemDetail(Mockito.any(SystemDetail.class))).thenReturn(1);
        when(syncerRepository.saveAdapterConfig(Mockito.any(AdapterConfig.class))).thenReturn(1);

        Response response = syncerService.createAdapterData(createAdapterRequest);

        assertEquals(HttpStatus.CREATED_201, response.getStatus());
        verify(syncerRepository, times(1)).fetchSystemDetailsBySystemCode(Mockito.anyList());
        verify(syncerRepository, times(2)).saveSystemDetail(Mockito.any(SystemDetail.class));
        verify(syncerRepository, times(1)).saveAdapterConfig(Mockito.any(AdapterConfig.class));
    }

    @Test
    @Order(12)
    void should_return_failed_response_when_change_adapter_create_with_system_failed() {
        CreateAdapterRequest createAdapterRequest = CreateAdapterRequest.builder().integrationSystemCode("TE1").sourceSystemCode("TEST1").isCreate(false).build();

        when(syncerRepository.fetchSystemDetailsBySystemCode(Mockito.anyList())).thenReturn(asList());
        when(syncerRepository.saveSystemDetail(Mockito.any(SystemDetail.class))).thenReturn(1);
        when(syncerRepository.saveSystemDetail(Mockito.any(SystemDetail.class))).thenReturn(1);
        when(syncerRepository.saveAdapterConfig(Mockito.any(AdapterConfig.class))).thenReturn(0);

        Response response = syncerService.createAdapterData(createAdapterRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        verify(syncerRepository, times(1)).fetchSystemDetailsBySystemCode(Mockito.anyList());
        verify(syncerRepository, times(2)).saveSystemDetail(Mockito.any(SystemDetail.class));
        verify(syncerRepository, times(1)).saveAdapterConfig(Mockito.any(AdapterConfig.class));
    }

    @Test
    @Order(13)
    void should_throw_exception_when_change_adapter_create_with_system_failed() {
        CreateAdapterRequest createAdapterRequest = CreateAdapterRequest.builder().integrationSystemCode("TE1").sourceSystemCode("TEST1").isCreate(false).build();
        String expectedMessage = "Failed to create adapter record";

        when(syncerRepository.fetchSystemDetailsBySystemCode(Mockito.anyList())).thenThrow(new RuntimeException());

        RecordPersistException exception = assertThrows(RecordPersistException.class, () -> {
            syncerService.createAdapterData(createAdapterRequest);
        });
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
        verify(syncerRepository, times(1)).fetchSystemDetailsBySystemCode(Mockito.anyList());
    }

    @Test
    @Order(14)
    void should_return_success_response_when_adapter_config_update_success() {
        UpdateAdapterRequest updateAdapterRequest = UpdateAdapterRequest.builder().adapterConfigId(1).isUpdate(true).isCreate(false).build();

        when(syncerRepository.findAdapterConfigByAdapterConfigId(Mockito.anyInt())).thenReturn(adapterConfig);
        when(syncerRepository.updateAdapterData(Mockito.any(AdapterConfig.class))).thenReturn(true);

        Response response = syncerService.updateAdapterData(updateAdapterRequest);

        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(syncerRepository, times(1)).findAdapterConfigByAdapterConfigId(Mockito.anyInt());
        verify(syncerRepository, times(1)).updateAdapterData(Mockito.any(AdapterConfig.class));
    }

    @Test
    @Order(15)
    void should_return_failed_response_when_adapter_config_update_failed() {
        UpdateAdapterRequest updateAdapterRequest = UpdateAdapterRequest.builder().adapterConfigId(1).isUpdate(true).isCreate(false).build();

        when(syncerRepository.findAdapterConfigByAdapterConfigId(Mockito.anyInt())).thenReturn(adapterConfig);
        when(syncerRepository.updateAdapterData(Mockito.any(AdapterConfig.class))).thenReturn(false);

        Response response = syncerService.updateAdapterData(updateAdapterRequest);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        verify(syncerRepository, times(1)).findAdapterConfigByAdapterConfigId(Mockito.anyInt());
        verify(syncerRepository, times(1)).updateAdapterData(Mockito.any(AdapterConfig.class));
    }

    @Test
    @Order(16)
    void should_return_error_response_when_adapter_config_update_throw_exception() {
        UpdateAdapterRequest updateAdapterRequest = UpdateAdapterRequest.builder().adapterConfigId(1).isUpdate(true).isCreate(false).build();
        String expectedMessage = "Failed to update adapter record";

        when(syncerRepository.findAdapterConfigByAdapterConfigId(Mockito.anyInt())).thenReturn(adapterConfig);
        when(syncerRepository.updateAdapterData(Mockito.any(AdapterConfig.class))).thenThrow(new RuntimeException());

        RecordPersistException exception = assertThrows(RecordPersistException.class, () -> {
            syncerService.updateAdapterData(updateAdapterRequest);
        });
        String actualMessage = exception.getMessage();

        assertEquals(expectedMessage, actualMessage);
        verify(syncerRepository, times(1)).findAdapterConfigByAdapterConfigId(Mockito.anyInt());
        verify(syncerRepository, times(1)).updateAdapterData(Mockito.any(AdapterConfig.class));
    }

    @Test
    @Order(17)
    void should_return_success_response_when_adapter_config_fetch_success() {
        FetchAdapterRequest fetchRequest = FetchAdapterRequest.builder().adapterId(1).build();

        when(syncerRepository.findAdapterConfigByAdapterConfigId(Mockito.anyInt())).thenReturn(adapterConfig);

        Response response = syncerService.fetchAdapterDetailsBySystemCodeOrSystemId(fetchRequest.getAdapterId());

        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(syncerRepository, times(1)).findAdapterConfigByAdapterConfigId(Mockito.anyInt());
    }

    @Test
    @Order(18)
    void should_return_bad_response_when_adapter_config_fetch_failed() {
        FetchAdapterRequest fetchRequest = FetchAdapterRequest.builder().adapterId(100).build();
        when(syncerRepository.findAdapterConfigByAdapterConfigId(100)).thenReturn(null);

        Response response = syncerService.fetchAdapterDetailsBySystemCodeOrSystemId(fetchRequest.getAdapterId());

        assertEquals(HttpStatus.NO_CONTENT_204, response.getStatus());
    }

    @Test
    @Order(19)
    void should_return_error_response_when_adapter_config_fetch_throw_exception() {
        FetchAdapterRequest fetchRequest = FetchAdapterRequest.builder().adapterId(1).build();
        String expectedMessage = "Failed to fetch adapter record";

        when(syncerRepository.findAdapterConfigByAdapterConfigId(Mockito.anyInt())).thenThrow(new RuntimeException());

        RecordNotFoundException exception = assertThrows(RecordNotFoundException.class, () -> {
            syncerService.fetchAdapterDetailsBySystemCodeOrSystemId(fetchRequest.getAdapterId());
        });
        String actualMessage = exception.getMessage();

        assertEquals(actualMessage, expectedMessage);
        verify(syncerRepository, times(1)).findAdapterConfigByAdapterConfigId(Mockito.anyInt());
    }

    private ChangeConfig buildChangeConfig() {
        ChangeConfig changeConfig = new ChangeConfig();
        changeConfig.setSystemId(1);
        changeConfig.setChangeConfigId(1);
        changeConfig.setSystemCode("TM");
        ApiAttributes apiAttributes = new ApiAttributes("/customer/", "Onboarding123");
        ApiDetails apiDetails = new ApiDetails("http://localhost:8080", apiAttributes, apiAttributes);
        changeConfig.setConfigFields(new String[]{"firstName"});
        changeConfig.setApiDetails("{\"baseUrl\":\"https:\\/\\/core-api-demo.xerus.tmachine.io\\/v1\\/\",\"create\":{\"endPoint\":\"customers\",\"authToken\":\"A0002298703723329582277!I1Ju\\/1ptLdYfZxH3Mnm+Y2pBmUs1tU4QlQU5Xxb0CtNhxw\\/NDK\\/f0cbYnXTcd0P6otLIuMFwnXvuWQmeY00zSarvPE8=\"},\"update\":{\"endPoint\":\"customers\\/{customerId}\",\"authToken\":\"A0002298703723329582277!I1Ju\\/1ptLdYfZxH3Mnm+Y2pBmUs1tU4QlQU5Xxb0CtNhxw\\/NDK\\/f0cbYnXTcd0P6otLIuMFwnXvuWQmeY00zSarvPE8=\"}}");
        changeConfig.setApiObject(apiDetails);
        return changeConfig;
    }
}
