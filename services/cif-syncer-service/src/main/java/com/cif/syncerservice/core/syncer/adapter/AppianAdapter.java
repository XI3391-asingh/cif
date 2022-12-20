package com.cif.syncerservice.core.syncer.adapter;

import com.cif.syncerservice.core.syncer.domain.AdapterConfig;
import com.cif.syncerservice.core.syncer.domain.ChangeConfig;
import com.cif.syncerservice.core.syncer.dto.ApiResponse;
import com.cif.syncerservice.core.syncer.dto.Party;
import com.cif.syncerservice.core.syncer.dto.PartyRequest;
import com.cif.syncerservice.core.syncer.dto.appian.AppianRequest;
import com.cif.syncerservice.core.syncer.dto.appian.AppianResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import liquibase.pro.packaged.T;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Set;

public class AppianAdapter implements IntegrationAdapter<T> {
    private final CircuitBreaker circuitBreaker;
    private ObjectMapper objectMapper;
    private List<AdapterConfig> adapterConfigs;
    private ChangeConfig changeConfig;
    private HttpClient httpClient;

    public AppianAdapter(List<AdapterConfig> adapterConfigs, ChangeConfig changeConfig, HttpClient httpClient, ObjectMapper objectMapper, CircuitBreaker circuitBreaker) {
        this.adapterConfigs = adapterConfigs;
        this.changeConfig = changeConfig;
        this.httpClient = httpClient;
        this.objectMapper = objectMapper;
        this.circuitBreaker = circuitBreaker;
    }

    @Override
    public String buildPayloadBasedOnActiveEvent(PartyRequest partyRequest, String event) {
        String payload = null;
        try {
            Party party = partyRequest.getParty();
            AppianRequest appianRequest = new AppianRequest();

            appianRequest.setFullName(party.getFullName());
            appianRequest.setFirstName(party.getFirstName());
            appianRequest.setMiddleName(party.getMiddleName());
            appianRequest.setLastName(party.getLastName());
            appianRequest.setDateOfBirth(party.getDateOfBirth().toString());
            appianRequest.setMobileNumber(party.getPrimaryMobileNumber());
            appianRequest.setNationality(party.getNationality());
            appianRequest.setEmailId(party.getPrimaryEmail());
            appianRequest.setNationality(party.getNationality());
            appianRequest.setOnBoardingDate(party.getCreatedAt().toLocalDate().toString());
            appianRequest.setCustomerRiskCategory(party.getAmlRisk());
            if ("UPDATE".equalsIgnoreCase(event)) {
                appianRequest.setPartyId(party.getxRefIf());
            }

            payload = objectMapper.writeValueAsString(appianRequest);

        } catch (JsonProcessingException | NullPointerException e) {
            LOGGER.error("Error while creating Appian payload for {} for {} event", partyRequest.getSourceSystem(), event);
            payload = null;
        }
        return payload;
    }

    @Override
    public ApiResponse adapterProcessRecord(PartyRequest partyRequest, String event, Set<String> metaFields) {
        if (event.equalsIgnoreCase("UPDATE") && !validateIfUpdateAvailableOrNot(changeConfig, metaFields)) {
            return null;
        }
        String payload = buildPayloadBasedOnActiveEvent(partyRequest, event);
        final HttpRequest httpRequest = buildHttpRequest(changeConfig, event, partyRequest, payload);
        ApiResponse apiResponse = makeApiCall(httpRequest, httpClient, circuitBreaker);


        try {
            if (apiResponse != null && apiResponse.getIsSuccess()) {
                var response = objectMapper.readValue(apiResponse.getResponseBody(), AppianResponse.class);
                if (response != null && response.getSuccess() != null)
                    apiResponse.setxRefId(response.getSuccess().get(0).getPartyId());
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Error Occurred while converting {} appian success response into pojo for [partyId={}]", event, partyRequest.getParty().getPartyIdentifier());
        }
        apiResponse.setRequestUrl(httpRequest.uri().toString());
        apiResponse.setRequestBody(payload);
        return apiResponse;
    }

    @Override
    public HttpRequest buildHttpRequest(ChangeConfig changeConfig, String event, PartyRequest partyRequest, String payload) {
        try {
            StringBuilder endPointBuilder = new StringBuilder(changeConfig.getApiObject().getBaseUrl());
            endPointBuilder.append("/");
            endPointBuilder.append(changeConfig.getApiObject().getCreate().getEndPoint());
            var authToken = changeConfig.getApiObject().getCreate().getAuthToken();
            return HttpRequest.newBuilder().uri(new URI(endPointBuilder.toString()))
                    .headers("Content-Type", "application/json")
                    .header("Authorization", "Basic " + authToken)
                    .POST(HttpRequest.BodyPublishers.ofString(payload)).build();

        } catch (URISyntaxException | NullPointerException e) {
            LOGGER.error("Error occurred while creating {} {} http request for party id {} ", changeConfig.getSystemCode(), event, partyRequest.getParty().getPartyIdentifier());
        }
        return null;
    }
}
