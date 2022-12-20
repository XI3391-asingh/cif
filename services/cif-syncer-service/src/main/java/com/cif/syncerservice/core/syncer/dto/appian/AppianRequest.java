package com.cif.syncerservice.core.syncer.dto.appian;

public class AppianRequest {
    private String partyId;
    private String fullName;
    private String firstName;
    private String middleName;
    private String lastName;
    private String dateOfBirth;
    private String mobileNumber;
    private String emailId;
    private String nationality;
    private String onBoardingDate;
    private String customerRiskCategory;

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setOnBoardingDate(String onBoardingDate) {
        this.onBoardingDate = onBoardingDate;
    }

    public void setCustomerRiskCategory(String customerRiskCategory) {
        this.customerRiskCategory = customerRiskCategory;
    }

    public String getPartyId() {
        return partyId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getNationality() {
        return nationality;
    }

    public String getOnBoardingDate() {
        return onBoardingDate;
    }

    public String getCustomerRiskCategory() {
        return customerRiskCategory;
    }
}
