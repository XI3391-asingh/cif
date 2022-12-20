package com.finx.onboardingservice.core.onboarding.domain;

import java.sql.Timestamp;

public class WorkflowOnboarding {

    private Integer wfId;
    private String mobileNumber;
    private Integer wfDefId;
    private Integer wfStageId;
    private String stageStatus;
    private String reason;
    private String createdBy;
    private Timestamp createdOn;

    public Integer getWfId() {
        return wfId;
    }

    public void setWfId(Integer wfId) {
        this.wfId = wfId;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public Integer getWfDefId() {
        return wfDefId;
    }

    public void setWfDefId(Integer wfDefId) {
        this.wfDefId = wfDefId;
    }

    public Integer getWfStageId() {
        return wfStageId;
    }

    public void setWfStageId(Integer wfStageId) {
        this.wfStageId = wfStageId;
    }

    public String getStageStatus() {
        return stageStatus;
    }

    public void setStageStatus(String stageStatus) {
        this.stageStatus = stageStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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
}
