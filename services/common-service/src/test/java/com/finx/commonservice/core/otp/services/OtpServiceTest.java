package com.finx.commonservice.core.otp.services;

import ch.qos.logback.classic.Level;
import com.finx.commonservice.CommonServiceConfiguration;
import com.finx.commonservice.api.MobileNumberCmd;
import com.finx.commonservice.api.VerifyOtpCmd;
import com.finx.commonservice.core.otp.domain.OtpDetail;
import com.finx.commonservice.core.otp.enums.OtpStatus;
import com.finx.commonservice.db.OtpDetailRepository;
import io.dropwizard.logging.BootstrapLogging;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class OtpServiceTest {

    private final OtpDetailRepository otpDetailRepository = mock(OtpDetailRepository.class);
    private final CommonServiceConfiguration configuration = mock(CommonServiceConfiguration.class);
    private final OtpService otpService = new OtpService(otpDetailRepository, configuration);

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    @Test
    void should_create_otp_detail_when_not_present() {
        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber())).thenReturn(Optional.empty());
        otpService.createOtpDetail(mobileNumberCmd);
        verify(otpDetailRepository).save(Mockito.any());
    }

    @Test
    void should_update_existing_otp_detail_if_present() {
        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber())).thenReturn(Optional.of(getOtpDetail(OtpStatus.GENERATED)));
        when(configuration.getMaxResendOtp()).thenReturn(5);
        when(configuration.getMaxValidationAttempt()).thenReturn(5);
        when(configuration.getOtpAttemptsReset()).thenReturn(24);
        otpService.createOtpDetail(mobileNumberCmd);
        verify(otpDetailRepository).updateRemainingAttemptsSendAndOtpStatus(Mockito.any());
    }
    @Test
    void should_update_remaining_attempts_if_otp_attempts_reset_is_expired() {

        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();
        OtpDetail otpDetail = getOtpDetail(OtpStatus.MOBILE_VERIFICATION_FAILED);
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(otpDetail.getLastUpdatedOn().toLocalDateTime().minusDays(1)));
        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber())).thenReturn(Optional.of(otpDetail));
        otpService.createOtpDetail(mobileNumberCmd);
        verify(otpDetailRepository).resetRemainingAttemptsVerifyAndSend(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

    }

    @Test
    void should_create_new_otp_detail_if_mobile_verification_failed_and_blocking_period_is_expired() {
        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();
        OtpDetail otpDetail = getOtpDetail(OtpStatus.GENERATION_BLOCKED);
        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber())).thenReturn(
                Optional.of(otpDetail));
        when(configuration.getBlockingPeriod()).thenReturn(0);
        when(configuration.getMaxResendOtp()).thenReturn(5);
        when(configuration.getMaxValidationAttempt()).thenReturn(5);
        when(configuration.getOtpAttemptsReset()).thenReturn(24);

        otpService.createOtpDetail(mobileNumberCmd);
        verify(otpDetailRepository).save(Mockito.any());
    }

    @Test
    void should_return_same_otp_detail_if_mobile_verification_is_successful() {
        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();
        OtpDetail otpDetail = getOtpDetail(OtpStatus.MOBILE_VERIFICATION_SUCCESSFUL);

        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber())).thenReturn(Optional.of(otpDetail));
        var otp = otpService.createOtpDetail(mobileNumberCmd);
        assertThat(otp.getLastUpdatedOn()).isEqualTo(otpDetail.getLastUpdatedOn());
    }

    @Test
    void should_update_status_to_mobile_verification_successful_if_otp_correct() {

        VerifyOtpCmd verifyOtpCmd = getVerifyOtpCmd(123456);

        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber()))
                .thenReturn(Optional.of(getOtpDetail(OtpStatus.GENERATED)));
        when(configuration.getOtp()).thenReturn(123456);
        otpService.verifyOtp(verifyOtpCmd);
        verify(otpDetailRepository).resetRemainingAttemptsVerifyAndSend(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any());

    }

    @Test
    void should_return_null_if_otp_detail_is_not_present() {

        VerifyOtpCmd verifyOtpCmd = getVerifyOtpCmd(123456);

        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber()))
                .thenReturn(Optional.empty());
        var otpDetail = otpService.verifyOtp(verifyOtpCmd);
        assertThat(otpDetail).isNull();

    }

    @Test
    void should_update_remaining_attempts_verify_wrong_otp_is_entered() {

        VerifyOtpCmd verifyOtpCmd = getVerifyOtpCmd(654321);

        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber()))
                .thenReturn(Optional.of(getOtpDetail(OtpStatus.GENERATED)));
        otpService.verifyOtp(verifyOtpCmd);
        verify(otpDetailRepository).updateRemainingAttemptsVerifyAndOtpStatus(Mockito.any());

    }

    @Test
    void should_return_otp_detail_if_present_but_otp_status_is_failed_or_successful() {

        VerifyOtpCmd verifyOtpCmd = getVerifyOtpCmd(123456);

        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();
        OtpDetail otpDetail = getOtpDetail(OtpStatus.MOBILE_VERIFICATION_FAILED);
        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber()))
                .thenReturn(Optional.of(otpDetail));
        var otp = otpService.verifyOtp(verifyOtpCmd);
        assertThat(otp.getLastUpdatedOn()).isEqualTo(otpDetail.getLastUpdatedOn());

    }
    @Test
    void should_return_true_if_mobile_number_does_not_exist() {
        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();
        var isBlockingPeriodExpired = otpService.isBlockingPeriodExpired(mobileNumberCmd);
        assertTrue(isBlockingPeriodExpired);
    }
    @Test
    void should_return_false_if_mobile_number_is_blocked() {
        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();
        OtpDetail otpDetail = getOtpDetail(OtpStatus.MOBILE_VERIFICATION_FAILED);
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now().minusMinutes(5)));
        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber()))
                .thenReturn(Optional.of(otpDetail));
        when(configuration.getBlockingPeriod()).thenReturn(10);
        var isBlockingPeriodExpired = otpService.isBlockingPeriodExpired(mobileNumberCmd);
        assertFalse(isBlockingPeriodExpired);
    }

    @Test
    void should_return_true_if_mobile_number_not_blocked() {
        MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();
        OtpDetail otpDetail = getOtpDetail(OtpStatus.MOBILE_VERIFICATION_FAILED);
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now().minusMinutes(11)));
        when(otpDetailRepository.getLatestOtpDetail(mobileNumberCmd.getNumber()))
                .thenReturn(Optional.of(otpDetail));
        when(configuration.getBlockingPeriod()).thenReturn(10);
        var isBlockingPeriodExpired = otpService.isBlockingPeriodExpired(mobileNumberCmd);
        assertTrue(isBlockingPeriodExpired);
    }

    private MobileNumberCmd getMobileNumberCmd() {
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("9876543210").build();
        return mobileNumberCmd;
    }

    private VerifyOtpCmd getVerifyOtpCmd(int otp) {
        VerifyOtpCmd verifyOtpCmd = VerifyOtpCmd.builder()
                .countryCode("+84")
                .number("9876543210")
                .otp(otp).build();
        return verifyOtpCmd;
    }

    private OtpDetail getOtpDetail(OtpStatus otpStatus) {
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setId(RandomUtils.nextInt());
        otpDetail.setMobileNumber("9876543210");
        otpDetail.setRemainingAttemptsSend(5);
        otpDetail.setRemainingAttemptsVerify(5);
        otpDetail.setOtpStatus(otpStatus);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));
        return otpDetail;
    }
}
