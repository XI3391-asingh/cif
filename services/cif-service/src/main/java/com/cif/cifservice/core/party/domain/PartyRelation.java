package com.cif.cifservice.core.party.domain;

public class PartyRelation {
    private Long partyRelationId;
    private String secondaryPartyId;
    private String partyRelationTypeCode;
    private String partyRelationType;
    private String invRelation;

    public PartyRelation(Long partyRelationId, String secondaryPartyId, String partyRelationTypeCode, String partyRelationType, String invRelation) {
        this.partyRelationId = partyRelationId;
        this.secondaryPartyId = secondaryPartyId;
        this.partyRelationTypeCode = partyRelationTypeCode;
        this.partyRelationType = partyRelationType;
        this.invRelation = invRelation;
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

    public PartyRelation() {
    }


}
