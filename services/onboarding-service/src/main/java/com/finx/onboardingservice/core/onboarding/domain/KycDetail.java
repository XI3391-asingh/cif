package com.finx.onboardingservice.core.onboarding.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class KycDetail {

  private Long id;
  private Long prospectId;
  private String mobileNumber;
  private String mobileNumberHashcode;
  private String prospectFullName;
  private LocalDate dateOfBirth;
  private String kycSystem;
  private String externalRequestId;
  private String kycType;
  private String kycStatus;
  private String documentType;
  private String documentIdNumber;
  private String originalKycDetails;
  private String kycDetails;
  private Boolean isManualVerificationNeeded;
  private String manualVerificationNeededReason;
  private Boolean isKycDataModified;
  private String createdBy;
  private LocalDateTime createdDate;
  private String lastUpdatedBy;
  private LocalDateTime lastUpdatedDate;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getProspectId() {
    return prospectId;
  }

  public void setProspectId(Long prospectId) {
    this.prospectId = prospectId;
  }

  public String getMobileNumberHashcode() {
    return mobileNumberHashcode;
  }

  public String getMobileNumber() {
    return mobileNumber;
  }

  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }

  public String getProspectFullName() {
    return prospectFullName;
  }

  public void setProspectFullName(String prospectFullName) {
    this.prospectFullName = prospectFullName;
  }

  public void setMobileNumberHashcode(String mobileNumberHashcode) {
    this.mobileNumberHashcode = mobileNumberHashcode;
  }

  public LocalDate getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(LocalDate dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getKycSystem() {
    return kycSystem;
  }

  public void setKycSystem(String kycSystem) {
    this.kycSystem = kycSystem;
  }

  public String getExternalRequestId() {
    return externalRequestId;
  }

  public void setExternalRequestId(String externalRequestId) {
    this.externalRequestId = externalRequestId;
  }

  public String getKycType() {
    return kycType;
  }

  public void setKycType(String kycType) {
    this.kycType = kycType;
  }

  public String getKycStatus() {
    return kycStatus;
  }

  public void setKycStatus(String kycStatus) {
    this.kycStatus = kycStatus;
  }

  public String getDocumentType() {
    return documentType;
  }

  public void setDocumentType(String documentType) {
    this.documentType = documentType;
  }

  public String getDocumentIdNumber() {
    return documentIdNumber;
  }

  public void setDocumentIdNumber(String documentIdNumber) {
    this.documentIdNumber = documentIdNumber;
  }

  public String getOriginalKycDetails() {
    return originalKycDetails;
  }

  public void setOriginalKycDetails(String originalKycDetails) {
    this.originalKycDetails = originalKycDetails;
  }

  public String getKycDetails() {
    return kycDetails;
  }

  public void setKycDetails(String kycDetails) {
    this.kycDetails = kycDetails;
  }

  public Boolean getManualVerificationNeeded() {
    return isManualVerificationNeeded;
  }

  public void setManualVerificationNeeded(Boolean manualVerificationNeeded) {
    isManualVerificationNeeded = manualVerificationNeeded;
  }

  public String getManualVerificationNeededReason() {
    return manualVerificationNeededReason;
  }

  public void setManualVerificationNeededReason(String manualVerificationNeededReason) {
    this.manualVerificationNeededReason = manualVerificationNeededReason;
  }

  public Boolean getKycDataModified() {
    return isKycDataModified;
  }

  public void setKycDataModified(Boolean kycDataModified) {
    isKycDataModified = kycDataModified;
  }

  public String getCreatedBy() {
    return createdBy;
  }

  public void setCreatedBy(String createdBy) {
    this.createdBy = createdBy;
  }

  public LocalDateTime getCreatedDate() {
    return createdDate;
  }

  public void setCreatedDate(LocalDateTime createdDate) {
    this.createdDate = createdDate;
  }

  public String getLastUpdatedBy() {
    return lastUpdatedBy;
  }

  public void setLastUpdatedBy(String lastUpdatedBy) {
    this.lastUpdatedBy = lastUpdatedBy;
  }

  public LocalDateTime getLastUpdatedDate() {
    return lastUpdatedDate;
  }

  public void setLastUpdatedDate(LocalDateTime lastUpdatedDate) {
    this.lastUpdatedDate = lastUpdatedDate;
  }
}
