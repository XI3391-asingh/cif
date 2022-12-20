package com.cif.syncerservice.core.syncer.domain;

public class AdapterConfig {
    private int adapterConfigId;
    private int sourceSystemId;
    private int integrationSystemId;
    private String sourceSystemCode;
    private String integrationSystemCode;
    private boolean isCreate;
    private boolean isUpdate;

    public String getSourceSystemCode() {
        return sourceSystemCode;
    }

    public void setSourceSystemCode(String sourceSystemCode) {
        this.sourceSystemCode = sourceSystemCode;
    }

    public String getIntegrationSystemCode() {
        return integrationSystemCode;
    }

    public void setIntegrationSystemCode(String integrationSystemCode) {
        this.integrationSystemCode = integrationSystemCode;
    }

    public boolean getIsCreate() {
        return isCreate;
    }

    public void setIsCreate(boolean create) {
        isCreate = create;
    }

    public boolean getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(boolean update) {
        isUpdate = update;
    }

    public void setAdapterConfigId(int adapterConfigId) {
        this.adapterConfigId = adapterConfigId;
    }

    public int getAdapterConfigId() {
        return adapterConfigId;
    }

    public int getSourceSystemId() {
        return sourceSystemId;
    }

    public void setSourceSystemId(int sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
    }

    public int getIntegrationSystemId() {
        return integrationSystemId;
    }

    public void setIntegrationSystemId(int integrationSystemId) {
        this.integrationSystemId = integrationSystemId;
    }
}
