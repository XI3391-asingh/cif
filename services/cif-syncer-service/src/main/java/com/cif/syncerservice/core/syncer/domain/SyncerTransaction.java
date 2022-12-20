package com.cif.syncerservice.core.syncer.domain;


import java.time.LocalDateTime;

public class SyncerTransaction {
    private int syncerTransactionId;
    private String partyId;
    private String eventType;
    private String eventTransactionId;
    private String kafkaPayload;
    private String apiEndpoint;
    private String apiRequest;
    private String apiResponse;
    private String systemCode;

    private int systemId;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public int getSyncerTransactionId() {
        return syncerTransactionId;
    }

    public void setSyncerTransactionId(int syncerTransactionId) {
        this.syncerTransactionId = syncerTransactionId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getEventTransactionId() {
        return eventTransactionId;
    }

    public void setEventTransactionId(String eventTransactionId) {
        this.eventTransactionId = eventTransactionId;
    }

    public String getKafkaPayload() {
        return kafkaPayload;
    }

    public void setKafkaPayload(String kafkaPayload) {
        this.kafkaPayload = kafkaPayload;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public String getApiRequest() {
        return apiRequest;
    }

    public void setApiRequest(String apiRequest) {
        this.apiRequest = apiRequest;
    }

    public String getApiResponse() {
        return apiResponse;
    }

    public void setApiResponse(String apiResponse) {
        this.apiResponse = apiResponse;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    @Override
    public String toString() {
        return "SyncerTransaction{"
                + "syncerTransactionId=" + syncerTransactionId
                + ", partyId=" + partyId
                + ", eventType='" + eventType + '\''
                + ", eventTransactionId='" + eventTransactionId + '\''
                + ", kafkaPayload='" + kafkaPayload + '\''
                + ", apiEndpoint='" + apiEndpoint + '\''
                + ", apiRequest='" + apiRequest + '\''
                + ", apiResponse='" + apiResponse + '\''
                + ", systemCode='" + systemCode + '\''
                + ", systemId=" + systemId
                + ", status='" + status + '\''
                + ", createdAt=" + createdAt
                + ", updatedAt=" + updatedAt
                + '}';
    }
}
