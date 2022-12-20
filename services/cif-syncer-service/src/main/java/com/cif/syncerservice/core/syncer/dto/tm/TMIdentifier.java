package com.cif.syncerservice.core.syncer.dto.tm;

import com.fasterxml.jackson.annotation.JsonProperty;


public class TMIdentifier {
    @JsonProperty("identifier_type")
    private String identifierType;
    @JsonProperty("identifier")
    private String identifier;

    public String getIdentifierType() {
        return identifierType;
    }

    public void setIdentifierType(String identifierType) {
        this.identifierType = identifierType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }
}
