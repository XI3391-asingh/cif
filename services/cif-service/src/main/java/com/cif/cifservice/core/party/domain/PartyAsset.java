package com.cif.cifservice.core.party.domain;




public class PartyAsset {
    private String assetTypeCode;
    private String assetType;
    private String assetName;
    private Double potentialValue;
    private Boolean isMortgaged;
    private Integer partyAssetId;

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
    public Integer getPartyAssetId() {
        return partyAssetId;
    }
    public void setPartyAssetId(Integer partyAssetId) {
        this.partyAssetId = partyAssetId;
    }


    public PartyAsset() {
    }

    public PartyAsset(String assetTypeCode, String assetType, String assetName, Double potentialValue, Boolean isMortgaged, Integer partyAssetId) {
        this.assetTypeCode = assetTypeCode;
        this.assetType = assetType;
        this.assetName = assetName;
        this.potentialValue = potentialValue;
        this.isMortgaged = isMortgaged;
        this.partyAssetId = partyAssetId;
    }
}
