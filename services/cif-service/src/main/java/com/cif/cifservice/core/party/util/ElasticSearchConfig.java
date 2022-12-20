package com.cif.cifservice.core.party.util;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public final class ElasticSearchConfig {
    @NotNull
    @Valid
    private String host;

    @NotNull
    @Valid
    private Integer port;

    @NotNull
    @Valid
    private String partyIndex;


    @NotNull
    @Valid
    private String activityPostingIndex;

    @NotNull
    @Valid
    private String adminPostingIndex;
    @JsonProperty("host")
    public String getHost() {
        return host;
    }

    @JsonProperty("port")
    public Integer getPort() {
        return port;
    }

    @JsonProperty("party_index")
    public String getPartyIndex() {
        return partyIndex;
    }

    @JsonProperty("activity_posting_index")
    public String getActivityPostingIndex() {
        return activityPostingIndex;
    }

    @JsonProperty("admin_cif_index")
    public String getAdminPostingIndex() {
        return adminPostingIndex;
    }
}
