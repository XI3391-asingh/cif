package com.cif.cifservice.core.party.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Set;

public final class EncryptionConfig {
    @NotNull
    @Valid
    private String secretKey;

    @NotNull
    @Valid
    private String encryptionFlag;

    @NotNull
    @Valid
    private Set<String> fields;

    @JsonProperty("secretKey")
    public String getSecretKey() {
        return secretKey;
    }

    @JsonProperty("encryptionFlag")
    public String getEncryptionFlag() {
        return encryptionFlag;
    }

    @JsonProperty("fields")
    public Set<String> getFields() {
        return fields;
    }
}
