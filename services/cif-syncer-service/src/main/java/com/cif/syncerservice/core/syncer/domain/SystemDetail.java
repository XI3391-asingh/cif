package com.cif.syncerservice.core.syncer.domain;

public class SystemDetail {
    private int systemId;
    private String systemCode;
    private String systemType;
    private String description;

    public SystemDetail() {
    }

    public SystemDetail(String systemCode, String systemType, String description) {
        this.systemCode = systemCode;
        this.systemType = systemType;
        this.description = description;
    }

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getSystemType() {
        return systemType;
    }

    public void setSystemType(String systemType) {
        this.systemType = systemType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
