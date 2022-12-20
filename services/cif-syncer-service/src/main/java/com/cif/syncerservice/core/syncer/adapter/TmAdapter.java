package com.cif.syncerservice.core.syncer.adapter;

import com.cif.syncerservice.core.syncer.domain.AdapterConfig;
import com.cif.syncerservice.core.syncer.domain.ChangeConfig;
import com.cif.syncerservice.core.syncer.dto.ApiResponse;
import com.cif.syncerservice.core.syncer.dto.Party;
import com.cif.syncerservice.core.syncer.dto.PartyRequest;
import com.cif.syncerservice.core.syncer.dto.SyncerEnum;
import com.cif.syncerservice.core.syncer.dto.tm.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import liquibase.pro.packaged.T;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Set;

import static java.util.Collections.singletonList;

public class TmAdapter implements IntegrationAdapter<T> {
    private final CircuitBreaker circuitBreaker;
    private ObjectMapper objectMapper;
    private List<AdapterConfig> adapterConfigs;
    private ChangeConfig changeConfig;
    private HttpClient httpClient;

    public TmAdapter(List<AdapterConfig> adapterConfigs, ChangeConfig changeConfig, HttpClient httpClient, ObjectMapper objectMapper, CircuitBreaker circuitBreaker) {
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
            TMCustomerRecord customerDetail = new TMCustomerRecord();
            if (StringUtils.isNoneBlank(party.getSalutation()))
                customerDetail.setTitle(SyncerEnum.valueOf(party.getSalutation().toUpperCase().replace(".", "")).getLabel());
            customerDetail.setFirstName(party.getFirstName());
            customerDetail.setMiddleName(party.getMiddleName());
            customerDetail.setLastName(party.getLastName());
            customerDetail.setDob(party.getDateOfBirth());
            customerDetail.setMobilePhoneNumber(party.getPrimaryMobileNumber());
            if (StringUtils.isNoneBlank(party.getGender()))
                customerDetail.setGender(SyncerEnum.valueOf(party.getGender().toUpperCase()).getLabel());
            customerDetail.setNationality(party.getNationality());
            customerDetail.setEmailAddress(party.getPrimaryEmail());
            customerDetail.setCountryOfResidence(party.getCountryResidence());
            if (StringUtils.isNoneBlank(party.getPrimaryEmail()))
                customerDetail.setContactMethod(SyncerEnum.CUSTOMER_CONTACT_METHOD_EMAIL.name());
            else customerDetail.setContactMethod(SyncerEnum.CUSTOMER_CONTACT_METHOD_SMS.name());
            customerDetail.setAccessibility(SyncerEnum.CUSTOMER_ACCESSIBILITY_UNKNOWN.name());

            TMIdentifier usernameIdentifier = new TMIdentifier();
            usernameIdentifier.setIdentifierType(SyncerEnum.IDENTIFIER_TYPE_USERNAME.name());
            usernameIdentifier.setIdentifier(String.valueOf(party.getPartyIdentifier()));

            TMCustomer customerObject = new TMCustomer();
            customerObject.setCustomerDetails(customerDetail);
            if (StringUtils.isNoneBlank(party.getStatus()))
                customerObject.setStatus(SyncerEnum.valueOf(party.getStatus().toUpperCase()).getLabel());
            customerObject.setId(String.valueOf(party.getPartyIdentifier()));
            customerObject.setIdentifiers(singletonList(usernameIdentifier));

            TmRequest tmRequest = new TmRequest();
            tmRequest.setCustomer(customerObject);
            if ("UPDATE".equalsIgnoreCase(event)) {
                UpdateMask updateMask = new UpdateMask(singletonList("customer_details"));
                tmRequest.setUpdateMask(updateMask);
            }
            tmRequest.setRequestId(partyRequest.getKafkaTransactionId());
            payload = objectMapper.writeValueAsString(tmRequest);
        } catch (JsonProcessingException | NullPointerException e) {
            LOGGER.error("Error while creating TM payload for {} for {} event", partyRequest.getSourceSystem(), event);
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
                var response = objectMapper.readValue(apiResponse.getResponseBody(), TMCustomer.class);
                if (response != null)
                    apiResponse.setxRefId(response.getId());
            }
        } catch (JsonProcessingException e) {
            LOGGER.error("Error Occurred while converting {} tm success response into pojo for [partyId={}]", event, partyRequest.getParty().getPartyIdentifier());
        }
        apiResponse.setRequestUrl(httpRequest.uri().toString());
        apiResponse.setRequestBody(payload);
        return apiResponse;
    }

    @Override
    public HttpRequest buildHttpRequest(ChangeConfig changeConfig, String event, PartyRequest partyRequest, String payload) {
        try {
            StringBuilder endPointBuilder = new StringBuilder(changeConfig.getApiObject().getBaseUrl());
            if (changeConfig.getApiObject().getCreate() != null && "CREATE".equalsIgnoreCase(event)) {
                endPointBuilder.append(changeConfig.getApiObject().getCreate().getEndPoint());
                var authToken = changeConfig.getApiObject().getCreate().getAuthToken();
                return HttpRequest.newBuilder().uri(new URI(endPointBuilder.toString())).headers("Content-Type", "application/json").header("X-Auth-Token", authToken).POST(HttpRequest.BodyPublishers.ofString(payload)).build();
            } else if (changeConfig.getApiObject().getUpdate() != null && "UPDATE".equalsIgnoreCase(event)) {
                endPointBuilder.append(changeConfig.getApiObject().getCreate().getEndPoint());
                endPointBuilder.append("/");
                endPointBuilder.append(partyRequest.getParty().getPartyIdentifier());
                var authToken = changeConfig.getApiObject().getUpdate().getAuthToken();
                return HttpRequest.newBuilder().uri(new URI(endPointBuilder.toString())).headers("Content-Type", "application/json").header("X-Auth-Token", authToken).PUT(HttpRequest.BodyPublishers.ofString(payload)).build();
            }
        } catch (URISyntaxException | NullPointerException e) {
            LOGGER.error("Error occurred while creating {} {} http request for party id {} ", changeConfig.getSystemCode(), event, partyRequest.getParty().getPartyIdentifier());
        }
        return null;
    }

}
