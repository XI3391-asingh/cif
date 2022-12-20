package com.cif.cifservice.core.party.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class PartyDetailsView {

    private Long partyId;

    private String partyIdentifier;
    private String type;
    private String salutationCode;
    private String salutation;
    private String status;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String motherMaidenName;
    private String nickName;
    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfBirth;
    private String placeOfBirth;
    private String countryBirthCode;
    private String countryOfBirth;
    private String primaryMobileNumber;
    private String primaryEmail;
    private String maritalStatus;
    private String countryResidenceCode;
    private String countryResidence;
    private String residencyTypeCode;
    private String residencyType;
    private String educationTypeCode;
    private String educationType;
    private String professionCode;
    private String profession;
    private String professionTypeCode;
    private String professionType;
    private String jobPosition;

    private String jobPositionTypeCode;
    private String industryTypeCode;
    private String industryType;
    private String nationalityCode;
    private String nationality;
    private Double annualIncome;
    private Double annualTurnover;
    private String taxId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date dateOfIncorp;
    private String staffCode;
    private String companyCode;
    private String company;
    private String groupCode;
    private String portfolioCode;
    private String relationTypeCode;
    private String relationType;
    private String segmentTypeCode;
    private String segment;
    private String amlRisk;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date amlRiskEvalDate;
    private Boolean amlCheckStatus;
    private String refferalCode;
    private String promoCode;
    private String nationalIdTypeCode;
    private String nationalIdType;
    private String nationalId;

    private Boolean isStaff;

    private Boolean isDeceased;
    private Boolean isInsolvency;
    private Boolean isNpa;
    private Boolean isWillfulDefaulter;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date willfulDefaulterDate;
    private Boolean isLoanOverdue;
    private Boolean isSuitFailed;
    private Boolean isPep;
    private Boolean isFatcaApplicable;
    private Boolean isEmailStatementReg;
    private Boolean isUnderWatchlist;
    private Boolean isDeleted;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date createdAt;
    private String createdBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date updatedAt;
    private String updatedBy;
    private String createdByChannel;
    private String updatedByChannel;
    private Long partyAddressId;
    private String addressTypeCode;
    private String addressType;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3;
    private String zipCode;
    private Boolean isDefault;
    private String wardCode;
    private String ward;
    private String districtCode;
    private String district;
    private String cityCode;
    private String city;
    private String provinceCode;
    private String province;
    private String countryCode;
    private String countryAddress;
    private String documentId;
    private Long partyContactDetailsId;
    private String contactTypeCode;
    private String contactType;
    private String contactValue;

    private String isdCode;
    private Boolean isPrimary;
    private Boolean isVerified;
    private String verifiedMode;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date lastVerifiedDate;
    private Boolean isDnd;

    private Long partyAssetId;
    private String assetTypeCode;
    private String assetType;
    private String assetName;
    private Double potentialValue;
    private Boolean isMortgaged;

    private Long partyRiskId;
    private String riskTypeCode;
    private String riskType;
    private Float riskScore;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date evaluationDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date validUntil;

    private Long partyMemoId;
    private String memoTypeCode;
    private String memoType;
    private String severity;
    private Float score;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date validFrom;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date validTo;

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

    private Long partyRelationId;
    private String secondaryPartyId;
    private String partyRelationTypeCode;
    private String partyRelationType;
    private String invRelation;

    private Long partyAccountMappingId;
    private String accountId;
    private String relation;

    private Long partyXrefId;
    private String systemCode;
    private String xrefId;

    private Long partyFatcaDetailsId;
    private String placeOfIncorporation;
    private String countryOfIncorporation;
    private String countryOfResidence;
    private String incorporationNumber;
    private String boardRelNumber;
    private String reportBlNumber;
    private String originalReportBlNumber;
    private String fatcaTaxId;
    private String documentReferenceId;

    private Long partyGuardianId;
    private String guardianFirstName;
    private String guardianMiddleName;
    private String guardianLastName;
    private String guardianRelation;
    private String guardianAddressLine1;
    private String guardianAddressLine2;
    private String guardianAddressLine3;
    private String guardianWardCode;
    private String guardianWard;
    private String guardianDistrictCode;
    private String guardianDistrict;
    private String guardianCityCode;
    private String guardianCity;

    public Long getPartyId() {
        return partyId;
    }

    public void setPartyId(Long partyId) {
        this.partyId = partyId;
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

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
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

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getCountryBirthCode() {
        return countryBirthCode;
    }

    public void setCountryBirthCode(String countryBirthCode) {
        this.countryBirthCode = countryBirthCode;
    }

    public String getCountryOfBirth() {
        return countryOfBirth;
    }

    public void setCountryOfBirth(String countryOfBirth) {
        this.countryOfBirth = countryOfBirth;
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

    public String getCountryResidenceCode() {
        return countryResidenceCode;
    }

    public void setCountryResidenceCode(String countryResidenceCode) {
        this.countryResidenceCode = countryResidenceCode;
    }

    public String getCountryResidence() {
        return countryResidence;
    }

    public void setCountryResidence(String countryResidence) {
        this.countryResidence = countryResidence;
    }

    public String getResidencyTypeCode() {
        return residencyTypeCode;
    }

    public void setResidencyTypeCode(String residencyTypeCode) {
        this.residencyTypeCode = residencyTypeCode;
    }

    public String getResidencyType() {
        return residencyType;
    }

    public void setResidencyType(String residencyType) {
        this.residencyType = residencyType;
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

    public String getProfessionCode() {
        return professionCode;
    }

    public void setProfessionCode(String professionCode) {
        this.professionCode = professionCode;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getProfessionTypeCode() {
        return professionTypeCode;
    }

    public void setProfessionTypeCode(String professionTypeCode) {
        this.professionTypeCode = professionTypeCode;
    }

    public String getProfessionType() {
        return professionType;
    }

    public void setProfessionType(String professionType) {
        this.professionType = professionType;
    }

    public String getJobPosition() {
        return jobPosition;
    }

    public void setJobPosition(String jobPosition) {
        this.jobPosition = jobPosition;
    }

    public String getIndustryTypeCode() {
        return industryTypeCode;
    }

    public void setIndustryTypeCode(String industryTypeCode) {
        this.industryTypeCode = industryTypeCode;
    }

    public String getIndustryType() {
        return industryType;
    }

    public void setIndustryType(String industryType) {
        this.industryType = industryType;
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

    public Double getAnnualIncome() {
        return annualIncome;
    }

    public void setAnnualIncome(Double annualIncome) {
        this.annualIncome = annualIncome;
    }

    public Double getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(Double annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public Date getDateOfIncorp() {
        return dateOfIncorp;
    }

    public void setDateOfIncorp(Date dateOfIncorp) {
        this.dateOfIncorp = dateOfIncorp;
    }

    public String getStaffCode() {
        return staffCode;
    }

    public void setStaffCode(String staffCode) {
        this.staffCode = staffCode;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getSegmentTypeCode() {
        return segmentTypeCode;
    }

    public void setSegmentTypeCode(String segmentTypeCode) {
        this.segmentTypeCode = segmentTypeCode;
    }

    public String getSegment() {
        return segment;
    }

    public void setSegment(String segment) {
        this.segment = segment;
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

    public String getRefferalCode() {
        return refferalCode;
    }

    public void setRefferalCode(String refferalCode) {
        this.refferalCode = refferalCode;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
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

    public Date getAmlRiskEvalDate() {
        return amlRiskEvalDate;
    }

    public void setAmlRiskEvalDate(Date amlRiskEvalDate) {
        this.amlRiskEvalDate = amlRiskEvalDate;
    }

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
    }

    public Boolean getAmlCheckStatus() {
        return amlCheckStatus;
    }

    public void setAmlCheckStatus(Boolean amlCheckStatus) {
        this.amlCheckStatus = amlCheckStatus;
    }

    public Boolean getIsDeceased() {
        return isDeceased;
    }

    public void setIsDeceased(Boolean isDeceased) {
        this.isDeceased = isDeceased;
    }

    public Boolean getIsInsolvency() {
        return isInsolvency;
    }

    public void setIsInsolvency(Boolean isInsolvency) {
        this.isInsolvency = isInsolvency;
    }

    public Boolean getIsNpa() {
        return isNpa;
    }

    public void setIsNpa(Boolean isNpa) {
        this.isNpa = isNpa;
    }

    public Boolean getIsWillfulDefaulter() {
        return isWillfulDefaulter;
    }

    public void setIsWillfulDefaulter(Boolean isWillfulDefaulter) {
        this.isWillfulDefaulter = isWillfulDefaulter;
    }

    public Date getWillfulDefaulterDate() {
        return willfulDefaulterDate;
    }

    public void setWillfulDefaulterDate(Date willfulDefaulterDate) {
        this.willfulDefaulterDate = willfulDefaulterDate;
    }

    public Boolean getIsLoanOverdue() {
        return isLoanOverdue;
    }

    public void setIsLoanOverdue(Boolean isLoanOverdue) {
        this.isLoanOverdue = isLoanOverdue;
    }

    public Boolean getIsSuitFailed() {
        return isSuitFailed;
    }

    public void setIsSuitFailed(Boolean isSuitFailed) {
        this.isSuitFailed = isSuitFailed;
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

    public Boolean getIsEmailStatementReg() {
        return isEmailStatementReg;
    }

    public void setIsEmailStatementReg(Boolean isEmailStatementReg) {
        this.isEmailStatementReg = isEmailStatementReg;
    }

    public Boolean getIsUnderWatchlist() {
        return isUnderWatchlist;
    }

    public void setIsUnderWatchlist(Boolean isUnderWatchlist) {
        this.isUnderWatchlist = isUnderWatchlist;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
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

    public Long getPartyAddressId() {
        return partyAddressId;
    }

    public void setPartyAddressId(Long partyAddressId) {
        this.partyAddressId = partyAddressId;
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

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Boolean getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Boolean isDefault) {
        this.isDefault = isDefault;
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

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryAddress() {
        return countryAddress;
    }

    public void setCountryAddress(String countryAddress) {
        this.countryAddress = countryAddress;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public Long getPartyContactDetailsId() {
        return partyContactDetailsId;
    }

    public void setPartyContactDetailsId(Long partyContactDetailsId) {
        this.partyContactDetailsId = partyContactDetailsId;
    }

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

    public Long getPartyAssetId() {
        return partyAssetId;
    }

    public void setPartyAssetId(Long partyAssetId) {
        this.partyAssetId = partyAssetId;
    }

    public String getAssetTypeCode() {
        return assetTypeCode;
    }

    public void setAssetTypeCode(String assetTypeCode) {
        this.assetTypeCode = assetTypeCode;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Double getPotentialValue() {
        return potentialValue;
    }

    public void setPotentialValue(Double potentialValue) {
        this.potentialValue = potentialValue;
    }

    public Boolean getIsMortgaged() {
        return isMortgaged;
    }

    public void setIsMortgaged(Boolean isMortgaged) {
        this.isMortgaged = isMortgaged;
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

    public Float getRiskScore() {
        return riskScore;
    }

    public void setRiskScore(Float riskScore) {
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

    public Long getPartyRelationId() {
        return partyRelationId;
    }

    public void setPartyRelationId(Long partyRelationId) {
        this.partyRelationId = partyRelationId;
    }

    public String getSecondaryPartyId() {
        return secondaryPartyId;
    }

    public void setSecondaryPartyId(String secondaryPartyId) {
        this.secondaryPartyId = secondaryPartyId;
    }

    public String getPartyRelationTypeCode() {
        return partyRelationTypeCode;
    }

    public void setPartyRelationTypeCode(String partyRelationTypeCode) {
        this.partyRelationTypeCode = partyRelationTypeCode;
    }

    public String getPartyRelationType() {
        return partyRelationType;
    }

    public void setPartyRelationType(String partyRelationType) {
        this.partyRelationType = partyRelationType;
    }

    public String getInvRelation() {
        return invRelation;
    }

    public void setInvRelation(String invRelation) {
        this.invRelation = invRelation;
    }

    public Long getPartyAccountMappingId() {
        return partyAccountMappingId;
    }

    public void setPartyAccountMappingId(Long partyAccountMappingId) {
        this.partyAccountMappingId = partyAccountMappingId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public Long getPartyXrefId() {
        return partyXrefId;
    }

    public void setPartyXrefId(Long partyXrefId) {
        this.partyXrefId = partyXrefId;
    }

    public String getSystemCode() {
        return systemCode;
    }

    public void setSystemCode(String systemCode) {
        this.systemCode = systemCode;
    }

    public String getXrefId() {
        return xrefId;
    }

    public void setXrefId(String xrefId) {
        this.xrefId = xrefId;
    }

    public Long getPartyFatcaDetailsId() {
        return partyFatcaDetailsId;
    }

    public void setPartyFatcaDetailsId(Long partyFatcaDetailsId) {
        this.partyFatcaDetailsId = partyFatcaDetailsId;
    }

    public String getPlaceOfIncorporation() {
        return placeOfIncorporation;
    }

    public void setPlaceOfIncorporation(String placeOfIncorporation) {
        this.placeOfIncorporation = placeOfIncorporation;
    }

    public String getCountryOfIncorporation() {
        return countryOfIncorporation;
    }

    public void setCountryOfIncorporation(String countryOfIncorporation) {
        this.countryOfIncorporation = countryOfIncorporation;
    }

    public String getCountryOfResidence() {
        return countryOfResidence;
    }

    public void setCountryOfResidence(String countryOfResidence) {
        this.countryOfResidence = countryOfResidence;
    }

    public String getIncorporationNumber() {
        return incorporationNumber;
    }

    public void setIncorporationNumber(String incorporationNumber) {
        this.incorporationNumber = incorporationNumber;
    }

    public String getBoardRelNumber() {
        return boardRelNumber;
    }

    public void setBoardRelNumber(String boardRelNumber) {
        this.boardRelNumber = boardRelNumber;
    }

    public String getReportBlNumber() {
        return reportBlNumber;
    }

    public void setReportBlNumber(String reportBlNumber) {
        this.reportBlNumber = reportBlNumber;
    }

    public String getOriginalReportBlNumber() {
        return originalReportBlNumber;
    }

    public void setOriginalReportBlNumber(String originalReportBlNumber) {
        this.originalReportBlNumber = originalReportBlNumber;
    }

    public String getFatcaTaxId() {
        return fatcaTaxId;
    }

    public void setFatcaTaxId(String fatcaTaxId) {
        this.fatcaTaxId = fatcaTaxId;
    }

    public String getDocumentReferenceId() {
        return documentReferenceId;
    }

    public void setDocumentReferenceId(String documentReferenceId) {
        this.documentReferenceId = documentReferenceId;
    }

    public Long getPartyGuardianId() {
        return partyGuardianId;
    }

    public void setPartyGuardianId(Long partyGuardianId) {
        this.partyGuardianId = partyGuardianId;
    }

    public String getGuardianFirstName() {
        return guardianFirstName;
    }

    public void setGuardianFirstName(String guardianFirstName) {
        this.guardianFirstName = guardianFirstName;
    }

    public String getGuardianMiddleName() {
        return guardianMiddleName;
    }

    public void setGuardianMiddleName(String guardianMiddleName) {
        this.guardianMiddleName = guardianMiddleName;
    }

    public String getGuardianLastName() {
        return guardianLastName;
    }

    public void setGuardianLastName(String guardianLastName) {
        this.guardianLastName = guardianLastName;
    }

    public String getGuardianAddressLine1() {
        return guardianAddressLine1;
    }

    public void setGuardianAddressLine1(String guardianAddressLine1) {
        this.guardianAddressLine1 = guardianAddressLine1;
    }

    public String getGuardianAddressLine2() {
        return guardianAddressLine2;
    }

    public void setGuardianAddressLine2(String guardianAddressLine2) {
        this.guardianAddressLine2 = guardianAddressLine2;
    }

    public String getGuardianAddressLine3() {
        return guardianAddressLine3;
    }

    public void setGuardianAddressLine3(String guardianAddressLine3) {
        this.guardianAddressLine3 = guardianAddressLine3;
    }

    public String getGuardianWardCode() {
        return guardianWardCode;
    }

    public void setGuardianWardCode(String guardianWardCode) {
        this.guardianWardCode = guardianWardCode;
    }

    public String getGuardianWard() {
        return guardianWard;
    }

    public void setGuardianWard(String guardianWard) {
        this.guardianWard = guardianWard;
    }

    public String getGuardianDistrictCode() {
        return guardianDistrictCode;
    }

    public void setGuardianDistrictCode(String guardianDistrictCode) {
        this.guardianDistrictCode = guardianDistrictCode;
    }

    public String getGuardianDistrict() {
        return guardianDistrict;
    }

    public void setGuardianDistrict(String guardianDistrict) {
        this.guardianDistrict = guardianDistrict;
    }

    public String getGuardianCityCode() {
        return guardianCityCode;
    }

    public void setGuardianCityCode(String guardianCityCode) {
        this.guardianCityCode = guardianCityCode;
    }

    public String getGuardianCity() {
        return guardianCity;
    }

    public void setGuardianCity(String guardianCity) {
        this.guardianCity = guardianCity;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getIsdCode() {
        return isdCode;
    }

    public void setIsdCode(String isdCode) {
        this.isdCode = isdCode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJobPositionTypeCode() {
        return jobPositionTypeCode;
    }

    public void setJobPositionTypeCode(String jobPositionTypeCode) {
        this.jobPositionTypeCode = jobPositionTypeCode;
    }


    public String getAmlRisk() {
        return amlRisk;
    }

    public void setAmlRisk(String amlRisk) {
        this.amlRisk = amlRisk;
    }

    public String getPartyIdentifier() {
        return partyIdentifier;
    }

    public void setPartyIdentifier(String partyIdentifier) {
        this.partyIdentifier = partyIdentifier;
    }

    public String getGuardianRelation() {
        return guardianRelation;
    }

    public void setGuardianRelation(String guardianRelation) {
        this.guardianRelation = guardianRelation;
    }
}

