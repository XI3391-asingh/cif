package com.cif.syncerservice.core.syncer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PartyRequest {
    private Party party;

    @JsonIgnoreProperties
    private String event;
    @JsonIgnoreProperties
    private String kafkaTransactionId;

    @JsonIgnoreProperties
    private String sourceSystem;
    @JsonIgnoreProperties
    private String endPoint;
    @JsonIgnoreProperties
    private String currentPayload;
    @JsonIgnoreProperties
    private String previousPayload;

    public PartyRequest() {
    }

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }

    public String getKafkaTransactionId() {
        return kafkaTransactionId;
    }

    public void setKafkaTransactionId(String kafkaTransactionId) {
        this.kafkaTransactionId = kafkaTransactionId;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getCurrentPayload() {
        return currentPayload;
    }

    public void setCurrentPayload(String currentPayload) {
        this.currentPayload = currentPayload;
    }

    public String getPreviousPayload() {
        return previousPayload;
    }

    public void setPreviousPayload(String previousPayload) {
        this.previousPayload = previousPayload;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
