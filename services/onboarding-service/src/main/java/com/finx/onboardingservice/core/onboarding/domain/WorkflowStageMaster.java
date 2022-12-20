package com.finx.onboardingservice.core.onboarding.domain;

import java.sql.Timestamp;

public class WorkflowStageMaster {

    private Integer wfStageId;
    private Integer wfDefId;
    private String stageName;
    private String stageDesc;
    private Integer orderStage;
    private String createdBy;
    private Timestamp createdOn;

    public WorkflowStageMaster() {
    }

    public WorkflowStageMaster(int wfStageId, int wfDefId, String stageName, String stageDesc,
                                int orderStage, String createdBy, Timestamp createdOn) {
        this.wfStageId = wfStageId;
        this.wfDefId = wfDefId;
        this.stageName = stageName;
        this.stageDesc = stageDesc;
        this.orderStage = orderStage;
        this.createdBy = createdBy;
        this.createdOn = createdOn;
    }

    public int getWfStageId() {
        return wfStageId;
    }

    public void setWfStageId(int wfStageId) {
        this.wfStageId = wfStageId;
    }

    public int getWfDefId() {
        return wfDefId;
    }

    public void setWfDefId(int wfDefId) {
        this.wfDefId = wfDefId;
    }

    public String getStageName() {
        return stageName;
    }

    public void setStageName(String stageName) {
        this.stageName = stageName;
    }

    public String getStageDesc() {
        return stageDesc;
    }

    public void setStageDesc(String stageDesc) {
        this.stageDesc = stageDesc;
    }

    public int getOrderStage() {
        return orderStage;
    }

    public void setOrderStage(int orderStage) {
        this.orderStage = orderStage;
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
        return "WorkflowStageMaster{"
                + "wfStageId=" + wfStageId
                + ", wfDefId=" + wfDefId
                + ", stageName='" + stageName + '\''
                + ", stageDesc='" + stageDesc + '\''
                + ", orderStage=" + orderStage
                + ", createdBy='" + createdBy + '\''
                + ", createdOn=" + createdOn
                + '}';
    }
}
