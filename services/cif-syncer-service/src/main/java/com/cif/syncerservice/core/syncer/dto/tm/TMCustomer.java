package com.cif.syncerservice.core.syncer.dto.tm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TMCustomer {
    @JsonProperty("id")
    private String id;
    @JsonProperty("status")
    private String status;
    @JsonProperty("identifiers")
    private List<TMIdentifier> identifiers = null;
    @JsonProperty("customer_details")
    private TMCustomerRecord customerDetails;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<TMIdentifier> getIdentifiers() {
        return identifiers;
    }

    public void setIdentifiers(List<TMIdentifier> identifiers) {
        this.identifiers = identifiers;
    }

    public TMCustomerRecord getCustomerDetails() {
        return customerDetails;
    }

    public void setCustomerDetails(TMCustomerRecord customerDetails) {
        this.customerDetails = customerDetails;
    }
}
