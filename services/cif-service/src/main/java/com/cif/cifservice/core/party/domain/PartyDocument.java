package com.cif.cifservice.core.party.domain;

import com.fasterxml.jackson.annotation.JsonFormat;


import java.util.Date;


public class PartyDocument {
    private Long partyDocumentId;
    private String documentTypeCode;
    private String documentType;
    private String documentNumber;
    private String documentNumberMasked;
    private String documentNumberToken;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date issuingDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date expiryDate;
    private String issuingPlace;
    private String issuingCountryCode;
    private String issuingCountry;
    private Boolean isPoi;
    private Boolean isPoa;
    private String dmsReferenceId;
    private String verificationStatus;
    private String additionalData;

    public Long getPartyDocumentId() {
        return partyDocumentId;
    }
    public void setPartyDocumentId(Long partyDocumentId) {
        this.partyDocumentId = partyDocumentId;
    }
    public String getDocumentTypeCode() {
        return documentTypeCode;
    }
    public void setDocumentTypeCode(String documentTypeCode) {
        this.documentTypeCode = documentTypeCode;
    }
    public String getDocumentType() {
        return documentType;
    }
    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }
    public String getDocumentNumber() {
        return documentNumber;
    }
    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }
    public String getDocumentNumberMasked() {
        return documentNumberMasked;
    }
    public void setDocumentNumberMasked(String documentNumberMasked) {
        this.documentNumberMasked = documentNumberMasked;
    }
    public String getDocumentNumberToken() {
        return documentNumberToken;
    }
    public void setDocumentNumberToken(String documentNumberToken) {
        this.documentNumberToken = documentNumberToken;
    }
    public Date getIssuingDate() {
        return issuingDate;
    }
    public void setIssuingDate(Date issuingDate) {
        this.issuingDate = issuingDate;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    public String getIssuingPlace() {
        return issuingPlace;
    }
    public void setIssuingPlace(String issuingPlace) {
        this.issuingPlace = issuingPlace;
    }
    public String getIssuingCountryCode() {
        return issuingCountryCode;
    }
    public void setIssuingCountryCode(String issuingCountryCode) {
        this.issuingCountryCode = issuingCountryCode;
    }
    public String getIssuingCountry() {
        return issuingCountry;
    }
    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }
    public Boolean getIsPoi() {
        return isPoi;
    }
    public void setIsPoi(Boolean isPoi) {
        this.isPoi = isPoi;
    }
    public Boolean getIsPoa() {
        return isPoa;
    }
    public void setIsPoa(Boolean isPoa) {
        this.isPoa = isPoa;
    }
    public String getDmsReferenceId() {
        return dmsReferenceId;
    }
    public void setDmsReferenceId(String dmsReferenceId) {
        this.dmsReferenceId = dmsReferenceId;
    }
    public String getVerificationStatus() {
        return verificationStatus;
    }
    public void setVerificationStatus(String verificationStatus) {
        this.verificationStatus = verificationStatus;
    }
    public String getAdditionalData() {
        return additionalData;
    }
    public void setAdditionalData(String additionalData) {
        this.additionalData = additionalData;
    }


}
