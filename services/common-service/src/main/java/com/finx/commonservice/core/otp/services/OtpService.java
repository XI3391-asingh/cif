package com.finx.commonservice.core.otp.services;

import com.finx.commonservice.CommonServiceConfiguration;
import com.finx.commonservice.api.MobileNumberCmd;
import com.finx.commonservice.api.VerifyOtpCmd;
import com.finx.commonservice.core.otp.domain.OtpDetail;
import com.finx.commonservice.core.otp.enums.OtpStatus;
import com.finx.commonservice.db.OtpDetailRepository;
import lombok.extern.slf4j.Slf4j;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;
@Slf4j
public class OtpService {

    private final OtpDetailRepository otpDetailRepository;
    private final CommonServiceConfiguration configuration;

    public OtpService(OtpDetailRepository otpDetailRepository, CommonServiceConfiguration configuration) {
        this.otpDetailRepository = otpDetailRepository;
        this.configuration = configuration;
    }

    public OtpDetail createOtpDetail(MobileNumberCmd cmd) {
        log.info("Creating otp detail for [cmd={}]", cmd);
        Optional<OtpDetail> latestOtpDetail = otpDetailRepository.getLatestOtpDetail(cmd.getNumber());
        if (latestOtpDetail.isPresent()) {
            log.info("Otp detail is already present for [cmd={}]", cmd);
            if (canOtpDetailReset(latestOtpDetail)) {
                log.info("Resetting remaining attempts using maxVerifyOtp and maxResendOtp configuration..");
                otpDetailRepository.resetRemainingAttemptsVerifyAndSend(cmd.getNumber(), OtpStatus.GENERATED,
                        configuration.getMaxResendOtp(), configuration.getMaxValidationAttempt());
                return getOtpDetailAfterUpdate(cmd.getNumber());
            }
            if (latestOtpDetail.get().getOtpStatus() == OtpStatus.GENERATED
                    || latestOtpDetail.get().getOtpStatus() == OtpStatus.MOBILE_VERIFICATION_SUCCESSFUL
                    && (latestOtpDetail.get().getRemainingAttemptsSend() > 0)) {
                log.info("Updating remaining attempts and status..");
                otpDetailRepository.updateRemainingAttemptsSendAndOtpStatus(cmd.getNumber());
                return getOtpDetailAfterUpdate(cmd.getNumber());
            } else if ((latestOtpDetail.get().getOtpStatus() == OtpStatus.GENERATION_BLOCKED
                    || latestOtpDetail.get().getOtpStatus() == OtpStatus.MOBILE_VERIFICATION_FAILED)
                    && isBlockingPeriodExpired(latestOtpDetail)) {
                log.info("Creating new otp details if user was blocked and blocking period was expired..");
                return saveOtpDetail(cmd);
            } else {
                log.info("Returning the latest otp details..");
                return latestOtpDetail.get();
            }
        } else {
            log.info("Otp detail is not present, Creating otp details for[cmd={}]", cmd);
            return saveOtpDetail(cmd);
        }
    }

    public OtpDetail verifyOtp(VerifyOtpCmd cmd) {
        log.info("Verifying otp detail for [cmd={}]", cmd);
        Optional<OtpDetail> latestOtpDetail = otpDetailRepository.getLatestOtpDetail(cmd.getNumber());
        if (latestOtpDetail.isPresent()) {
            if (latestOtpDetail.get().getOtpStatus() == OtpStatus.GENERATED
                    || latestOtpDetail.get().getOtpStatus() == OtpStatus.MOBILE_VERIFICATION_SUCCESSFUL
                    || latestOtpDetail.get().getOtpStatus() == OtpStatus.GENERATION_BLOCKED
                    || (latestOtpDetail.get().getOtpStatus() == OtpStatus.MOBILE_VERIFICATION_FAILED
                    && isBlockingPeriodExpired(latestOtpDetail))) {
                if (configuration.getOtp().equals(cmd.getOtp())) {
                    log.info("Otp entered is correct. Resetting remaining attempts using configuration.. ");
                    otpDetailRepository.resetRemainingAttemptsVerifyAndSend(cmd.getNumber(),
                            OtpStatus.MOBILE_VERIFICATION_SUCCESSFUL,
                            configuration.getMaxResendOtp(), configuration.getMaxValidationAttempt());
                } else {
                    log.info("Otp entered is incorrect. Updating remaining attempts using configuration.. ");
                    otpDetailRepository.updateRemainingAttemptsVerifyAndOtpStatus(cmd.getNumber());
                }
            } else {
                log.info("Returning latest Otp detail if otp status is not GENERATED");
                return latestOtpDetail.get();
            }
            return getOtpDetailAfterUpdate(cmd.getNumber());
        }
        return null;
    }

    public boolean isBlockingPeriodExpired(MobileNumberCmd cmd) {
        log.info("Checking if blocking period is expired for [cmd={}]", cmd);
        Optional<OtpDetail> latestOtpDetail = otpDetailRepository.getLatestOtpDetail(cmd.getNumber());
        return latestOtpDetail.isEmpty() || (latestOtpDetail.get().getOtpStatus()
                != OtpStatus.MOBILE_VERIFICATION_FAILED
                || latestOtpDetail.get().getLastUpdatedOn().toLocalDateTime()
                .plusMinutes(configuration.getBlockingPeriod()).isBefore(LocalDateTime.now()));
    }

    private OtpDetail saveOtpDetail(MobileNumberCmd cmd) {
        var otpDetail = new OtpDetail();
        otpDetail.setMobileNumber(cmd.getNumber());
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setRemainingAttemptsVerify(configuration.getMaxValidationAttempt());
        otpDetail.setRemainingAttemptsSend(configuration.getMaxResendOtp());
        otpDetail.setOtpExpirationTime(configuration.getCodeExpirationTime());
        otpDetail.setBlockingTime(configuration.getBlockingPeriod());
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setCreatedBy(cmd.getNumber());
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy(cmd.getNumber());

        otpDetailRepository.save(otpDetail);
        return otpDetail;

    }

    private boolean isBlockingPeriodExpired(Optional<OtpDetail> latestOtpDetail) {
        return latestOtpDetail.isEmpty() || latestOtpDetail.get().getLastUpdatedOn().toLocalDateTime()
                .plusMinutes(configuration.getBlockingPeriod())
                .isBefore(LocalDateTime.now());
    }

    private OtpDetail getOtpDetailAfterUpdate(String cmd) {
        Optional<OtpDetail> updatedOtpDetail = otpDetailRepository.getLatestOtpDetail(cmd);
        return updatedOtpDetail.isPresent() ? updatedOtpDetail.get() : null;
    }

    private boolean canOtpDetailReset(Optional<OtpDetail> latestOtpDetail) {
        return latestOtpDetail.isEmpty() || latestOtpDetail.get().getLastUpdatedOn().toLocalDateTime()
                .plusHours(configuration.getOtpAttemptsReset())
                .isBefore(LocalDateTime.now());
    }

    public CommonServiceConfiguration getConfiguration() {
        return configuration;
    }


}
