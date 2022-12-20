package com.finx.commonservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public class CommonServiceConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @NotNull
    private Integer maxValidationAttempt;

    @NotNull
    private Integer maxResendOtp;

    @NotNull
    private Integer blockingPeriod;

    @NotNull
    private Integer codeExpirationTime;

    @NotNull
    private Integer otp;

    @NotNull
    private Integer otpAttemptsReset;

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDatasourceFactory(DataSourceFactory database) {
        this.database = database;
    }

    @JsonProperty("maxValidationAttempt")
    public Integer getMaxValidationAttempt() {
        return maxValidationAttempt;
    }

    @JsonProperty("maxValidationAttempt")
    public void setMaxValidationAttempt(Integer maxValidationAttempt) {
        this.maxValidationAttempt = maxValidationAttempt;
    }

    @JsonProperty("maxResendOtp")
    public Integer getMaxResendOtp() {
        return maxResendOtp;
    }

    @JsonProperty("maxResendOtp")
    public void setMaxResendOtp(Integer maxResendOtp) {
        this.maxResendOtp = maxResendOtp;
    }

    @JsonProperty("blockingPeriod")
    public Integer getBlockingPeriod() {
        return blockingPeriod;
    }

    @JsonProperty("blockingPeriod")
    public void setBlockingPeriod(Integer blockingPeriod) {
        this.blockingPeriod = blockingPeriod;
    }

    @JsonProperty("codeExpirationTime")
    public Integer getCodeExpirationTime() {
        return codeExpirationTime;
    }

    @JsonProperty("codeExpirationTime")
    public void setCodeExpirationTime(Integer codeExpirationTime) {
        this.codeExpirationTime = codeExpirationTime;
    }

    @JsonProperty("otp")
    public Integer getOtp() {
        return otp;
    }

    @JsonProperty("otp")
    public void setOtp(Integer otp) {
        this.otp = otp;
    }

    @JsonProperty("otpAttemptsReset")
    public Integer getOtpAttemptsReset() {
        return otpAttemptsReset;
    }

    @JsonProperty("otpAttemptsReset")
    public void setOtpAttemptsReset(Integer otpAttemptsReset) {
        this.otpAttemptsReset = otpAttemptsReset;
    }


}
