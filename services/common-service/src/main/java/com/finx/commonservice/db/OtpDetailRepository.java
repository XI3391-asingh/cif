package com.finx.commonservice.db;

import com.finx.commonservice.core.otp.domain.OtpDetail;
import com.finx.commonservice.core.otp.enums.OtpStatus;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Optional;

public interface OtpDetailRepository {

    @SqlUpdate("INSERT INTO otp_detail (mobile_number, otp_status, remaining_attempts_send, "
            + " otp_expiration_time, remaining_attempts_verify, blocking_time, created_by, created_on, "
            + " last_updated_by, last_updated_on) "
            + " VALUES (:mobileNumber, :otpStatus, :remainingAttemptsSend, :otpExpirationTime, "
            + " :remainingAttemptsVerify, :blockingTime, :createdBy, :createdOn, "
            + " :lastUpdatedBy, :lastUpdatedOn)")
    @GetGeneratedKeys("id")
    @RegisterBeanMapper(OtpDetail.class)
    OtpDetail save(@BindBean OtpDetail otpDetail);


    @SqlQuery("SELECT * from otp_detail WHERE mobile_number = :mobileNumber "
            + " ORDER BY last_updated_on DESC LIMIT 1")
    @RegisterBeanMapper(OtpDetail.class)
    Optional<OtpDetail> getLatestOtpDetail(@Bind("mobileNumber") String mobileNumber);


    @SqlUpdate("UPDATE otp_detail SET remaining_attempts_send = remaining_attempts_send - 1, "
            + " otp_status = (CASE WHEN remaining_attempts_send > 1 THEN 'GENERATED' "
            + " ELSE 'GENERATION_BLOCKED' END), "
            + " last_updated_on = CURRENT_TIMESTAMP WHERE mobile_number = :mobileNumber "
            + " AND last_updated_on = ( SELECT MAX(last_updated_on) "
            + " FROM otp_detail WHERE mobile_number = :mobileNumber )")
    @RegisterBeanMapper(OtpDetail.class)
    int updateRemainingAttemptsSendAndOtpStatus(@Bind("mobileNumber") String mobileNumber);


    @SqlUpdate("UPDATE otp_detail SET remaining_attempts_verify = remaining_attempts_verify - 1, "
            + " otp_status = (CASE WHEN remaining_attempts_verify > 1 THEN 'GENERATED' "
            + " ELSE 'MOBILE_VERIFICATION_FAILED' END), "
            + " last_updated_on = CURRENT_TIMESTAMP WHERE mobile_number = :mobileNumber "
            + " AND last_updated_on = ( SELECT MAX(last_updated_on) "
            + " FROM otp_detail WHERE mobile_number = :mobileNumber )")
    @RegisterBeanMapper(OtpDetail.class)
    int updateRemainingAttemptsVerifyAndOtpStatus(@Bind("mobileNumber") String mobileNumber);

    @SqlUpdate("UPDATE otp_detail SET otp_status = :otpStatus, "
            + " last_updated_on = CURRENT_TIMESTAMP WHERE mobile_number = :mobileNumber "
            + " AND last_updated_on = ( SELECT MAX(last_updated_on) "
            + " FROM otp_detail WHERE mobile_number = :mobileNumber )")
    @RegisterBeanMapper(OtpDetail.class)
    int updateOtpStatus(@Bind("otpStatus") OtpStatus otpStatus, @Bind("mobileNumber") String mobileNumber);

    @SqlUpdate("UPDATE otp_detail SET remaining_attempts_send = :maxResendOtp, "
            + " remaining_attempts_verify = :maxValidationAttempt, otp_status = :otpStatus, "
            + "last_updated_on = CURRENT_TIMESTAMP WHERE mobile_number = :mobileNumber "
            + " AND last_updated_on = ( SELECT MAX(last_updated_on) "
            + " FROM otp_detail WHERE mobile_number = :mobileNumber )")
    @RegisterBeanMapper(OtpDetail.class)
    int resetRemainingAttemptsVerifyAndSend(@Bind("mobileNumber") String mobileNumber,
                                            @Bind("otpStatus") OtpStatus otpStatus,
                                            @Bind("maxResendOtp") Integer maxResendOtp,
                                            @Bind("maxValidationAttempt") Integer maxValidationAttempt);

}
