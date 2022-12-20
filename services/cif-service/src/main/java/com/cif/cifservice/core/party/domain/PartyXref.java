package com.cif.cifservice.core.party.domain;




public class PartyXref {
    private Long partyXrefId;
    private String systemCode;
    private String xrefId;

    public PartyXref(Long partyXrefId, String systemCode, String xrefId) {
        this.partyXrefId = partyXrefId;
        this.systemCode = systemCode;
        this.xrefId = xrefId;
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

    public PartyXref() {
    }

}
