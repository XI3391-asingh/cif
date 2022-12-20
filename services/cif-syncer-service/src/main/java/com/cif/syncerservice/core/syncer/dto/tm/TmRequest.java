package com.cif.syncerservice.core.syncer.dto.tm;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TmRequest {

    @JsonProperty("request_id")
    private String requestId;
    @JsonProperty("customer")
    private TMCustomer customer;
    @JsonProperty("update_mask")
    private UpdateMask updateMask;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public TMCustomer getCustomer() {
        return customer;
    }

    public void setCustomer(TMCustomer customer) {
        this.customer = customer;
    }

    public UpdateMask getUpdateMask() {
        return updateMask;
    }

    public void setUpdateMask(UpdateMask updateMask) {
        this.updateMask = updateMask;
    }
}
