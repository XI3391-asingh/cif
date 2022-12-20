package com.finx.commonservice.resources;

import ch.qos.logback.classic.Level;
import com.codahale.metrics.MetricRegistry;
import com.finx.commonservice.CommonServiceConfiguration;
import com.finx.commonservice.api.MobileNumberCmd;
import com.finx.commonservice.api.ResponseApi;
import com.finx.commonservice.api.VerifyOtpCmd;
import com.finx.commonservice.core.otp.domain.OtpDetail;
import com.finx.commonservice.core.otp.enums.OtpStatus;
import com.finx.commonservice.core.otp.services.OtpService;
import com.finx.commonservice.resources.exceptionmappers.FailedToCreateOtpExceptionMapper;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
class OtpResourceTests {

    public static final String PATH_SEND_OTP = "/otp";
    public static final String PATH_VERIFY_OTP = "/otp/verify";
    public static final String PATH_BLOCKING_PERIOD_EXPIRED = "otp/blocking-period-expired";
    private final OtpService mockOtpService = mock(OtpService.class);
    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new OtpResource(mockOtpService))
                    .addProvider(() ->
                            new FailedToCreateOtpExceptionMapper(new MetricRegistry()))
                    .build();

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    @Test
    void should_create_otp_detail_and_send_successfully() {

        OtpDetail otpDetail = createOtpDetail();
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0876543210").build();
        when(mockOtpService.createOtpDetail(mobileNumberCmd)).thenReturn(otpDetail);
        Entity<MobileNumberCmd> entity = Entity.json(mobileNumberCmd);
        final Response response = resourceTestClient.target(PATH_SEND_OTP)
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
        var r = response.readEntity(ResponseApi.class);
        assertThat(r.getMessage()).isEqualTo(OtpStatus.GENERATED.name());

    }

    @Test
    void should_create_otp_detail_and_verify_otp_successfully() {
        OtpDetail otpDetail = createOtpDetail();

        VerifyOtpCmd verifyOtpCmd = VerifyOtpCmd.builder()
                .countryCode("+84")
                .number("0867543210")
                .otp(123456).build();

        CommonServiceConfiguration configuration = createConfiguration();

        when(mockOtpService.verifyOtp(verifyOtpCmd)).thenReturn(otpDetail);
        when(mockOtpService.getConfiguration()).thenReturn(configuration);
        Entity<VerifyOtpCmd> entity = Entity.json(verifyOtpCmd);
        final Response response = resourceTestClient.target(PATH_VERIFY_OTP)
                .request()
                .post(entity);

        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void should_return_failure_response_when_fail_to_create_otp() {
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0876543210").build();
        when(mockOtpService.createOtpDetail(mobileNumberCmd)).thenThrow(new RuntimeException());

        Entity<MobileNumberCmd> entity = Entity.json(mobileNumberCmd);
        final Response response = resourceTestClient.target(PATH_SEND_OTP)
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(ErrorMessage.class).getMessage())
                .isEqualTo("Failed to create otp");
    }

    @Test
    void should_return_400_if_mobile_number_is_not_valid_while_sending_otp() {
        OtpDetail otpDetail = createOtpDetail();
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("9876543210").build();
        when(mockOtpService.createOtpDetail(mobileNumberCmd)).thenReturn(otpDetail);
        Entity<MobileNumberCmd> entity = Entity.json(mobileNumberCmd);
        final Response response = resourceTestClient.target(PATH_SEND_OTP)
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }
    @Test
    void should_return_403_if_mobile_verification_has_failed_due_to_multiple_resend_attempts() {
        OtpDetail otpDetail = createOtpDetail();
        otpDetail.setOtpStatus(OtpStatus.GENERATION_BLOCKED);

        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0876543210").build();
        when(mockOtpService.createOtpDetail(mobileNumberCmd)).thenReturn(otpDetail);
        Entity<MobileNumberCmd> entity = Entity.json(mobileNumberCmd);
        final Response response = resourceTestClient.target(PATH_SEND_OTP)
                .request()
                .post(entity);

        assertThat(response.readEntity(ResponseApi.class).getStatusCode()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
    }
    @Test
    void should_return_400_if_mobile_number_is_not_valid_while_verifying_otp() {
        OtpDetail otpDetail = createOtpDetail();
        VerifyOtpCmd verifyOtpCmd = VerifyOtpCmd.builder()
                .countryCode("+84")
                .number("9867543210")
                .otp(123456).build();
        when(mockOtpService.verifyOtp(verifyOtpCmd)).thenReturn(otpDetail);
        Entity<VerifyOtpCmd> entity = Entity.json(verifyOtpCmd);
        final Response response = resourceTestClient.target(PATH_VERIFY_OTP)
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }
    @Test
    void should_return_403_if_mobile_verification_has_failed_due_to_multiple_failed_validation_attempts() {
        OtpDetail otpDetail = createOtpDetail();
        VerifyOtpCmd verifyOtpCmd = VerifyOtpCmd.builder()
                .countryCode("+84")
                .number("0867543210")
                .otp(123456).build();
        otpDetail.setOtpStatus(OtpStatus.MOBILE_VERIFICATION_FAILED);

        when(mockOtpService.verifyOtp(verifyOtpCmd)).thenReturn(otpDetail);
        Entity<VerifyOtpCmd> entity = Entity.json(verifyOtpCmd);
        final Response response = resourceTestClient.target(PATH_VERIFY_OTP)
                .request()
                .post(entity);
        assertThat(response.readEntity(ResponseApi.class).getStatusCode()).isEqualTo(Response.Status.FORBIDDEN.getStatusCode());
    }

    @Test
    void should_return_402_if_mobile_verification_has_failed_but_remaining_attempts_are_not_expired() {
        OtpDetail otpDetail = createOtpDetail();
        VerifyOtpCmd verifyOtpCmd = VerifyOtpCmd.builder()
                .countryCode("+84")
                .number("0867543210")
                .otp(123456).build();
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setRemainingAttemptsVerify(3);

        CommonServiceConfiguration configuration = createConfiguration();

        when(mockOtpService.verifyOtp(verifyOtpCmd)).thenReturn(otpDetail);
        when(mockOtpService.getConfiguration()).thenReturn(configuration);
        Entity<VerifyOtpCmd> entity = Entity.json(verifyOtpCmd);
        final Response response = resourceTestClient.target(PATH_VERIFY_OTP)
                .request()
                .post(entity);

        assertThat(response.readEntity(ResponseApi.class).getStatusCode()).isEqualTo(Response.Status.PAYMENT_REQUIRED.getStatusCode());
    }

    @Test
    void should_return_404_if_otp_is_not_present() {
        VerifyOtpCmd verifyOtpCmd = VerifyOtpCmd.builder()
                .countryCode("+84")
                .number("0867543210")
                .otp(123456).build();
        when(mockOtpService.verifyOtp(verifyOtpCmd)).thenReturn(null);
        Entity<VerifyOtpCmd> entity = Entity.json(verifyOtpCmd);
        final Response response = resourceTestClient.target(PATH_VERIFY_OTP)
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void should_return_failure_response_when_fail_to_verify_otp() {
        VerifyOtpCmd verifyOtpCmd = VerifyOtpCmd.builder()
                .countryCode("+84")
                .number("0867543210")
                .otp(123456).build();
        when(mockOtpService.verifyOtp(verifyOtpCmd)).thenThrow(new RuntimeException());

        Entity<VerifyOtpCmd> entity = Entity.json(verifyOtpCmd);
        final Response response = resourceTestClient.target(PATH_VERIFY_OTP)
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(ErrorMessage.class).getMessage())
                .isEqualTo("Failed to verify otp");
    }

    @Test
    void should_return_true_if_blocking_period_expired() {
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0876543210").build();

        when(mockOtpService.isBlockingPeriodExpired(mobileNumberCmd)).thenReturn(true);
        Entity<MobileNumberCmd> entity = Entity.json(mobileNumberCmd);
        final Response response = resourceTestClient.target(PATH_BLOCKING_PERIOD_EXPIRED)
                .request()
                .post(entity);

        var r = response.readEntity(ResponseApi.class);
        assertThat(r.getData().toString()).isEqualTo("{isBlockingPeriodExpired=true}");
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void should_return_false_if_blocking_period_not_expired() {
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0876543210").build();

        when(mockOtpService.isBlockingPeriodExpired(mobileNumberCmd)).thenReturn(false);
        Entity<MobileNumberCmd> entity = Entity.json(mobileNumberCmd);
        final Response response = resourceTestClient.target(PATH_BLOCKING_PERIOD_EXPIRED)
                .request()
                .post(entity);

        var r = response.readEntity(ResponseApi.class);
        assertThat(r.getData().toString()).isEqualTo("{isBlockingPeriodExpired=false}");
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void should_return_400_while_checking_blockage_if_mobile_number_is_invalid() {
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("9876543210").build();

        when(mockOtpService.isBlockingPeriodExpired(mobileNumberCmd)).thenReturn(false);
        Entity<MobileNumberCmd> entity = Entity.json(mobileNumberCmd);
        final Response response = resourceTestClient.target(PATH_BLOCKING_PERIOD_EXPIRED)
                .request()
                .post(entity);

        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    @Test
    void should_return_failure_response_when_checking_blocking_period_throws_exception() {
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0876543210").build();
        when(mockOtpService.isBlockingPeriodExpired(mobileNumberCmd)).thenThrow(new RuntimeException());

        Entity<MobileNumberCmd> entity = Entity.json(mobileNumberCmd);
        final Response response = resourceTestClient.target(PATH_BLOCKING_PERIOD_EXPIRED)
                .request()
                .post(entity);
        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(ErrorMessage.class).getMessage())
                .isEqualTo("Failed to check if blocking period has expired");
    }

    private OtpDetail createOtpDetail() {
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setMobileNumber("9876543210");
        otpDetail.setRemainingAttemptsSend(5);
        otpDetail.setRemainingAttemptsVerify(5);
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        return otpDetail;
    }

    private CommonServiceConfiguration createConfiguration() {
        CommonServiceConfiguration configuration = new CommonServiceConfiguration();
        configuration.setOtp(123456);
        configuration.setBlockingPeriod(10);
        configuration.setMaxValidationAttempt(5);
        configuration.setCodeExpirationTime(90);
        configuration.setMaxResendOtp(5);
        configuration.setOtpAttemptsReset(24);

        return configuration;
    }
}