package com.finx.onboardingservice.core.onboarding.domain;

import java.sql.Timestamp;

public class WorkflowMaster {

    private Integer wfDefId;
    private String wfName;
    private String wfDesc;
    private String createdBy;
    private Timestamp createdOn;

    public WorkflowMaster() {
    }

    public WorkflowMaster(int wfDefId, String wfName, String wfDesc, String createdBy, Timestamp createdOn) {
        this.wfDefId = wfDefId;
        this.wfName = wfName;
        this.wfDesc = wfDesc;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }

    public int getWfDefId() {
        return wfDefId;
    }

    public void setWfDefId(int wfDefId) {
        this.wfDefId = wfDefId;
    }

    public String getWfName() {
        return wfName;
    }

    public void setWfName(String wfName) {
        this.wfName = wfName;
    }

    public String getWfDesc() {
        return wfDesc;
    }

    public void setWfDesc(String wfDesc) {
        this.wfDesc = wfDesc;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "WorkflowMaster{"
                + "wfDefId=" + wfDefId
                + ", wfName='" + wfName + '\''
                + ", wfDesc='" + wfDesc + '\''
                + ", createdBy='" + createdBy + '\''
                + ", createdOn=" + createdOn
                + '}';
    }
}
