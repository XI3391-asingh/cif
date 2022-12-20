package com.cif.syncerservice.core.syncer.domain;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

public class ChangeConfig implements Serializable {

    private int changeConfigId;
    private String systemCode;

    private String systemType;
    private int systemId;
    private String[] configFields;
    private String apiDetails;

    private boolean isActive;

    @JsonIgnoreProperties
    private ApiDetails apiObject;


    public ChangeConfig() {
    }

    public ChangeConfig(int changeConfigId, String systemCode, String systemType, int systemId) {
        this.changeConfigId = changeConfigId;
        this.systemCode = systemCode;
        this.systemType = systemType;
        this.systemId = systemId;
        this.isActive = true;
    }

    @Override
    public int hashCode() {
        final int temp = 14;
        int ans = systemId;
        ans = temp * ans + changeConfigId;
        return ans;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        ChangeConfig other = (ChangeConfig) o;
        if (this.changeConfigId != other.changeConfigId) {
            return false;
        }
        return true;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String[] getConfigFields() {
        return configFields;
    }

    public void setConfigFields(String[] configFields) {
        this.configFields = configFields;
    }

    public String getApiDetails() {
        return apiDetails;
    }

    public void setApiDetails(String apiDetails) {
        this.apiDetails = apiDetails;
    }

    public ApiDetails getApiObject() {
        return apiObject;
    }

    public void setApiObject(ApiDetails apiObject) {
        this.apiObject = apiObject;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public int getChangeConfigId() {
        return changeConfigId;
    }

    public void setChangeConfigId(int changeConfigId) {
        this.changeConfigId = changeConfigId;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }
}
