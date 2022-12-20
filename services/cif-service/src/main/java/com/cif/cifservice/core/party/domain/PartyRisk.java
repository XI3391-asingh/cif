package com.cif.cifservice.core.party.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class PartyRisk {
    private Long partyRiskId;
    private String riskTypeCode;
    private String riskType;
    private Integer riskScore;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date evaluationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date validUntil;

    public PartyRisk(Long partyRiskId, String riskTypeCode, String riskType, Integer riskScore, Date evaluationDate, Date validUntil) {
        this.partyRiskId = partyRiskId;
        this.riskTypeCode = riskTypeCode;
        this.riskType = riskType;
        this.riskScore = riskScore;
        this.evaluationDate = evaluationDate;
        this.validUntil = validUntil;
    }

    public Long getPartyRiskId() {
        return partyRiskId;
    }

    public void setPartyRiskId(Long partyRiskId) {
        this.partyRiskId = partyRiskId;
    }

    public String getRiskTypeCode() {
        return riskTypeCode;
    }

    public void setRiskTypeCode(String riskTypeCode) {
        this.riskTypeCode = riskTypeCode;
    }

    public String getRiskType() {
        return riskType;
    }

    public void setRiskType(String riskType) {
        this.riskType = riskType;
    }

    public Integer getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Integer riskScore) {
        this.riskScore = riskScore;
    }

    public Date getEvaluationDate() {
        return evaluationDate;
    }

    public void setEvaluationDate(Date evaluationDate) {
        this.evaluationDate = evaluationDate;
    }

    public Date getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(Date validUntil) {
        this.validUntil = validUntil;
    }

    public PartyRisk() {
    }


}
