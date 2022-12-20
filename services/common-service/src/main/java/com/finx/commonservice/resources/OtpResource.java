package com.finx.commonservice.resources;

import com.finx.commonservice.api.MobileNumberCmd;
import com.finx.commonservice.api.OtpApi;
import com.finx.commonservice.api.ResponseApi;
import com.finx.commonservice.api.VerifyOtpCmd;
import com.finx.commonservice.core.otp.services.OtpService;
import com.finx.commonservice.resources.exceptions.OtpException;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

import static com.finx.commonservice.core.otp.enums.OtpStatus.*;
import static com.finx.commonservice.util.OtpUtil.getErrorResponse;
import static com.finx.commonservice.util.OtpUtil.isMobileNumberNotValid;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class OtpResource implements OtpApi {

    private OtpService otpService;
    private static final String MOBILE_NUMBER_INVALID = "Mobile number is invalid";
    public static final String OTP_NOT_PRESENT = "OTP not present";

    public OtpResource(OtpService otpService) {
        this.otpService = otpService;
    }

    @Override
    public Response sendOtp(MobileNumberCmd mobileNumberCmd) {
        try {
            ResponseApi responseApi;
            log.info("Command received to send a new Otp [cmd={}]", mobileNumberCmd);
            if (isMobileNumberNotValid(mobileNumberCmd.getNumber())) {
                log.info(MOBILE_NUMBER_INVALID);
                return getErrorResponse(Response.Status.BAD_REQUEST, MOBILE_NUMBER_INVALID);
            }
            var otpDetail = otpService.createOtpDetail(mobileNumberCmd);
            log.info("Otp created [cmd={},id={}]", mobileNumberCmd, otpDetail.getId());

            Map<String, Integer> dataMap = new HashMap<>();
            dataMap.put("codeExpirationTime", otpDetail.getOtpExpirationTime());
            dataMap.put("remainingAttempts", otpDetail.getRemainingAttemptsSend());
            if (otpDetail.getOtpStatus() == GENERATION_BLOCKED
                    || otpDetail.getOtpStatus() == MOBILE_VERIFICATION_FAILED) {
                responseApi = ResponseApi.builder()
                        .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                        .success(false)
                        .message(otpDetail.getOtpStatus().name())
                        .data(dataMap).build();
                log.info("Otp not sent. Verification has failed. Returning response..");
            } else {
                responseApi = ResponseApi.builder()
                        .statusCode(Response.Status.OK.getStatusCode())
                        .success(true)
                        .message(otpDetail.getOtpStatus().name())
                        .data(dataMap).build();
                log.info("Otp is sent. Returning response..");
            }
            return Response.status(Response.Status.OK).entity(responseApi).build();
        } catch (Exception e) {
            log.error("Failed to create Otp for [cmd={}]", mobileNumberCmd, e);
            throw new OtpException(
                    "Failed to create otp",
                    e);
        }
    }

    @Override
    public Response verifyOtp(VerifyOtpCmd verifyOtpCmd) {
        try {
            ResponseApi responseApi;
            log.info("Command received to verify a new Otp [cmd={}]", verifyOtpCmd);
            if (isMobileNumberNotValid(verifyOtpCmd.getNumber())) {
                log.info(MOBILE_NUMBER_INVALID);
                return getErrorResponse(Response.Status.BAD_REQUEST, MOBILE_NUMBER_INVALID);
            }
            var otpDetail = otpService.verifyOtp(verifyOtpCmd);
            if (otpDetail != null) {
                log.info("Otp found [cmd={},id={}]", verifyOtpCmd, otpDetail.getId());
                Map<String, Integer> dataMap = new HashMap<>();
                dataMap.put("blockingTime", otpDetail.getBlockingTime());
                dataMap.put("remainingAttempts", otpDetail.getRemainingAttemptsVerify());
                if (otpDetail.getOtpStatus() == MOBILE_VERIFICATION_FAILED) {
                    responseApi = ResponseApi.builder()
                            .statusCode(Response.Status.FORBIDDEN.getStatusCode())
                            .success(false)
                            .message(otpDetail.getOtpStatus().name())
                            .data(dataMap).build();
                    log.info("Otp verification is blocked. Returning response..");
                } else {
                    if (otpDetail.getRemainingAttemptsVerify() < otpService.getConfiguration()
                            .getMaxValidationAttempt()
                            && (otpDetail.getOtpStatus() == GENERATED
                            || otpDetail.getOtpStatus() == GENERATION_BLOCKED)) {
                        responseApi = ResponseApi.builder()
                                .statusCode(Response.Status.PAYMENT_REQUIRED.getStatusCode())
                                .success(false)
                                .message(otpDetail.getOtpStatus().name())
                                .data(dataMap).build();
                        log.info("Wrong Otp entered. Returning response..");
                    } else {
                        responseApi = ResponseApi.builder()
                                .statusCode(Response.Status.OK.getStatusCode())
                                .success(true)
                                .message(otpDetail.getOtpStatus().name())
                                .data(dataMap).build();
                        log.info("Mobile verification is done. Returning response..");
                    }
                }
                return Response.status(Response.Status.OK).entity(responseApi).build();
            } else {
                return getErrorResponse(Response.Status.NOT_FOUND, OTP_NOT_PRESENT);
            }
        } catch (Exception e) {
            log.error("Failed to verify Otp for [cmd={}]", verifyOtpCmd, e);
            throw new OtpException(
                    "Failed to verify otp",
                    e);
        }
    }

    @Override
    public Response isBlockingPeriodExpired(MobileNumberCmd mobileNumberCmd) {
        try {
            log.info("Command received check if blocking period is expired for [cmd={}]", mobileNumberCmd);
            if (isMobileNumberNotValid(mobileNumberCmd.getNumber())) {
                log.info(MOBILE_NUMBER_INVALID);
                return getErrorResponse(Response.Status.BAD_REQUEST, MOBILE_NUMBER_INVALID);
            }
            boolean isBlockingPeriodExpired = otpService.isBlockingPeriodExpired(mobileNumberCmd);

            Map<String, Boolean> dataMap = new HashMap<>();
            dataMap.put("isBlockingPeriodExpired", isBlockingPeriodExpired);
            ResponseApi responseApi = ResponseApi.builder()
                    .statusCode(Response.Status.OK.getStatusCode())
                    .success(true)
                    .message(Response.Status.OK.name())
                    .data(dataMap).build();

            return Response.status(Response.Status.OK).entity(responseApi).build();
        } catch (Exception e) {
            log.error("Failed to check if blocking period has expired for [cmd={}]", mobileNumberCmd, e);
            throw new OtpException(
                    "Failed to check if blocking period has expired",
                    e);
        }
    }

}
