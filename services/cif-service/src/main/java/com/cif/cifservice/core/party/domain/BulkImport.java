package com.cif.cifservice.core.party.domain;

public class BulkImport {
    private String output;
    private int totalRecordsAddressMaster;
    private int successRecordsAddressMaster;
    private int failedRecordsAddressMaster;
    private int totalRecordsCountryMaster;
    private int successRecordsCountryMaster;
    private int failedRecordsCountryMaster;
    private int totalRecordsLookupMaster;
    private int successRecordsLookupMaster;
    private int failedRecordsLookupMaster;

 public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public int getTotalRecordsAddressMaster() {
        return totalRecordsAddressMaster;
    }

    public void setTotalRecordsAddressMaster(int totalRecordsAddressMaster) {
        this.totalRecordsAddressMaster = totalRecordsAddressMaster;
    }

    public int getSuccessRecordsAddressMaster() {
        return successRecordsAddressMaster;
    }

    public void setSuccessRecordsAddressMaster(int successRecordsAddressMaster) {
        this.successRecordsAddressMaster = successRecordsAddressMaster;
    }

    public int getFailedRecordsAddressMaster() {
        return failedRecordsAddressMaster;
    }

    public void setFailedRecordsAddressMaster(int failedRecordsAddressMaster) {
        this.failedRecordsAddressMaster = failedRecordsAddressMaster;
    }

    public int getTotalRecordsCountryMaster() {
        return totalRecordsCountryMaster;
    }

    public void setTotalRecordsCountryMaster(int totalRecordsCountryMaster) {
        this.totalRecordsCountryMaster = totalRecordsCountryMaster;
    }

    public int getSuccessRecordsCountryMaster() {
        return successRecordsCountryMaster;
    }

    public void setSuccessRecordsCountryMaster(int successRecordsCountryMaster) {
        this.successRecordsCountryMaster = successRecordsCountryMaster;
    }

    public int getFailedRecordsCountryMaster() {
        return failedRecordsCountryMaster;
    }

    public void setFailedRecordsCountryMaster(int failedRecordsCountryMaster) {
        this.failedRecordsCountryMaster = failedRecordsCountryMaster;
    }

    public int getTotalRecordsLookupMaster() {
        return totalRecordsLookupMaster;
    }

    public void setTotalRecordsLookupMaster(int totalRecordsLookupMaster) {
        this.totalRecordsLookupMaster = totalRecordsLookupMaster;
    }

    public int getSuccessRecordsLookupMaster() {
        return successRecordsLookupMaster;
    }

    public void setSuccessRecordsLookupMaster(int successRecordsLookupMaster) {
        this.successRecordsLookupMaster = successRecordsLookupMaster;
    }

    public int getFailedRecordsLookupMaster() {
        return failedRecordsLookupMaster;
    }

    public void setFailedRecordsLookupMaster(int failedRecordsLookupMaster) {
        this.failedRecordsLookupMaster = failedRecordsLookupMaster;
    }
}
