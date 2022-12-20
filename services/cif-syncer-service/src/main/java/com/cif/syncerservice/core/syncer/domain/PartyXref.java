package com.cif.syncerservice.core.syncer.domain;


public class PartyXref {
    private int partyXrefId;
    private String partyId;

    private int systemId;
    private String systemCode;
    private String xrefId;

    public int getPartyXrefId() {
        return partyXrefId;
    }

    public void setPartyXrefId(int partyXrefId) {
        this.partyXrefId = partyXrefId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
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

    public int getSystemId() {
        return systemId;
    }

    public void setSystemId(int systemId) {
        this.systemId = systemId;
    }
}
