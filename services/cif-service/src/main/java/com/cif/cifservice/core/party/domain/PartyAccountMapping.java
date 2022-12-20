package com.cif.cifservice.core.party.domain;




public class PartyAccountMapping {
    private Long partyAccountMappingId;
    private String accountId;
    private String relation;
    public PartyAccountMapping(Long partyAccountMappingId, String accountId, String relation) {
        this.partyAccountMappingId = partyAccountMappingId;
        this.accountId = accountId;
        this.relation = relation;
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

    public PartyAccountMapping() {
    }

}
