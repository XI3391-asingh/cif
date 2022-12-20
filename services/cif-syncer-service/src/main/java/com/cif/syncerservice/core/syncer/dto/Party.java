package com.cif.syncerservice.core.syncer.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.time.LocalDate;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Party {

    private String partyIdentifier;
    private String partyType;
    private String salutationCode;
    private String salutation;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String mothersMaidenName;
    private String nickName;
    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String placeOfBirth;
    private String primaryMobileNumber;
    private String primaryEmail;
    private String maritalStatus;
    private String partyStatus;
    private String status;
    private String nationalIdTypeCode;
    private String nationalIdType;
    private String nationalId;
    private String referralCode;
    private String promoCode;
    private String relationTypeCode;
    private String relationType;
    private String partySegmentCode;
    private String partySegment;
    private String countryOfBirthCode;
    private String countryOfBirth;
    private String nationalityCode;
    private String nationality;
    private String educationTypeCode;
    private String educationType;
    private Boolean isStaff;
    private String staffCode;
    private String groupCode;
    private String portfolioCode;
    private Integer countryOfResidenceCode;
    private String countryResidence;
    private Integer residencyTypeCode;
    private String residencyType;
    private String jobPosition;
    private String amlRisk;

    private String xRefIf;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate amlRiskEvalDate;
    private boolean amlCheckStatus;
    private String createdByChannel;
    private String updatedByChannel;

    private String sourceSystem;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy'T'HH:mm:ss")
    private LocalDateTime createdAt;

    public String getPartyIdentifier() {
        return partyIdentifier;
    }

    public void setPartyIdentifier(String partyIdentifier) {
        this.partyIdentifier = partyIdentifier;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
    }

    public String getSalutationCode() {
        return salutationCode;
    }

    public void setSalutationCode(String salutationCode) {
        this.salutationCode = salutationCode;
    }

    public String getSalutation() {
        return salutation;
    }

    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMothersMaidenName() {
        return mothersMaidenName;
    }

    public void setMothersMaidenName(String mothersMaidenName) {
        this.mothersMaidenName = mothersMaidenName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getPrimaryMobileNumber() {
        return primaryMobileNumber;
    }

    public void setPrimaryMobileNumber(String primaryMobileNumber) {
        this.primaryMobileNumber = primaryMobileNumber;
    }

    public String getPrimaryEmail() {
        return primaryEmail;
    }

    public void setPrimaryEmail(String primaryEmail) {
        this.primaryEmail = primaryEmail;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getPartyStatus() {
        return partyStatus;
    }

    public void setPartyStatus(String partyStatus) {
        this.partyStatus = partyStatus;
    }

    public String getNationalIdTypeCode() {
        return nationalIdTypeCode;
    }

    public void setNationalIdTypeCode(String nationalIdTypeCode) {
        this.nationalIdTypeCode = nationalIdTypeCode;
    }

    public String getNationalIdType() {
        return nationalIdType;
    }

    public void setNationalIdType(String nationalIdType) {
        this.nationalIdType = nationalIdType;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getRelationTypeCode() {
        return relationTypeCode;
    }

    public void setRelationTypeCode(String relationTypeCode) {
        this.relationTypeCode = relationTypeCode;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getPartySegmentCode() {
        return partySegmentCode;
    }

    public void setPartySegmentCode(String partySegmentCode) {
        this.partySegmentCode = partySegmentCode;
    }

    public String getPartySegment() {
        return partySegment;
    }

    public void setPartySegment(String partySegment) {
        this.partySegment = partySegment;
    }

    public String getCountryOfBirthCode() {
        return countryOfBirthCode;
    }

    public void setCountryOfBirthCode(String countryOfBirthCode) {
        this.countryOfBirthCode = countryOfBirthCode;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getEducationTypeCode() {
        return educationTypeCode;
    }

    public void setEducationTypeCode(String educationTypeCode) {
        this.educationTypeCode = educationTypeCode;
    }

    public String getEducationType() {
        return educationType;
    }

    public void setEducationType(String educationType) {
        this.educationType = educationType;
    }

    public Boolean getStaff() {
        return isStaff;
    }

    public void setStaff(Boolean staff) {
        isStaff = staff;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getPortfolioCode() {
        return portfolioCode;
    }

    public void setPortfolioCode(String portfolioCode) {
        this.portfolioCode = portfolioCode;
    }

    public Integer getCountryOfResidenceCode() {
        return countryOfResidenceCode;
    }

    public void setCountryOfResidenceCode(Integer countryOfResidenceCode) {
        this.countryOfResidenceCode = countryOfResidenceCode;
    }

    public String getCountryResidence() {
        return countryResidence;
    }

    public void setCountryResidence(String countryResidence) {
        this.countryResidence = countryResidence;
    }

    public Integer getResidencyTypeCode() {
        return residencyTypeCode;
    }

    public void setResidencyTypeCode(Integer residencyTypeCode) {
        this.residencyTypeCode = residencyTypeCode;
    }

    public String getResidencyType() {
        return residencyType;
    }

    public void setResidencyType(String residencyType) {
        this.residencyType = residencyType;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getCreatedByChannel() {
        return createdByChannel;
    }

    public void setCreatedByChannel(String createdByChannel) {
        this.createdByChannel = createdByChannel;
    }

    public String getUpdatedByChannel() {
        return updatedByChannel;
    }

    public void setUpdatedByChannel(String updatedByChannel) {
        this.updatedByChannel = updatedByChannel;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAmlRisk() {
        return amlRisk;
    }

    public void setAmlRisk(String amlRisk) {
        this.amlRisk = amlRisk;
    }

    public LocalDate getAmlRiskEvalDate() {
        return amlRiskEvalDate;
    }

    public void setAmlRiskEvalDate(LocalDate amlRiskEvalDate) {
        this.amlRiskEvalDate = amlRiskEvalDate;
    }

    public boolean isAmlCheckStatus() {
        return amlCheckStatus;
    }

    public void setAmlCheckStatus(boolean amlCheckStatus) {
        this.amlCheckStatus = amlCheckStatus;
    }

    public String getxRefIf() {
        return xRefIf;
    }

    public void setxRefIf(String xRefIf) {
        this.xRefIf = xRefIf;
    }
}
