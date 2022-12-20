package com.finx.commonservice.db;

import com.finx.commonservice.core.otp.domain.OtpDetail;
import com.finx.commonservice.core.otp.enums.OtpStatus;
import org.jdbi.v3.core.mapper.RowMapper;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class OtpDetailRepositoryTest extends JdbiTest {

    @Test
    void should_create_otp_detail() {
        OtpDetailRepository otpDetailRepository = jdbi.onDemand(OtpDetailRepository.class);
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setMobileNumber("9867486912");
        otpDetail.setRemainingAttemptsSend(5);
        otpDetail.setRemainingAttemptsVerify(5);
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        var otp = otpDetailRepository.save(otpDetail);

        OtpDetail savedOtpDetail = jdbi.withHandle(handle ->
                handle.createQuery("select * from otp_detail where id = :id")
                        .bind("id", otp.getId())
                        .map(getOtpDetailRowMapper()).first());

        assertThat(savedOtpDetail.getMobileNumber()).isEqualTo("9867486912");
        assertThat(savedOtpDetail.getCreatedBy()).isEqualTo("finx");
        assertThat(savedOtpDetail.getOtpStatus()).isEqualTo(OtpStatus.GENERATED);
        assertThat(savedOtpDetail.getBlockingTime()).isEqualTo(10);

    }

    @Test
    void should_fetch_latest_generated_otp_detail() {

        OtpDetailRepository otpDetailRepository = jdbi.onDemand(OtpDetailRepository.class);
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setMobileNumber("9867486912");
        otpDetail.setRemainingAttemptsSend(5);
        otpDetail.setRemainingAttemptsVerify(5);
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        otpDetailRepository.save(otpDetail);

        OtpDetail otpDetail2 = new OtpDetail();
        otpDetail2.setMobileNumber("9867486912");
        otpDetail2.setRemainingAttemptsSend(4);
        otpDetail2.setRemainingAttemptsVerify(5);
        otpDetail2.setOtpStatus(OtpStatus.GENERATED);
        otpDetail2.setOtpExpirationTime(90);
        otpDetail2.setBlockingTime(10);
        otpDetail2.setCreatedBy("finx");
        otpDetail2.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail2.setLastUpdatedBy("finx");
        otpDetail2.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        otpDetailRepository.save(otpDetail2);

        Optional<OtpDetail> latestOtpDetail = otpDetailRepository.getLatestOtpDetail("9867486912");
        assertThat(latestOtpDetail.get()).isNotNull();
        assertThat(latestOtpDetail.get().getRemainingAttemptsSend()).isEqualTo(4);
    }

    @Test
    void should_update_remaining_attempts_send_in_otp_detail() {

        OtpDetailRepository otpDetailRepository = jdbi.onDemand(OtpDetailRepository.class);
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setMobileNumber("9867486912");
        otpDetail.setRemainingAttemptsSend(5);
        otpDetail.setRemainingAttemptsVerify(5);
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        var otp = otpDetailRepository.save(otpDetail);

        OtpDetail savedOtpDetail = jdbi.withHandle(handle ->
                handle.createQuery("select * from otp_detail where id = :id")
                        .bind("id", otp.getId())
                        .map(getOtpDetailRowMapper()).first());


        otpDetailRepository.updateRemainingAttemptsSendAndOtpStatus("9867486912");
        Optional<OtpDetail> updatedOtpDetail =  otpDetailRepository.getLatestOtpDetail("9867486912");
        assertThat(updatedOtpDetail.get().getLastUpdatedOn()).isAfter(savedOtpDetail.getLastUpdatedOn());
        assertThat(updatedOtpDetail.get().getRemainingAttemptsSend()).isLessThan(savedOtpDetail.getRemainingAttemptsSend());

    }

    @Test
    void should_update_remaining_attempts_send_and_otp_status_in_otp_detail() {

        OtpDetailRepository otpDetailRepository = jdbi.onDemand(OtpDetailRepository.class);
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setMobileNumber("9867486912");
        otpDetail.setRemainingAttemptsSend(1);
        otpDetail.setRemainingAttemptsVerify(5);
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        var otp = otpDetailRepository.save(otpDetail);

        OtpDetail savedOtpDetail = jdbi.withHandle(handle ->
                handle.createQuery("select * from otp_detail where id = :id")
                        .bind("id", otp.getId())
                        .map(getOtpDetailRowMapper()).first());


        otpDetailRepository.updateRemainingAttemptsSendAndOtpStatus("9867486912");
        Optional<OtpDetail> updatedOtpDetail =  otpDetailRepository.getLatestOtpDetail("9867486912");
        assertThat(updatedOtpDetail.get().getLastUpdatedOn()).isAfter(savedOtpDetail.getLastUpdatedOn());
        assertThat(updatedOtpDetail.get().getRemainingAttemptsSend()).isLessThan(savedOtpDetail.getRemainingAttemptsSend());
        assertThat(updatedOtpDetail.get().getOtpStatus()).isEqualTo(OtpStatus.GENERATION_BLOCKED);

    }

    @Test
    void should_update_remaining_attempts_verify_and_otp_status_in_otp_detail() {

        OtpDetailRepository otpDetailRepository = jdbi.onDemand(OtpDetailRepository.class);
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setMobileNumber("9867486912");
        otpDetail.setRemainingAttemptsSend(1);
        otpDetail.setRemainingAttemptsVerify(1);
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        var otp = otpDetailRepository.save(otpDetail);

        OtpDetail savedOtpDetail = jdbi.withHandle(handle ->
                handle.createQuery("select * from otp_detail where id = :id")
                        .bind("id", otp.getId())
                        .map(getOtpDetailRowMapper()).first());


        otpDetailRepository.updateRemainingAttemptsVerifyAndOtpStatus("9867486912");
        Optional<OtpDetail> updatedOtpDetail =  otpDetailRepository.getLatestOtpDetail("9867486912");
        assertThat(updatedOtpDetail.get().getLastUpdatedOn()).isAfter(savedOtpDetail.getLastUpdatedOn());
        assertThat(updatedOtpDetail.get().getRemainingAttemptsVerify()).isLessThan(savedOtpDetail.getRemainingAttemptsVerify());
        assertThat(updatedOtpDetail.get().getOtpStatus()).isEqualTo(OtpStatus.MOBILE_VERIFICATION_FAILED);

    }

    @Test
    void should_update_status_to_mobile_verification_successful() {

        OtpDetailRepository otpDetailRepository = jdbi.onDemand(OtpDetailRepository.class);
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setMobileNumber("9867486912");
        otpDetail.setRemainingAttemptsSend(1);
        otpDetail.setRemainingAttemptsVerify(1);
        otpDetail.setOtpStatus(OtpStatus.GENERATED);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        var otp = otpDetailRepository.save(otpDetail);

        OtpDetail savedOtpDetail = jdbi.withHandle(handle ->
                handle.createQuery("select * from otp_detail where id = :id")
                        .bind("id", otp.getId())
                        .map(getOtpDetailRowMapper()).first());

        otpDetailRepository.updateOtpStatus(OtpStatus.MOBILE_VERIFICATION_SUCCESSFUL, "9867486912");
        Optional<OtpDetail> updatedOtpDetail =  otpDetailRepository.getLatestOtpDetail("9867486912");
        assertThat(updatedOtpDetail.get().getLastUpdatedOn()).isAfter(savedOtpDetail.getLastUpdatedOn());
        assertThat(updatedOtpDetail.get().getOtpStatus()).isEqualTo(OtpStatus.MOBILE_VERIFICATION_SUCCESSFUL);

    }
    @Test
    void should_update_remaining_attempts_send_and_verify_as_per_configuration() {

        OtpDetailRepository otpDetailRepository = jdbi.onDemand(OtpDetailRepository.class);
        OtpDetail otpDetail = new OtpDetail();
        otpDetail.setMobileNumber("9867486912");
        otpDetail.setRemainingAttemptsSend(1);
        otpDetail.setRemainingAttemptsVerify(1);
        otpDetail.setOtpStatus(OtpStatus.MOBILE_VERIFICATION_FAILED);
        otpDetail.setOtpExpirationTime(90);
        otpDetail.setBlockingTime(10);
        otpDetail.setCreatedBy("finx");
        otpDetail.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
        otpDetail.setLastUpdatedBy("finx");
        otpDetail.setLastUpdatedOn(Timestamp.valueOf(LocalDateTime.now()));

        var otp = otpDetailRepository.save(otpDetail);

        OtpDetail savedOtpDetail = jdbi.withHandle(handle ->
                handle.createQuery("select * from otp_detail where id = :id")
                        .bind("id", otp.getId())
                        .map(getOtpDetailRowMapper()).first());

        otpDetailRepository.resetRemainingAttemptsVerifyAndSend("9867486912", OtpStatus.GENERATED,
                5, 5);

        Optional<OtpDetail> updatedOtpDetail =  otpDetailRepository.getLatestOtpDetail("9867486912");

        assertThat(updatedOtpDetail.get().getLastUpdatedOn()).isAfter(savedOtpDetail.getLastUpdatedOn());
        assertThat(updatedOtpDetail.get().getOtpStatus()).isEqualTo(OtpStatus.GENERATED);
        assertThat(updatedOtpDetail.get().getRemainingAttemptsVerify()).isEqualTo(5);
        assertThat(updatedOtpDetail.get().getRemainingAttemptsSend()).isEqualTo(5);

    }

    private RowMapper<OtpDetail> getOtpDetailRowMapper() {
        return (rs, ctx) -> {
            OtpDetail otpDetailDB = new OtpDetail();
            otpDetailDB.setId(rs.getInt("id"));
            otpDetailDB.setMobileNumber(rs.getString("mobile_number"));
            otpDetailDB.setOtpStatus(OtpStatus.valueOf(rs.getString("otp_status")));
            otpDetailDB.setRemainingAttemptsSend(rs.getInt("remaining_attempts_send"));
            otpDetailDB.setOtpExpirationTime(rs.getInt("otp_expiration_time"));
            otpDetailDB.setRemainingAttemptsVerify(rs.getInt("remaining_attempts_verify"));
            otpDetailDB.setBlockingTime(rs.getInt("blocking_time"));
            otpDetailDB.setCreatedBy(rs.getString("created_by"));
            otpDetailDB.setCreatedOn(rs.getTimestamp("created_on"));
            otpDetailDB.setLastUpdatedBy(rs.getString("last_updated_by"));
            otpDetailDB.setLastUpdatedOn(rs.getTimestamp("last_updated_on"));

            return otpDetailDB;
        };
    }
}
