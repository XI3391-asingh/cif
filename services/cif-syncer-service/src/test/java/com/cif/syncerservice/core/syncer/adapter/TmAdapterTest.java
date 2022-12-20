package com.cif.syncerservice.core.syncer.adapter;

import com.cif.syncerservice.core.syncer.domain.AdapterConfig;
import com.cif.syncerservice.core.syncer.domain.ApiAttributes;
import com.cif.syncerservice.core.syncer.domain.ApiDetails;
import com.cif.syncerservice.core.syncer.domain.ChangeConfig;
import com.cif.syncerservice.core.syncer.dto.ApiResponse;
import com.cif.syncerservice.core.syncer.dto.PartyRequest;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TmAdapterTest {

    ObjectMapper objectMapper = new ObjectMapper();

    CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("testName");

    HttpRequest httpRequest = mock(HttpRequest.class);

    HttpClient mockHttpClient = mock(HttpClient.class);

    HttpResponse mockHttpResponse = mock(HttpResponse.class);

    TmAdapter tmAdapter;

    List<AdapterConfig> adapterConfigs = new ArrayList<>();
    List<ChangeConfig> changeConfigs = new ArrayList<>();

    @BeforeEach()
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ChangeConfig changeConfig = new ChangeConfig();
        ApiAttributes apiAttributes = new ApiAttributes("/customer/", "abc123");
        ApiDetails apiDetails = new ApiDetails("http://localhost:8080", apiAttributes, apiAttributes);
        changeConfig.setConfigFields(new String[]{"firstName"});
        changeConfig.setApiObject(apiDetails);
        changeConfigs.add(changeConfig);
        AdapterConfig adapterConfig = new AdapterConfig();
        adapterConfig.setIntegrationSystemCode("TM");
        adapterConfig.setIsCreate(true);
        adapterConfig.setSourceSystemCode("ABC");
        adapterConfigs.add(adapterConfig);
        tmAdapter = new TmAdapter(adapterConfigs, changeConfigs.get(0), mockHttpClient, objectMapper, circuitBreaker);
    }

    @Test
    void should_return_true_if_mapping_is_active_based_on_event() throws IOException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");

        var response = tmAdapter.validateAdapter(adapterConfigs, "ABC", "TM", "CREATE");

        assertTrue(response);
    }

    @Test
    void should_return_false_if_mapping_is_active_based_on_event() throws IOException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");

        var response = tmAdapter.validateAdapter(adapterConfigs, "PQR", "TM", "CREATE");

        assertFalse(response);
    }

    @Test
    void should_return_payload_when_event_type_is_create() throws IOException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");

        var response = tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE");

        assertNotNull(response);
        assertTrue(response.contains("customer_details"));
        assertTrue(response.contains("Mukesh"));
    }

    @Test
    void should_return_payload_when_event_type_is_update() throws IOException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");

        var response = tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "UPDATE");

        assertNotNull(response);
        assertTrue(response.contains("customer_details"));
        assertTrue(response.contains("Mukesh"));
    }

    @Test
    void should_validate_circuit_breaker() throws IOException, InterruptedException, URISyntaxException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

        assertThat(metrics.getNumberOfBufferedCalls()).isZero();
        when(mockHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{mockHttpResponse}");
        Supplier<HttpResponse<String>> supplier = circuitBreaker.decorateSupplier(() -> {
            try {
                return mockHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        HttpResponse result = supplier.get();
        assertThat(metrics.getNumberOfBufferedCalls()).isEqualTo(1);
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(1);
        assertThat(result.statusCode()).isEqualTo(200);
    }


    @Test
    void should_return_success_response_when_create_api_call_success() throws IOException, InterruptedException, URISyntaxException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

        HttpRequest httpRequest = tmAdapter.buildHttpRequest(changeConfigs.get(0), "CREATE", partyRequest, tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE"));

        assertThat(metrics.getNumberOfBufferedCalls()).isZero();
        when(mockHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{mockHttpResponse}");
        ApiResponse result = tmAdapter.adapterProcessRecord(partyRequest, "CREATE", Collections.singleton("firstName"));

        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(metrics.getNumberOfBufferedCalls()).isEqualTo(1);
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(1);
    }

    @Test
    void should_return_error_api_response_when_create_api_call_return_bad_request() throws IOException, InterruptedException, URISyntaxException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

        HttpRequest httpRequest = tmAdapter.buildHttpRequest(changeConfigs.get(0), "CREATE", partyRequest, tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE"));

        assertThat(metrics.getNumberOfBufferedCalls()).isZero();
        when(mockHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(null);
        ApiResponse result = tmAdapter.adapterProcessRecord(partyRequest, "CREATE", Collections.singleton("firstName"));

        assertThat(result.getIsSuccess()).isEqualTo(false);
        assertThat(metrics.getNumberOfBufferedCalls()).isEqualTo(1);
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(1);
    }

    @Test
    void should_throw_exception_when_create_api_call_failed() throws IOException, InterruptedException, URISyntaxException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

        HttpRequest httpRequest = tmAdapter.buildHttpRequest(changeConfigs.get(0), "CREATE", partyRequest, tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE"));

        assertThat(metrics.getNumberOfBufferedCalls()).isZero();
        when(mockHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(new IOException());
        ApiResponse result = tmAdapter.adapterProcessRecord(partyRequest, "CREATE", Collections.singleton("firstName"));

        assertThat(result.getIsSuccess()).isEqualTo(false);
        assertThat(metrics.getNumberOfBufferedCalls()).isEqualTo(1);
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(1);
    }

    @Test
    void should_return_success_response_when_update_api_call_success() throws IOException, InterruptedException, URISyntaxException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

        HttpRequest httpRequest = tmAdapter.buildHttpRequest(changeConfigs.get(0), "UPDATE", partyRequest, tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE"));

        assertThat(metrics.getNumberOfBufferedCalls()).isZero();
        when(mockHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(mockHttpResponse);
        when(mockHttpResponse.statusCode()).thenReturn(200);
        when(mockHttpResponse.body()).thenReturn("{mockHttpResponse}");
        ApiResponse result = tmAdapter.adapterProcessRecord(partyRequest, "UPDATE", Collections.singleton("firstName"));

        assertThat(result.getStatusCode()).isEqualTo(200);
        assertThat(metrics.getNumberOfBufferedCalls()).isEqualTo(1);
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(1);
    }

    @Test
    void should_return_error_api_response_when_update_api_call_return_bad_request() throws IOException, InterruptedException, URISyntaxException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

        HttpRequest httpRequest = tmAdapter.buildHttpRequest(changeConfigs.get(0), "UPDATE", partyRequest, tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE"));

        assertThat(metrics.getNumberOfBufferedCalls()).isZero();
        when(mockHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(null);
        ApiResponse result = tmAdapter.adapterProcessRecord(partyRequest, "UPDATE", Collections.singleton("firstName"));

        assertThat(result.getIsSuccess()).isEqualTo(false);
        assertThat(metrics.getNumberOfBufferedCalls()).isEqualTo(1);
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(1);
    }

    @Test
    void should_throw_exception_when_update_api_call_failed() throws IOException, InterruptedException, URISyntaxException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");
        CircuitBreaker.Metrics metrics = circuitBreaker.getMetrics();

        HttpRequest httpRequest = tmAdapter.buildHttpRequest(changeConfigs.get(0), "CREATE", partyRequest, tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE"));

        assertThat(metrics.getNumberOfBufferedCalls()).isZero();
        when(mockHttpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenThrow(new InterruptedException());
        ApiResponse result = tmAdapter.adapterProcessRecord(partyRequest, "CREATE", Collections.singleton("firstName"));

        assertThat(result.getIsSuccess()).isEqualTo(false);
        assertThat(metrics.getNumberOfBufferedCalls()).isEqualTo(1);
        assertThat(metrics.getNumberOfFailedCalls()).isZero();
        assertThat(metrics.getNumberOfSuccessfulCalls()).isEqualTo(1);
    }

    @Test
    void should_return_payload_when_build_payload_success() throws IOException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");

        var response = tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE");

        assertNotNull(response);
    }

    @Test
    void should_return_empty_payload_when_build_payload_throws_exception() throws IOException {
        PartyRequest partyRequest = new PartyRequest();
        partyRequest.setSourceSystem("ABC");

        var response = tmAdapter.buildPayloadBasedOnActiveEvent(partyRequest, "CREATE");

        assertNull(response);
    }

    @Test
    void should_return_http_request_when_build_request_success() throws IOException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");

        var response = tmAdapter.buildHttpRequest(changeConfigs.get(0), "CREATE", partyRequest, "");

        assertNotNull(response);
    }

    @Test
    void should_return_null_when_build_request_throws_exception() throws IOException {
        PartyRequest partyRequest = objectMapper.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequest.class);
        partyRequest.setKafkaTransactionId("1");
        changeConfigs.get(0).setApiObject(null);

        var response = tmAdapter.buildHttpRequest(changeConfigs.get(0), "CREATE", partyRequest, "");

        assertNull(response);
    }

}