package com.finx.onboardingservice.db;

import com.finx.onboardingservice.core.onboarding.domain.KycDetail;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

public interface KycDetailRepository {

    @SqlUpdate("INSERT INTO kyc_detail (prospect_id, "
        + "mobile_number, "
        + "mobile_number_hashcode, "
        + "prospect_full_name, "
        + "date_of_birth, "
        + "kyc_system, "
        + "external_request_id, "
        + "kyc_type, "
        + "kyc_status, "
        + "document_type, "
        + "document_id_number, "
        + "original_kyc_details, "
        + "kyc_details, "
        + "is_manual_verification_needed, "
        + "manual_verification_needed_reason, "
        + "is_kyc_data_modified, "
        + "created_by, "
        + "created_date, "
        + "last_updated_by, "
        + "last_updated_date) "
        + "values (:prospectId, "
        + ":mobileNumber, "
        + ":mobileNumberHashcode, "
        + ":prospectFullName, "
        + ":dateOfBirth, "
        + ":kycSystem, "
        + ":externalRequestId, "
        + ":kycType, "
        + ":kycStatus, "
        + ":documentType, "
        + ":documentIdNumber, "
        + ":originalKycDetails::json, "
        + ":kycDetails::json, "
        + ":manualVerificationNeeded, "
        + ":manualVerificationNeededReason, "
        + ":kycDataModified, "
        + ":createdBy, "
        + ":createdDate, "
        + ":lastUpdatedBy, "
        + ":lastUpdatedDate);")
    @GetGeneratedKeys("id")
    @RegisterBeanMapper(KycDetail.class)
    KycDetail save(@BindBean KycDetail kycDetail);

    @SqlUpdate("UPDATE kyc_detail "
        + "SET prospect_id = :prospectId, "
        + "    mobile_number = :mobileNumber, "
        + "    mobile_number_hashcode = :mobileNumberHashcode, "
        + "    prospect_full_name = :prospectFullName, "
        + "    date_of_birth = :dateOfBirth, "
        + "    kyc_system = :kycSystem, "
        + "    external_request_id = :externalRequestId, "
        + "    kyc_type = :kycType, "
        + "    kyc_status = :kycStatus, "
        + "    document_type = :documentType, "
        + "    document_id_number = :documentIdNumber, "
        + "    original_kyc_details = :originalKycDetails::json, "
        + "    kyc_details = :kycDetails::json, "
        + "    is_manual_verification_needed = :manualVerificationNeeded, "
        + "    manual_verification_needed_reason = :manualVerificationNeededReason, "
        + "    is_kyc_data_modified = :kycDataModified, "
        + "    created_by = :createdBy, "
        + "    created_date = :createdDate, "
        + "    last_updated_by = :lastUpdatedBy, "
        + "    last_updated_date = :lastUpdatedDate "
        + "WHERE id = :id;")
    @RegisterBeanMapper(KycDetail.class)
    Integer update(@BindBean KycDetail kycDetail);

    @SqlQuery("SELECT * FROM kyc_detail where mobile_number = :mobileNumber")
    @RegisterBeanMapper(KycDetail.class)
    KycDetail getKycDetailByPhoneNumber(@Bind("mobileNumber") String mobileNumber);
}
