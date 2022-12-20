package com.cif.cifservice.core.party.domain;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.util.Date;



public class PartyContactDetails {
    private String contactTypeCode;
    private String contactType;
    private String contactValue;
    private Boolean isPrimary;
    private Boolean isVerified;
    private String verifiedMode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date lastVerifiedDate;
    private Boolean isDnd;
    private Integer partyContactDetailsId;

    public String getContactTypeCode() {
        return contactTypeCode;
    }
    public void setContactTypeCode(String contactTypeCode) {
        this.contactTypeCode = contactTypeCode;
    }
    public String getContactType() {
        return contactType;
    }
    public void setContactType(String contactType) {
        this.contactType = contactType;
    }
    public String getContactValue() {
        return contactValue;
    }
    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }
    public Boolean getIsPrimary() {
        return isPrimary;
    }
    public void setIsPrimary(Boolean isPrimary) {
        this.isPrimary = isPrimary;
    }
    public Boolean getIsVerified() {
        return isVerified;
    }
    public void setIsVerified(Boolean isVerified) {
        this.isVerified = isVerified;
    }
    public String getVerifiedMode() {
        return verifiedMode;
    }
    public void setVerifiedMode(String verifiedMode) {
        this.verifiedMode = verifiedMode;
    }
    public Date getLastVerifiedDate() {
        return lastVerifiedDate;
    }
    public void setLastVerifiedDate(Date lastVerifiedDate) {
        this.lastVerifiedDate = lastVerifiedDate;
    }
    public Boolean getIsDnd() {
        return isDnd;
    }
    public void setIsDnd(Boolean isDnd) {
        this.isDnd = isDnd;
    }
    public Integer getPartyContactDetailsId() {
        return partyContactDetailsId;
    }
    public void setPartyContactDetailsId(Integer partyContactDetailsId) {
        this.partyContactDetailsId = partyContactDetailsId;
    }


}
