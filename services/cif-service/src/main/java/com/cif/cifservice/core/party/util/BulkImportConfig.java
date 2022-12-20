package com.cif.cifservice.core.party.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class BulkImportConfig {
    @NotNull
    @Valid
    private String tempPath;

    @JsonProperty("tempPath")
    public String getTempPath() {
        return tempPath;
    }
}
