package com.cif.cifservice.core.party.domain;


public class PartyNominee {
    private String salutationCode;
    private String salutation;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nomineeRelationTypeCode;
    private String nomineeRelation;
    private String nationalId;
    private String nationalIdMasked;
    private String nationalIdToken;
    private Float percentage;
    private Integer partyNomineesId;

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

    public String getNomineeRelationTypeCode() {
        return nomineeRelationTypeCode;
    }

    public void setNomineeRelationTypeCode(String nomineeRelationTypeCode) {
        this.nomineeRelationTypeCode = nomineeRelationTypeCode;
    }

    public String getNomineeRelation() {
        return nomineeRelation;
    }

    public void setNomineeRelation(String nomineeRelation) {
        this.nomineeRelation = nomineeRelation;
    }

    public String getNationalId() {
        return nationalId;
    }

    public void setNationalId(String nationalId) {
        this.nationalId = nationalId;
    }

    public String getNationalIdMasked() {
        return nationalIdMasked;
    }

    public void setNationalIdMasked(String nationalIdMasked) {
        this.nationalIdMasked = nationalIdMasked;
    }

    public String getNationalIdToken() {
        return nationalIdToken;
    }

    public void setNationalIdToken(String nationalIdToken) {
        this.nationalIdToken = nationalIdToken;
    }

    public Float getPercentage() {
        return percentage;
    }

    public void setPercentage(Float percentage) {
        this.percentage = percentage;
    }

    public Integer getPartyNomineesId() {
        return partyNomineesId;
    }

    public void setPartyNomineesId(Integer partyNomineesId) {
        this.partyNomineesId = partyNomineesId;
    }


}
