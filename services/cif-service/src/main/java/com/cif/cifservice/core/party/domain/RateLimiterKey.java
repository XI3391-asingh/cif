package com.cif.cifservice.core.party.domain;

import java.io.Serializable;

public class RateLimiterKey implements Serializable {
    private String userId;
    private String uri;

    public RateLimiterKey(String userId, String uri) {
        this.userId = userId;
        this.uri = uri;
    }

    public RateLimiterKey() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return "RateLimiterKey{" + "userId='" + userId + '\'' + ", uri='" + uri + '\'' + '}';
    }
}
