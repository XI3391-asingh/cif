package com.cif.syncerservice.core.syncer.adapter;

import com.cif.syncerservice.core.syncer.domain.AdapterConfig;
import com.cif.syncerservice.core.syncer.domain.ChangeConfig;
import com.cif.syncerservice.core.syncer.dto.ApiResponse;
import com.cif.syncerservice.core.syncer.dto.PartyRequest;
import io.github.resilience4j.circuitbreaker.CallNotPermittedException;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface IntegrationAdapter<T> {
    Logger LOGGER = LoggerFactory.getLogger(IntegrationAdapter.class);

    String buildPayloadBasedOnActiveEvent(PartyRequest partyRequest, String event);

    ApiResponse adapterProcessRecord(PartyRequest partyRequest, String event, Set<String> metaFields);

    HttpRequest buildHttpRequest(ChangeConfig changeConfig, String event, PartyRequest partyRequest, String payload);

    default boolean validateAdapter(List<AdapterConfig> adapterConfig, String sourceSystem, String integrationSystem, String event) {
        return adapterConfig.stream().filter(config -> config.getSourceSystemCode().equalsIgnoreCase(sourceSystem) && config.getIntegrationSystemCode().equalsIgnoreCase(integrationSystem))
                .filter(config -> (event.equalsIgnoreCase("CREATE") && config.getIsCreate())
                        || (event.equalsIgnoreCase("UPDATE") && config.getIsUpdate()))
                .count() > 0;
    }

    default ApiResponse makeApiCall(HttpRequest httpRequest, HttpClient httpClient, CircuitBreaker circuitBreaker) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setIsSuccess(false);
        HttpResponse<String> httpResponse = null;
        try {
            Supplier<HttpResponse<String>> supplier = circuitBreaker.decorateSupplier(() -> {
                try {
                    return httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
                } catch (IOException e) {
                    LOGGER.error("Exception occurred while calling TM API .Error Message{}", e.getMessage());
                } catch (InterruptedException e) {
                    LOGGER.error("Exception occurred while calling TM API .Error Message{}", e.getMessage());
                }
                return null;
            });
            httpResponse = supplier.get();
        } catch (CallNotPermittedException e) {
            LOGGER.error("Circuit breaker is not permitting call for TM API. Error Message {}", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Exception occurred while calling TM API .Error Message{}", e.getMessage());
        }
        try {
            if (httpResponse != null) {
                apiResponse.setStatusCode(httpResponse.statusCode());
                apiResponse.setResponseBody(httpResponse.body());
                if (httpResponse.statusCode() == 200 || httpResponse.statusCode() == 400)
                    apiResponse.setIsSuccess(true);
            }
        } catch (NullPointerException e) {
            LOGGER.error("Exception occurred while building api response .Error Message{}", e.getMessage());
        }
        return apiResponse;
    }

    default boolean validateIfUpdateAvailableOrNot(ChangeConfig changeConfig, Set<String> metaFields) {
        return Stream.of(changeConfig.getConfigFields())
                .anyMatch(value -> metaFields.contains(value));
    }
}
