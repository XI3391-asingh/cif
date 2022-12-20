package com.cif.syncerservice.core.syncer.domain;

public class ApiDetails {

    private String baseUrl;
    private ApiAttributes create;
    private ApiAttributes update;

    public ApiDetails() {
    }

    public ApiDetails(String baseUrl, ApiAttributes create, ApiAttributes update) {
        this.baseUrl = baseUrl;
        this.create = create;
        this.update = update;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public ApiAttributes getCreate() {
        return create;
    }

    public ApiAttributes getUpdate() {
        return update;
    }
}
