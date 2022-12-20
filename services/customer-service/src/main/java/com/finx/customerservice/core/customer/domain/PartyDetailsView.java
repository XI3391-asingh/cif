package com.finx.customerservice.core.customer.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class PartyDetailsView {

    private Long partyId;
    private String partyType;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private String maritalStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate dateOfBirth;
    private String primaryMobileNumber;
    private String primaryEmail;
    private String status;
    private String nationalIdTypeCode;
    private String nationalIdType;
    private String nationalId;
    private String nationalityCode;
    private String nationality;
    private String countryOfResidenceCode;
    private String countryOfResidence;
    private String jobPositionTypeCode;
    private String jobPosition;
    private String occupationTypeCode;
    private String occupation;
    private String segmentCode;
    private String segment;
    private String taxId;
    private String amlRisk;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate amlRiskEvalDate;
    private Boolean isAmlStatus;
    private Boolean isPep;
    private Boolean isFatcaApplicable;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate createdAt;
    private String createdBy;
    private String updatedAt;
    private String createdByChannel;
    private String updatedByChannel;
    private String addressTypeCode;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String cityCode;
    private String city;
    private String wardCode;
    private String ward;
    private String districtCode;
    private String district;
    private Integer zipCode;
    private String countryCode;
    private String country;
    private Boolean isDefault;
    private Long partyAddressId;

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
    }

    public String getPartyType() {
        return partyType;
    }

    public void setPartyType(String partyType) {
        this.partyType = partyType;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
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

    public String getCountryOfResidenceCode() {
        return countryOfResidenceCode;
    }

    public void setCountryOfResidenceCode(String countryOfResidenceCode) {
        this.countryOfResidenceCode = countryOfResidenceCode;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getJobPositionTypeCode() {
        return jobPositionTypeCode;
    }

    public void setJobPositionTypeCode(String jobPositionTypeCode) {
        this.jobPositionTypeCode = jobPositionTypeCode;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getOccupationTypeCode() {
        return occupationTypeCode;
    }

    public void setOccupationTypeCode(String occupationTypeCode) {
        this.occupationTypeCode = occupationTypeCode;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getSegmentCode() {
        return segmentCode;
    }

    public void setSegmentCode(String segmentCode) {
        this.segmentCode = segmentCode;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
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

    public Boolean getIsAmlStatus() {
        return isAmlStatus;
    }

    public void setIsAmlStatus(Boolean isAmlStatus) {
        this.isAmlStatus = isAmlStatus;
    }

    public Boolean getIsPep() {
        return isPep;
    }

    public void setIsPep(Boolean isPep) {
        this.isPep = isPep;
    }

    public Boolean getIsFatcaApplicable() {
        return isFatcaApplicable;
    }

    public void setIsFatcaApplicable(Boolean isFatcaApplicable) {
        this.isFatcaApplicable = isFatcaApplicable;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
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

    public String getAddressTypeCode() {
        return addressTypeCode;
    }

    public void setAddressTypeCode(String addressTypeCode) {
        this.addressTypeCode = addressTypeCode;
    }

    public String getAddressType() {
        return addressType;
    }

    public void setAddressType(String addressType) {
        this.addressType = addressType;
    }

    public String getAddressLine1() {
        return addressLine1;
    }

    public void setAddressLine1(String addressLine1) {
        this.addressLine1 = addressLine1;
    }

    public String getAddressLine2() {
        return addressLine2;
    }

    public void setAddressLine2(String addressLine2) {
        this.addressLine2 = addressLine2;
    }

    public String getAddressLine3() {
        return addressLine3;
    }

    public void setAddressLine3(String addressLine3) {
        this.addressLine3 = addressLine3;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWardCode() {
        return wardCode;
    }

    public void setWardCode(String wardCode) {
        this.wardCode = wardCode;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Integer getZipCode() {
        return zipCode;
    }

    public void setZipCode(Integer zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
    }

    public Long getPartyAddressId() {
        return partyAddressId;
    }

    public void setPartyAddressId(Long partyAddressId) {
        this.partyAddressId = partyAddressId;
    }
}