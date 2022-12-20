package com.cif.syncerservice.core.syncer.domain;

public class ApiAttributes {

    private String endPoint;
    private String authToken;

    public ApiAttributes() {
    }

    public ApiAttributes(String endPoint, String authToken) {
        this.endPoint = endPoint;
        this.authToken = authToken;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public String getAuthToken() {
        return authToken;
    }
}
