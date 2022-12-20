package com.finx.commonservice.core.otp.domain;

import com.finx.commonservice.core.otp.enums.OtpStatus;

import java.sql.Timestamp;

public class OtpDetail {

    private Integer id;
    private String mobileNumber;
    private OtpStatus otpStatus;
    private Integer remainingAttemptsSend;
    private Integer otpExpirationTime;
    private Integer remainingAttemptsVerify;
    private Integer blockingTime;
    private String createdBy;
    private Timestamp createdOn;
    private String lastUpdatedBy;
    private Timestamp lastUpdatedOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public OtpStatus getOtpStatus() {
        return otpStatus;
    }

    public void setOtpStatus(OtpStatus otpStatus) {
        this.otpStatus = otpStatus;
    }

    public Integer getRemainingAttemptsSend() {
        return remainingAttemptsSend;
    }

    public void setRemainingAttemptsSend(Integer remainingAttemptsSend) {
        this.remainingAttemptsSend = remainingAttemptsSend;
    }

    public Integer getOtpExpirationTime() {
        return otpExpirationTime;
    }

    public void setOtpExpirationTime(Integer otpExpirationTime) {
        this.otpExpirationTime = otpExpirationTime;
    }

    public Integer getRemainingAttemptsVerify() {
        return remainingAttemptsVerify;
    }

    public void setRemainingAttemptsVerify(Integer remainingAttemptsVerify) {
        this.remainingAttemptsVerify = remainingAttemptsVerify;
    }

    public Integer getBlockingTime() {
        return blockingTime;
    }

    public void setBlockingTime(Integer blockingTime) {
        this.blockingTime = blockingTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Timestamp getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Timestamp createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(String lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Timestamp getLastUpdatedOn() {
        return lastUpdatedOn;
    }

    public void setLastUpdatedOn(Timestamp lastUpdatedOn) {
        this.lastUpdatedOn = lastUpdatedOn;
    }

}
