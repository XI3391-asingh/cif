package com.cif.cifservice.core.party.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class PartyMemo {

    private Long partyMemoId;
    private String memoTypeCode;
    private String memoType;
    private String severity;
    private Float score;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date validFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date validTo;

    public PartyMemo(Long partyMemoId, String memoTypeCode, String memoType, String severity, Float score, Date validFrom, Date validTo) {
        this.partyMemoId = partyMemoId;
        this.memoTypeCode = memoTypeCode;
        this.memoType = memoType;
        this.severity = severity;
        this.score = score;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public Long getPartyMemoId() {
        return partyMemoId;
    }

    public void setPartyMemoId(Long partyMemoId) {
        this.partyMemoId = partyMemoId;
    }

    public String getMemoTypeCode() {
        return memoTypeCode;
    }

    public void setMemoTypeCode(String memoTypeCode) {
        this.memoTypeCode = memoTypeCode;
    }

    public String getMemoType() {
        return memoType;
    }

    public void setMemoType(String memoType) {
        this.memoType = memoType;
    }

    public String getSeverity() {
        return severity;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public Float getScore() {
        return score;
    }

    public void setScore(Float score) {
        this.score = score;
    }

    public Date getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(Date validFrom) {
        this.validFrom = validFrom;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public PartyMemo() {
    }

}
