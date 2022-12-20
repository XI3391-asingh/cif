package com.cif.cifservice.core.party.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;


public class Party {
    private Long partyId;

    private String partyIdentifier;

    private String type;

    private String salutationCode;
    private String salutation;

    private String fullName;

    private String firstName;
    private String middleName;
    private String lastName;
    private String motherMaidenName;
    private String nickName;

    private String gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date dateOfBirth;
    private String placeOfBirth;

    private String countryBirthCode;

    private String primaryMobileNumber;

    private String primaryEmail;

    private String maritalStatus;

    private String status;

    private String countryResidenceCode;

    private String professionCode;
    private String professionTypeCode;
    private String industryTypeCode;
    private String companyTypeCode;
    private Integer annualIncome;
    private Integer annualTurnover;
    private String taxId;
    private String dateOfInCorp;

    private String companyCode;
    private String nationalIdTypeCode;
    private String nationalIdType;
    private String nationalId;
    private String referralCode;
    private String promoCode;
    private String relationTypeCode;
    private String relationType;
    private String segmentTypeCode;
    private String amlRisk;
    private Date amlRiskEvalDate;
    private Boolean amlCheckStatus;
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
    private String countryOfResidenceCode;
    private String countryResidence;
    private String residencyTypeCode;
    private String residencyType;
    private String jobPosition;
    private String createdByChannel;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date lastRiskEvalDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date createdAt;
    private String updatedByChannel;
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date updatedAt;
    private Boolean isDeceased;

    private Boolean isInsolvency;
    private Boolean isNpa;

    private Boolean isWillfulDefaulter;

    private Date willfulDefaulterDate;

    private Boolean isLoanOverdue;

    private Boolean isSuitFailed;

    private Boolean isPep;

    private Boolean isFatcaApplicable;

    private Boolean isEmailStatementReg;

    private Boolean isUnderWatchlist;

    private Boolean isDeleted;

    private String createdBy;

    private String updatedBy;

    public String getType() {
        return type;
    }

    public String getMotherMaidenName() {
        return motherMaidenName;
    }

    public String getCountryBirthCode() {
        return countryBirthCode;
    }

    public String getStatus() {
        return status;
    }

    public String getCountryResidenceCode() {
        return countryResidenceCode;
    }

    public String getProfessionCode() {
        return professionCode;
    }

    public String getProfessionTypeCode() {
        return professionTypeCode;
    }

    public String getIndustryTypeCode() {
        return industryTypeCode;
    }

    public String getCompanyTypeCode() {
        return companyTypeCode;
    }

    public Integer getAnnualIncome() {
        return annualIncome;
    }

    public Integer getAnnualTurnover() {
        return annualTurnover;
    }

    public String getTaxId() {
        return taxId;
    }

    public String getDateOfInCorp() {
        return dateOfInCorp;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public String getSegmentTypeCode() {
        return segmentTypeCode;
    }

    public String getAmlRisk() {
        return amlRisk;
    }

    public Date getAmlRiskEvalDate() {
        return amlRiskEvalDate;
    }

    public Boolean getAmlCheckStatus() {
        return amlCheckStatus;
    }


    public Boolean getDeceased() {
        return isDeceased;
    }

    public Boolean getInsolvency() {
        return isInsolvency;
    }

    public Boolean getNpa() {
        return isNpa;
    }

    public Boolean getWillfulDefaulter() {
        return isWillfulDefaulter;
    }

    public Date getWillfulDefaulterDate() {
        return willfulDefaulterDate;
    }

    public Boolean getLoanOverdue() {
        return isLoanOverdue;
    }

    public Boolean getSuitFailed() {
        return isSuitFailed;
    }

    public Boolean getPep() {
        return isPep;
    }

    public Boolean getFatcaApplicable() {
        return isFatcaApplicable;
    }

    public Boolean getEmailStatementReg() {
        return isEmailStatementReg;
    }

    public Boolean getUnderWatchlist() {
        return isUnderWatchlist;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

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

    public Boolean getIsStaff() {
        return isStaff;
    }

    public void setIsStaff(Boolean isStaff) {
        this.isStaff = isStaff;
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

    public String getCountryOfResidenceCode() {
        return countryOfResidenceCode;
    }

    public void setCountryOfResidenceCode(String countryOfResidenceCode) {
        this.countryOfResidenceCode = countryOfResidenceCode;
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

    public Date getLastRiskEvalDate() {
        return lastRiskEvalDate;
    }

    public void setLastRiskEvalDate(Date lastRiskEvalDate) {
        this.lastRiskEvalDate = lastRiskEvalDate;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedByChannel() {
        return updatedByChannel;
    }

    public void setUpdatedByChannel(String updatedByChannel) {
        this.updatedByChannel = updatedByChannel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartyIdentifier() {
        return partyIdentifier;
    }

    public void setPartyIdentifier(String partyIdentifier) {
        this.partyIdentifier = partyIdentifier;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMotherMaidenName(String motherMaidenName) {
        this.motherMaidenName = motherMaidenName;
    }

    public void setCountryBirthCode(String countryBirthCode) {
        this.countryBirthCode = countryBirthCode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCountryResidenceCode(String countryResidenceCode) {
        this.countryResidenceCode = countryResidenceCode;
    }

    public void setProfessionCode(String professionCode) {
        this.professionCode = professionCode;
    }

    public void setProfessionTypeCode(String professionTypeCode) {
        this.professionTypeCode = professionTypeCode;
    }

    public void setIndustryTypeCode(String industryTypeCode) {
        this.industryTypeCode = industryTypeCode;
    }

    public void setCompanyTypeCode(String companyTypeCode) {
        this.companyTypeCode = companyTypeCode;
    }

    public void setAnnualIncome(Integer annualIncome) {
        this.annualIncome = annualIncome;
    }

    public void setAnnualTurnover(Integer annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public void setDateOfInCorp(String dateOfInCorp) {
        this.dateOfInCorp = dateOfInCorp;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public void setSegmentTypeCode(String segmentTypeCode) {
        this.segmentTypeCode = segmentTypeCode;
    }

    public void setAmlRisk(String amlRisk) {
        this.amlRisk = amlRisk;
    }

    public void setAmlRiskEvalDate(Date amlRiskEvalDate) {
        this.amlRiskEvalDate = amlRiskEvalDate;
    }

    public void setAmlCheckStatus(Boolean amlCheckStatus) {
        this.amlCheckStatus = amlCheckStatus;
    }

    public void setStaff(Boolean staff) {
        isStaff = staff;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setDeceased(Boolean deceased) {
        isDeceased = deceased;
    }

    public void setInsolvency(Boolean insolvency) {
        isInsolvency = insolvency;
    }

    public void setNpa(Boolean npa) {
        isNpa = npa;
    }

    public void setWillfulDefaulter(Boolean willfulDefaulter) {
        isWillfulDefaulter = willfulDefaulter;
    }

    public void setWillfulDefaulterDate(Date willfulDefaulterDate) {
        this.willfulDefaulterDate = willfulDefaulterDate;
    }

    public void setLoanOverdue(Boolean loanOverdue) {
        isLoanOverdue = loanOverdue;
    }

    public void setSuitFailed(Boolean suitFailed) {
        isSuitFailed = suitFailed;
    }

    public void setPep(Boolean pep) {
        isPep = pep;
    }

    public void setFatcaApplicable(Boolean fatcaApplicable) {
        isFatcaApplicable = fatcaApplicable;
    }

    public void setEmailStatementReg(Boolean emailStatementReg) {
        isEmailStatementReg = emailStatementReg;
    }

    public void setUnderWatchlist(Boolean underWatchlist) {
        isUnderWatchlist = underWatchlist;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
