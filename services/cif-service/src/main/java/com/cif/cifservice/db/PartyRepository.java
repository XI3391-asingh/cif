package com.cif.cifservice.db;

import com.cif.cifservice.api.*;
import com.cif.cifservice.core.party.domain.*;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.Define;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface PartyRepository {

    @SqlUpdate("INSERT INTO party(party_id, type,salutation_code,full_name,first_name, middle_name, last_name, mother_maiden_name, nick_name, gender, date_of_birth, place_of_birth, country_birth_code, primary_mobile_number, primary_email, marital_status,status, country_residence_code, residency_type_code, education_type_code, profession_code, profession_type_code, job_position_type_code, industry_type_code,nationality_code, annual_income, annual_turnover, tax_id, date_of_incorp, staff_code, company_code, group_code, portfolio_code, segment_type_code, relation_type_code, refferal_code, promo_code,national_id_type_code,national_id,aml_risk,aml_risk_eval_date,aml_check_status, is_staff, created_at, created_by, created_by_channel) VALUES (nextval('party_id_seq'),:p.type, :p.salutationCode, :p.fullName, :p.firstName, :p.middleName, :p.lastName, :p.mothersMaidenName, :p.nickName, :p.gender,TO_DATE(:p.dateOfBirth,'dd-MM-yyyy'), :p.placeOfBirth, :p.countryOfBirthCode, :p.primaryMobileNumber, :p.primaryEmail, :p.maritalStatus, :p.status, :p.countryOfResidenceCode,:p.residencyTypeCode,:p.educationTypeCode, :po.professionCode, :po.professionTypeCode, :p.jobPositionTypeCode,:po.industryTypeCode, :p.nationalityCode,:po.annualIncome, :po.annualTurnover, :po.taxId, TO_DATE(:po.dateOfInCorporation,'dd-MM-yyyy'), :p.staffCode,:po.companyTypeCode, :p.groupCode, :p.portfolioCode,:p.segmentCode, :p.relationTypeCode, :p.referralCode,:p.promoCode,:p.nationalIdTypeCode,:p.nationalId,:p.amlRisk,TO_DATE(:p.amlRiskEvalDate,'dd-MM-yyyy'),:p.amlCheckStatus,:p.isStaff,now(),:p.createdBy,:p.createdByChannel);")
    @GetGeneratedKeys
    Long saveParty(@BindBean("p") CreatePartyCmd createPartyCmd, @BindBean("po") CreateOccupationDetailCmd createOccupationDetailCmd);

    @SqlUpdate("INSERT INTO party(party_id,party_identifier, type,salutation_code,full_name,first_name, middle_name, last_name, mother_maiden_name, nick_name, gender, date_of_birth, place_of_birth, country_birth_code, primary_mobile_number, primary_email, marital_status,status, country_residence_code, residency_type_code, education_type_code, profession_code, profession_type_code, job_position_type_code, industry_type_code,nationality_code, annual_income, annual_turnover, tax_id, date_of_incorp, staff_code, company_code, group_code, portfolio_code, segment_type_code, relation_type_code, refferal_code, promo_code,national_id_type_code,national_id,aml_risk,aml_risk_eval_date,aml_check_status, is_staff, created_at, created_by, created_by_channel) VALUES (nextval('party_id_seq'),:p.partyIdentifier,:p.type, :p.salutationCode, :p.fullName, :p.firstName, :p.middleName, :p.lastName, :p.mothersMaidenName, :p.nickName, :p.gender,TO_DATE(:p.dateOfBirth,'dd-MM-yyyy'), :p.placeOfBirth, :p.countryOfBirthCode, :p.primaryMobileNumber, :p.primaryEmail, :p.maritalStatus, :p.status, :p.countryOfResidenceCode,:p.residencyTypeCode,:p.educationTypeCode, :po.professionCode, :po.professionTypeCode, :p.jobPositionTypeCode,:po.industryTypeCode, :p.nationalityCode,:po.annualIncome, :po.annualTurnover, :po.taxId, TO_DATE(:po.dateOfInCorporation,'dd-MM-yyyy'), :p.staffCode,:po.companyTypeCode, :p.groupCode, :p.portfolioCode,:p.segmentCode, :p.relationTypeCode, :p.referralCode,:p.promoCode,:p.nationalIdTypeCode,:p.nationalId,:p.amlRisk,TO_DATE(:p.amlRiskEvalDate,'dd-MM-yyyy'),:p.amlCheckStatus,:p.isStaff,now(),:p.createdBy,:p.createdByChannel);")
    @GetGeneratedKeys
    Long savePartyWithPartyIdentifier(@BindBean("p") CreatePartyCmd createPartyCmd, @BindBean("po") CreateOccupationDetailCmd createOccupationDetailCmd);

    @SqlQuery("SELECT * FROM party_details_view WHERE party_id= :partyId")
    @RegisterBeanMapper(PartyDetailsView.class)
    List<PartyDetailsView> findPartyDetail(@Bind("partyId") Long partyId);

    @SqlUpdate("""
            UPDATE party SET
            type= CASE WHEN :p.type IS NOT NULL THEN :p.type ELSE type END,
            salutation_code= CASE WHEN :p.salutationCode IS NOT NULL THEN :p.salutationCode ELSE salutation_code END,
            full_name= CASE WHEN :p.fullName IS NOT NULL THEN :p.fullName ELSE full_name END,
            first_name= CASE WHEN :p.firstName IS NOT NULL THEN :p.firstName ELSE first_name END,
            middle_name= CASE WHEN :p.middleName IS NOT NULL THEN :p.middleName ELSE middle_name END,
            last_name= CASE WHEN :p.lastName IS NOT NULL THEN :p.lastName ELSE last_name END,
            mother_maiden_name= CASE WHEN :p.mothersMaidenName IS NOT NULL THEN :p.mothersMaidenName ELSE mother_maiden_name END,
            nick_name= CASE WHEN :p.nickName IS NOT NULL THEN :p.nickName ELSE nick_name END,
            gender= CASE WHEN :p.gender IS NOT NULL THEN :p.gender ELSE gender END,
            date_of_birth= CASE WHEN :p.dateOfBirth IS NOT NULL THEN TO_DATE(:p.dateOfBirth,'dd-MM-yyyy') ELSE date_of_birth END,
            status= CASE WHEN :p.status IS NOT NULL THEN :p.status ELSE status END,
            place_of_birth= CASE WHEN :p.placeOfBirth IS NOT NULL THEN :p.placeOfBirth ELSE place_of_birth END,
            country_birth_code= CASE WHEN :p.countryOfBirthCode IS NOT NULL THEN :p.countryOfBirthCode ELSE country_birth_code END,
            primary_mobile_number= CASE WHEN :p.primaryMobileNumber IS NOT NULL THEN :p.primaryMobileNumber ELSE primary_mobile_number END,
            primary_email= CASE WHEN :p.primaryEmail IS NOT NULL THEN :p.primaryEmail ELSE primary_email END,
            marital_status= CASE WHEN :p.maritalStatus IS NOT NULL THEN :p.maritalStatus ELSE marital_status END,
            country_residence_code= CASE WHEN :p.countryOfResidenceCode IS NOT NULL THEN :p.countryOfResidenceCode ELSE country_residence_code END,
            residency_type_code= CASE WHEN :p.residencyTypeCode IS NOT NULL THEN :p.residencyTypeCode ELSE residency_type_code END,
            education_type_code= CASE WHEN :p.educationTypeCode IS NOT NULL THEN :p.educationTypeCode ELSE education_type_code END,
            profession_code= CASE WHEN :po.professionCode IS NOT NULL THEN :po.professionCode ELSE profession_code END,
            profession_type_code= CASE WHEN :po.professionTypeCode IS NOT NULL THEN :po.professionTypeCode ELSE profession_type_code END,
            job_position_type_code= CASE WHEN :p.jobPositionTypeCode IS NOT NULL THEN :p.jobPositionTypeCode ELSE job_position_type_code END,
            industry_type_code= CASE WHEN :po.industryTypeCode IS NOT NULL THEN :po.industryTypeCode ELSE industry_type_code END,
            nationality_code= CASE WHEN :p.nationalityCode IS NOT NULL THEN :p.nationalityCode ELSE nationality_code END,
            annual_income= CASE WHEN :po.annualIncome IS NOT NULL THEN :po.annualIncome ELSE annual_income END,
            annual_turnover= CASE WHEN :po.annualTurnover IS NOT NULL THEN :po.annualTurnover ELSE annual_turnover END,
            tax_id= CASE WHEN :po.taxId IS NOT NULL THEN :po.taxId ELSE tax_id END,
            date_of_incorp= CASE WHEN :po.dateOfInCorporation IS NOT NULL THEN TO_DATE(:po.dateOfInCorporation,'dd-MM-yyyy') ELSE date_of_incorp END,
            staff_code= CASE WHEN :p.staffCode IS NOT NULL THEN :p.staffCode ELSE staff_code END,
            company_code= CASE WHEN :po.companyTypeCode IS NOT NULL THEN :po.companyTypeCode ELSE company_code END,
            group_code= CASE WHEN :p.groupCode IS NOT NULL THEN :p.groupCode ELSE group_code END,
            portfolio_code= CASE WHEN :p.portfolioCode IS NOT NULL THEN :p.portfolioCode ELSE portfolio_code END,
            segment_type_code= CASE WHEN :p.segmentCode IS NOT NULL THEN :p.segmentCode ELSE segment_type_code END,
            relation_type_code= CASE WHEN :p.relationTypeCode IS NOT NULL THEN :p.relationTypeCode ELSE relation_type_code END,
            refferal_code= CASE WHEN :p.referralCode IS NOT NULL THEN :p.referralCode ELSE refferal_code END,
            promo_code= CASE WHEN :p.promoCode IS NOT NULL THEN :p.promoCode ELSE promo_code END,
            national_id_type_code= CASE WHEN :p.nationalIdTypeCode IS NOT NULL THEN :p.nationalIdTypeCode ELSE national_id_type_code END,
            is_staff= CASE WHEN :p.isStaff IS NOT NULL THEN :p.isStaff ELSE is_staff END,
            aml_risk= CASE WHEN :p.amlRisk IS NOT NULL THEN :p.amlRisk ELSE aml_risk END,
            aml_risk_eval_date= CASE WHEN :p.amlRiskEvalDate IS NOT NULL THEN TO_DATE(:p.amlRiskEvalDate,'dd-MM-yyyy') ELSE aml_risk_eval_date END,
            aml_check_status= CASE WHEN :p.amlCheckStatus IS NOT NULL THEN :p.amlCheckStatus ELSE aml_check_status END,
            is_insolvency= CASE WHEN :pf.isSolvency IS NOT NULL THEN :pf.isSolvency ELSE is_insolvency END,
            is_deceased= CASE WHEN :pf.isDeceased IS NOT NULL THEN :pf.isDeceased ELSE is_deceased END,
            is_npa= CASE WHEN :pf.isNpa IS NOT NULL THEN :pf.isNpa ELSE is_npa END,
            is_willful_defaulter= CASE WHEN :pf.isWillFullDefaulter IS NOT NULL THEN :pf.isWillFullDefaulter ELSE is_willful_defaulter END,
            willful_defaulter_date= CASE WHEN :pf.willFullDefaulterDate IS NOT NULL THEN TO_DATE(:pf.willFullDefaulterDate,'dd-MM-yyyy') ELSE willful_defaulter_date END,
            is_loan_overdue= CASE WHEN :pf.isLoanOverDue IS NOT NULL THEN :pf.isLoanOverDue ELSE is_loan_overdue END,
            is_suit_failed= CASE WHEN :pf.isSuitFiled IS NOT NULL THEN :pf.isSuitFiled ELSE is_suit_failed END,
            is_pep= CASE WHEN :pf.isPoliticallyExposed IS NOT NULL THEN :pf.isPoliticallyExposed ELSE is_pep END,
            is_fatca_applicable= CASE WHEN :pf.isFatcaApplicable IS NOT NULL THEN :pf.isFatcaApplicable ELSE is_fatca_applicable END,
            is_email_statement_reg= CASE WHEN :pf.isEmailStatementReg IS NOT NULL THEN :pf.isEmailStatementReg ELSE is_email_statement_reg END,
            is_under_watchlist= CASE WHEN :pf.isUnderWatchList IS NOT NULL THEN :pf.isUnderWatchList ELSE is_under_watchlist END,
            updated_at= :timestamp ,
            updated_by= :user ,
            updated_by_channel= CASE WHEN :p.updatedByChannel IS NOT NULL THEN :p.updatedByChannel ELSE updated_by_channel END
            WHERE party_id= :partyId
            """)
    boolean updateParty(@Bind("partyId") Long partyId, @BindBean("p") UpdatePartyCmd updatePartyCmd, @BindBean("po") UpdateOccupationDetailCmd updateOccupationDetailCmd, @Bind("timestamp") LocalDateTime timestamp, @Bind("user") String user, @BindBean("pf") UpdatePartyFlagCmd flagCmd);

    @SqlBatch("INSERT INTO party_address(party_address_id, party_id, address_type_code, address_line1, "
            + " address_line2, address_line3, zip_code,is_default, ward_code, district_code, city_code, country_code, document_id)"
            + " VALUES (nextval('party_address_id_seq'), :partyId, :pa.addressTypeCode, :pa.addressLine1,:pa.addressLine2, :pa.addressLine3,:pa.cityZipCode,"
            + " :pa.isDefault, :pa.wardCode, :pa.districtCode, :pa.cityCode, :pa.countryCode,:pa.documentId)")
    boolean[] saveAddress(@BindBean("pa") Set<CreateAddressCmd> createAddressCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE party_address SET
            address_type_code= CASE WHEN :pa.addressTypeCode IS NOT NULL THEN :pa.addressTypeCode ELSE address_type_code END,
            address_line1= CASE WHEN :pa.addressLine1 IS NOT NULL THEN :pa.addressLine1 ELSE address_line1 END,
            address_line2= CASE WHEN :pa.addressLine2 IS NOT NULL THEN :pa.addressLine2 ELSE address_line2 END,
            address_line3= CASE WHEN :pa.addressLine3 IS NOT NULL THEN :pa.addressLine3 ELSE address_line3 END,
            zip_code= CASE WHEN :pa.cityZipCode IS NOT NULL THEN :pa.cityZipCode ELSE zip_code END,
            is_default= CASE WHEN :pa.isDefault IS NOT NULL THEN :pa.isDefault ELSE is_default END,
            ward_code= CASE WHEN :pa.wardCode IS NOT NULL THEN :pa.wardCode ELSE ward_code END,
            district_code= CASE WHEN :pa.districtCode IS NOT NULL THEN :pa.districtCode ELSE district_code END,
            city_code= CASE WHEN :pa.cityCode IS NOT NULL THEN :pa.cityCode ELSE city_code END,
            country_code= CASE WHEN :pa.countryCode IS NOT NULL THEN :pa.countryCode ELSE country_code END,
            document_id= CASE WHEN :pa.documentId IS NOT NULL THEN :pa.documentId ELSE document_id END
            WHERE party_address_id= :pa.partyAddressId AND party_id=:partyId
            """)
    boolean[] updateAddress(@BindBean("pa") Set<UpdateAddressCmd> updateAddressCmds, @Bind("partyId") Long partyId);

    @SqlBatch("INSERT INTO party_contact_details(party_contact_details_id, party_id, contact_type_code, contact_value, isd_code,is_primary, is_verified, verified_mode, last_verified_date, is_dnd ) "
            + " VALUES (nextval('party_contact_details_id_seq'),:partyId, :pc.contactTypeCode, :pc.contactValue,:pc.isdCode,:pc.isPrimary, :pc.isVerified, :pc.verifiedMode, TO_DATE(:pc.lastVerifiedDate,'dd-MM-yyyy'), :pc.isDnd)")
    boolean[] saveContactDetails(@BindBean("pc") Set<CreateContactDetailsCmd> createContactDetailsCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE party_contact_details SET
            contact_type_code= CASE WHEN :pc.contactTypeCode IS NOT NULL THEN :pc.contactTypeCode ELSE contact_type_code END,
            contact_value= CASE WHEN :pc.contactValue IS NOT NULL THEN :pc.contactValue ELSE contact_value END,
            isd_code= CASE WHEN :pc.isdCode IS NOT NULL THEN :pc.isdCode ELSE isd_code END,
            is_primary= CASE WHEN :pc.isPrimary IS NOT NULL THEN :pc.isPrimary ELSE is_primary END,
            is_verified= CASE WHEN :pc.isVerified IS NOT NULL THEN :pc.isVerified ELSE is_verified END,
            verified_mode= CASE WHEN :pc.verifiedMode IS NOT NULL THEN :pc.verifiedMode ELSE verified_mode END,
            last_verified_date= CASE WHEN :pc.lastVerifiedDate IS NOT NULL THEN TO_DATE(:pc.lastVerifiedDate,'dd-MM-yyyy') ELSE last_verified_date END,
            is_dnd= CASE WHEN :pc.isDnd IS NOT NULL THEN :pc.isDnd ELSE is_dnd END
            WHERE party_contact_details_id=:pc.partyContactDetailsId AND party_id=:partyId          
            """)
    boolean[] updateContactDetails(@BindBean("pc") Set<UpdateContactDetailsCmd> updateContactDetailsCmds, @Bind("partyId") Long partyId);

    @SqlBatch("INSERT INTO party_asset(party_asset_id, party_id, asset_type_code, asset_name,potential_value, is_mortgaged)"
            + " VALUES (nextval('party_asset_id_seq'), :partyId, :asset.assetTypeCode, :asset.assetName, :asset.potentialValue, :asset.isMortgaged)")
    boolean[] saveAsset(@BindBean("asset") Set<CreateAssetsCmd> createAssetsCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE party_asset SET
            asset_type_code= CASE WHEN :asset.assetTypeCode IS NOT NULL THEN :asset.assetTypeCode ELSE asset_type_code END,
            asset_name= CASE WHEN :asset.assetName IS NOT NULL THEN :asset.assetName ELSE asset_name END,
            potential_value= CASE WHEN :asset.potentialValue IS NOT NULL THEN :asset.potentialValue ELSE potential_value END,
            is_mortgaged= CASE WHEN :asset.isMortgaged IS NOT NULL THEN :asset.isMortgaged ELSE is_mortgaged END
            WHERE party_asset_id=:asset.partyAssetId AND party_id=:partyId
            """)
    boolean[] updateAsset(@BindBean("asset") Set<UpdateAssetsCmd> updateAssetsCmds, @Bind("partyId") Long partyId);

    @SqlBatch("INSERT INTO party_risk(party_risk_id, party_id, risk_type_code, risk_score, evaluation_date, valid_until)\n"
            + "\tVALUES (nextval('party_risk_id_seq'), :partyId, :risk.riskTypeCode, :risk.riskScore, TO_DATE(:risk.evaluationDate,'dd-MM-yyyy'), TO_DATE(:risk.validUntil,'dd-MM-yyyy'))")
    boolean[] saveRisks(@BindBean("risk") Set<CreateRisksCmd> createRisksCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE party_risk SET
            risk_type_code= CASE WHEN :risk.riskTypeCode IS NOT NULL THEN :risk.riskTypeCode ELSE risk_type_code END,
            risk_score= CASE WHEN :risk.riskScore IS NOT NULL THEN :risk.riskScore ELSE risk_score END,
            evaluation_date= CASE WHEN :risk.evaluationDate IS NOT NULL THEN TO_DATE(:risk.evaluationDate,'dd-MM-yyyy') ELSE evaluation_date END,
            valid_until= CASE WHEN :risk.validUntil IS NOT NULL THEN TO_DATE(:risk.validUntil,'dd-MM-yyyy') ELSE valid_until END
            WHERE party_risk_id=:risk.partyRiskId AND party_id=:partyId
            """)
    boolean[] updateRisks(@BindBean("risk") Set<UpdateRisksCmd> updateRisksCmds, @Bind("partyId") Long partyId);

    @SqlBatch("INSERT INTO party_memo(party_memo_id, party_id, memo_type_code, severity, risk_score, valid_from, valid_until)\n"
            + "VALUES (nextval('party_memo_id_seq'),:partyId, :pm.memoTypeCode, :pm.severity, :pm.score, TO_DATE(:pm.validFrom,'dd-MM-yyyy'), TO_DATE(:pm.validTo,'dd-MM-yyyy'))")
    boolean[] saveMemo(@BindBean("pm") Set<CreateMemosCmd> createMemosCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE party_memo SET 
            memo_type_code= CASE WHEN :pm.memoTypeCode IS NOT NULL THEN :pm.memoTypeCode ELSE memo_type_code END,
            severity= CASE WHEN :pm.severity IS NOT NULL THEN :pm.severity ELSE severity END,
            risk_score= CASE WHEN :pm.score IS NOT NULL THEN :pm.score ELSE risk_score END,
            valid_from= CASE WHEN :pm.validFrom IS NOT NULL THEN TO_DATE(:pm.validFrom,'dd-MM-yyyy') ELSE valid_from END,
            valid_until= CASE WHEN :pm.validTo IS NOT NULL THEN TO_DATE(:pm.validTo,'dd-MM-yyyy') ELSE valid_until END
            WHERE party_memo_id=:pm.partyMemoId AND party_id=:partyId
            """)
    boolean[] updateMemo(@BindBean("pm") Set<UpdateMemosCmd> updateMemosCmds, @Bind("partyId") Long partyId);

    @SqlBatch("INSERT INTO party_fatca_details(party_fatca_details_id, party_id, place_of_incorporation, country_of_incorporation, country_of_residence, incorporation_number, board_rel_number, report_bl_number, original_report_bl_number, fatca_tax_id, document_reference_id)\n"
            + "VALUES (nextval('party_fatca_details_id_seq'),:partyId, :fd.placeOfIncorporation, :fd.countryOfIncorporation, :fd.countryOfResidence, :fd.incorporationNumber, :fd.boardRelNumber, :fd.reportBlNumber, :fd.originalReportBlNumber, :fd.fatcaTaxId, :fd.documentReferenceId)")
    boolean[] saveFatcaDetails(@BindBean("fd") Set<CreateFatcaDetailsCmd> createFatcaDetailsCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE  party_fatca_details SET 
            place_of_incorporation= CASE WHEN :pf.placeOfIncorporation IS NOT NULL THEN :pf.placeOfIncorporation ELSE place_of_incorporation END,
            country_of_incorporation= CASE WHEN :pf.countryOfIncorporation IS NOT NULL THEN :pf.countryOfIncorporation ELSE country_of_incorporation END,
            country_of_residence= CASE WHEN :pf.countryOfResidence IS NOT NULL THEN :pf.countryOfResidence ELSE country_of_residence END,
            incorporation_number= CASE WHEN :pf.incorporationNumber IS NOT NULL THEN :pf.incorporationNumber ELSE incorporation_number END,
            board_rel_number= CASE WHEN :pf.boardRelNumber IS NOT NULL THEN :pf.boardRelNumber ELSE board_rel_number END,
            report_bl_number= CASE WHEN :pf.reportBlNumber IS NOT NULL THEN :pf.reportBlNumber ELSE report_bl_number END,
            original_report_bl_number= CASE WHEN :pf.originalReportBlNumber IS NOT NULL THEN :pf.originalReportBlNumber ELSE original_report_bl_number END,
            fatca_tax_id = CASE WHEN :pf.fatcaTaxId IS NOT NULL THEN :pf.fatcaTaxId ELSE fatca_tax_id END,
            document_reference_id = CASE WHEN :pf.documentReferenceId IS NOT NULL THEN :pf.documentReferenceId ELSE document_reference_id END
            WHERE party_id=:partyId AND party_fatca_details_id=:pf.partyFatcaDetailsId
            """)
    boolean[] updateFatcaDetails(@BindBean("pf") Set<UpdateFatcaDetailsCmd> updateFatcaDetailsCmd, @Bind("partyId") Long partyId);

    @SqlBatch("INSERT INTO party_document(party_document_id, party_id, document_type_code, document_number, document_number_masked, document_number_token, issuing_date, expiry_date, issuing_place, issuing_country_code, is_poi,is_poa,dms_reference_id,verification_status,additional_data)\n"
            + "VALUES (nextval('party_document_id_seq'),:partyId, :pd.documentTypeCode, :pd.documentNumber, :pd.documentNumberMasked, :pd.documentNumberToken, TO_DATE(:pd.issuingDate,'dd-MM-yyyy'), TO_DATE(:pd.expiryDate,'dd-MM-yyyy'), :pd.issuingPlace, :pd.issuingCountryCode, :pd.isPoi , :pd.isPoa, :pd.dmsReferenceId, :pd.verificationStatus, :pd.additionalData)")
    boolean[] saveDocument(@BindBean("pd") Set<CreateDocumentCmd> createDocumentCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE party_document SET
            document_number= CASE WHEN :pd.documentNumber IS NOT NULL THEN :pd.documentNumber ELSE document_number END,
            document_number_masked= CASE WHEN :pd.documentNumberMasked IS NOT NULL THEN :pd.documentNumberMasked ELSE document_number_masked END,
            document_number_token= CASE WHEN :pd.documentNumberToken IS NOT NULL THEN :pd.documentNumberToken ELSE document_number_token END,
            issuing_date= CASE WHEN :pd.issuingDate IS NOT NULL THEN TO_DATE(:pd.issuingDate,'dd-MM-yyyy') ELSE issuing_date END,
            expiry_date= CASE WHEN :pd.expiryDate IS NOT NULL THEN TO_DATE(:pd.expiryDate,'dd-MM-yyyy') ELSE expiry_date END,
            issuing_place= CASE WHEN :pd.issuingPlace IS NOT NULL THEN :pd.issuingPlace ELSE issuing_place END,
            issuing_country_code= CASE WHEN :pd.issuingCountryCode IS NOT NULL THEN :pd.issuingCountryCode ELSE issuing_country_code END,
            is_poi= CASE WHEN :pd.isPoi IS NOT NULL THEN :pd.isPoi ELSE is_poi END,
            is_poa= CASE WHEN :pd.isPoa IS NOT NULL THEN :pd.isPoa ELSE is_poa END,
            dms_reference_id= CASE WHEN :pd.dmsReferenceId IS NOT NULL THEN :pd.dmsReferenceId ELSE dms_reference_id END,
            verification_status= CASE WHEN :pd.verificationStatus IS NOT NULL THEN :pd.verificationStatus ELSE verification_status END,
            additional_data= CASE WHEN :pd.additionalData IS NOT NULL THEN :pd.additionalData ELSE additional_data END
            WHERE party_id=:partyId AND party_document_id=:pd.partyDocumentId
            """)
    boolean[] updateDocument(@BindBean("pd") Set<UpdateDocumentsCmd> updateDocumentsCmds, @Bind("partyId") Long partyId);

    @SqlQuery("SELECT party_id FROM party WHERE party_id= :partyId")
    Long findIdByPartyId(@Bind("partyId") long partyId);

    @SqlUpdate("UPDATE party SET is_deleted=true WHERE party_id=:partyId")
    boolean updatePartyIsDeletedFlagByPartyId(@Bind("partyId") long partyId);

    @SqlQuery("SELECT * FROM party_address_view WHERE party_id= :partyId  AND party_address_id= CASE WHEN :addressId IS NOT NULL THEN :addressId ELSE party_address_id END ORDER BY party_address_id")
    @RegisterBeanMapper(ViewAddressCmd.class)
    Set<ViewAddressCmd> findAddressDetailsByIds(@Bind("partyId") Long partyId, @Bind("addressId") Long addressId);

    @SqlQuery("Select party_id FROM check_dedupe(:d.countryCode, :d.mobileNumber,:d.fullName,TO_DATE(:d.dateOfBirth,'dd-MM-yyyy'),:d.nationalIdTypeCode,:d.nationalId,:d.nationalityCode)")
    List<Long> findDedupeDetails(@BindBean("d") DedupeFieldRequestCmd dedupeFieldRequestCmd);

    @SqlQuery("""
            SELECT DISTINCT p.party_id , p.party_identifier,p.full_name,p.first_name,p.middle_name,p.last_name,m.description AS nationality,
            p.primary_mobile_number AS mobileNumber,p.primary_email AS emailId,DATE(p.date_of_birth) AS date_of_birth,
            DATE(p.created_at) as onBoardingDate,p.aml_risk as customerRiskCategory  FROM PARTY p
            left join LOOKUP_MASTER m   ON p.nationality_code::text = m.code::text AND m.type::text = 'NATIONALITY'::text AND m.is_active = true 
            LEFT JOIN party_contact_details pc ON p.party_id = pc.party_id 
            WHERE ((:partyId is null or :partyId=p.party_id) 
            and  (:partySearchData.firstName is null or :partySearchData.firstName=p.first_name)
            and (:partySearchData.dateOfBirth is null or TO_TIMESTAMP(:partySearchData.dateOfBirth,'dd-MM-yyyy')= p.date_of_birth)
            and (:partySearchData.lastName is null or :partySearchData.lastName=p.last_name)) 
            and ((:partySearchData.mobileNumber is null or :partySearchData.mobileNumber=p.primary_mobile_number)
            or  (:partySearchData.mobileNumber is null or :partySearchData.mobileNumber=pc.contact_value)) 
            and ((:partySearchData.emailId is null or :partySearchData.emailId=p.primary_email)
            or (:partySearchData.emailId is null or :partySearchData.emailId=pc.contact_value))
            and p.is_deleted= :isDelete
            ORDER BY <orderByField> <sortBy> LIMIT  :limit OFFSET :offSet 
            """)
    @RegisterBeanMapper(PartyRecordCmd.class)
    List<PartyRecordCmd> fetchPartyDetailsByPassedParameter(
            @Bind("partyId") Long partyId,
            @BindBean("partySearchData") PartyDistinctiveSearchCmd partySearchData,
            @Define("orderByField") String orderByField,
            @Define("sortBy") String sortBy,
            @Bind("limit") int limit,
            @Bind("offSet") int offSet,
            @Bind("isDelete") boolean isDelete);

    @SqlQuery("SELECT party_id FROM party WHERE party_id NOT IN(:partyId) AND primary_mobile_number = :mobileNumber ")
    Long findPartyIdByPartyIdAndMobileNumber(@Bind("partyId") long partyId, @Bind("mobileNumber") String mobileNumber);

    @SqlUpdate("DELETE FROM party_address WHERE party_id= :partyId and party_address_id= :partyAddressId")
    boolean deleteAddressByPassedParameter(@Bind("partyId") Long partyId, @Bind("partyAddressId") long partyAddressId);


    @SqlQuery("SELECT party_contact_details_id, party_id, contact_type_code, contact_type, contact_value, isd_code, is_primary, is_verified, verified_mode, to_char(last_verified_date,'DD-MM-YYYY') AS last_verified_date, is_dnd FROM party_contact_view WHERE party_id= :partyId  AND party_contact_details_id= CASE WHEN :contactId IS NOT NULL THEN :contactId ELSE party_contact_details_id END")
    @RegisterBeanMapper(ViewContactDetailsCmd.class)
    Set<ViewContactDetailsCmd> findContactDetailsById(@Bind("partyId") Long partyId, @Bind("contactId") Long contactId);

    @SqlUpdate("DELETE FROM party_contact_details WHERE party_id= :partyId and party_contact_details_id= :contactId")
    boolean deleteContact(@Bind("partyId") Long partyId, @Bind("contactId") Long contactId);

    @SqlUpdate("DELETE FROM party_document WHERE party_id= :partyId and party_document_id= :documentId")
    boolean deleteDocument(@Bind("partyId") Long partyId, @Bind("documentId") Long documentId);

    @SqlQuery("SELECT party_document_id, party_id, document_type_code, document_type, document_number, document_number_masked, document_number_token, to_char(issuing_date,'DD-MM-YYYY') AS issuing_date, to_char(expiry_date,'DD-MM-YYYY') AS expiry_date, issuing_place, issuing_country_code, issuing_country, is_poi, is_poa, dms_reference_id, verification_status, additional_data FROM party_document_view WHERE party_id= :partyId  AND party_document_id= CASE WHEN :documentId IS NOT NULL THEN :documentId ELSE party_document_id END")
    @RegisterBeanMapper(ViewDocumentsCmd.class)
    Set<ViewDocumentsCmd> findDocumentById(@Bind("partyId") Long partyId, @Bind("documentId") Long partyDocumentsId);


    @SqlBatch("INSERT INTO party_relation(party_relation_id, party_id, secondary_party_id, party_relation_type_code, inv_relation) VALUES (nextval('party_relation_id_seq'),:partyId, :pr.secondaryPartyId, :pr.partyRelationTypeCode, :pr.invRelation)")
    boolean[] saveRelation(@BindBean("pr") Set<CreateRelationsCmd> partyRelationCmds, @Bind("partyId") Long partyId);


    @SqlBatch("INSERT INTO party_xref(party_xref_id, party_id, system_code, xref_id) VALUES (nextval('party_xref_id_seq'),:partyId, :px.systemCode, :px.xrefId)")
    boolean[] saveXref(@BindBean("px") Set<CreateXrefsCmd> createXrefsCmds, @Bind("partyId") Long partyId);

    @SqlBatch("INSERT INTO party_guardian(party_guardian_id, party_id, guardian_first_name, guardian_middle_name, guardian_last_name, guardian_relation, guardian_address_line1, guardian_address_line2, guardian_address_line3, guardian_ward_code, guardian_city_code, guardian_district_code) VALUES (nextval('party_guardian_id_seq'),:partyId, :pg.guardianFirstName, :pg.guardianMiddleName, :pg.guardianLastName, :pg.guardianRelation, :pg.guardianAddressLine1, :pg.guardianAddressLine2, :pg.guardianAddressLine3, :pg.guardianWardCode, :pg.guardianCityCode, :pg.guardianDistrictCode)")
    boolean[] saveGuardian(@BindBean("pg") Set<CreateGuardianCmd> createGuardianCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE party_relation SET
            secondary_party_id= CASE WHEN :r.secondaryPartyId IS NOT NULL THEN :r.secondaryPartyId ELSE secondary_party_id END,
            party_relation_type_code= CASE WHEN :r.partyRelationTypeCode IS NOT NULL THEN :r.partyRelationTypeCode ELSE party_relation_type_code END,
            inv_relation= CASE WHEN :r.invRelation IS NOT NULL THEN :r.invRelation ELSE inv_relation END
            WHERE party_relation_id=:r.partyRelationId AND party_id=:partyId
            """)
    boolean[] updateRelation(@BindBean("r") Set<UpdateRelationsCmd> updateRelationsCmds, @Bind("partyId") Long partyId);


    @SqlBatch("""
            UPDATE party_xref SET
            system_code= CASE WHEN :x.systemCode IS NOT NULL THEN :x.systemCode ELSE system_code END,
            xref_id= CASE WHEN :x.xrefId IS NOT NULL THEN :x.xrefId ELSE xref_id END
            WHERE party_xref_id=:x.partyXrefId AND party_id=:partyId
            """)
    boolean[] updateXref(@BindBean("x") Set<UpdateXrefsCmd> updateXrefsCmds, @Bind("partyId") Long partyId);

    @SqlBatch("""
            UPDATE party_guardian SET
            guardian_first_name= CASE WHEN :g.guardianFirstName IS NOT NULL THEN :g.guardianFirstName ELSE guardian_first_name END,
            guardian_middle_name= CASE WHEN :g.guardianMiddleName IS NOT NULL THEN :g.guardianMiddleName ELSE guardian_middle_name END,
            guardian_last_name= CASE WHEN :g.guardianLastName IS NOT NULL THEN :g.guardianLastName ELSE guardian_last_name END,
            guardian_relation= CASE WHEN :g.guardianRelation IS NOT NULL THEN :g.guardianRelation ELSE guardian_relation END,
            guardian_address_line1= CASE WHEN :g.guardianAddressLine1 IS NOT NULL THEN :g.guardianAddressLine1 ELSE guardian_address_line1 END,
            guardian_address_line2= CASE WHEN :g.guardianAddressLine2 IS NOT NULL THEN :g.guardianAddressLine2 ELSE guardian_address_line2 END,
            guardian_address_line3= CASE WHEN :g.guardianAddressLine3 IS NOT NULL THEN :g.guardianAddressLine3 ELSE guardian_address_line3 END,
            guardian_ward_code= CASE WHEN :g.guardianWardCode IS NOT NULL THEN :g.guardianWardCode ELSE guardian_ward_code END,
            guardian_district_code= CASE WHEN :g.guardianDistrictCode IS NOT NULL THEN :g.guardianDistrictCode ELSE guardian_district_code END,
            guardian_city_code= CASE WHEN :g.guardianCityCode IS NOT NULL THEN :g.guardianCityCode ELSE guardian_city_code END
            WHERE party_guardian_id=:g.partyGuardianId AND party_id=:partyId
            """)
    boolean[] updateGuardian(@BindBean("g") Set<UpdateGuardiansCmd> updateGuardiansCmds, @Bind("partyId") Long partyId);

    @SqlQuery("SELECT party_risk_id, party_id, risk_type_code, risk_type, risk_score,to_char(evaluation_date,'DD-MM-YYYY') AS evaluation_date, to_char(valid_until,'DD-MM-YYYY') AS valid_until FROM party_risk_view WHERE party_id= :partyId  AND party_risk_id= CASE WHEN :riskId IS NOT NULL THEN :riskId ELSE party_risk_id END")
    @RegisterBeanMapper(ViewRisksCmd.class)
    Set<ViewRisksCmd> findRiskById(@Bind("partyId") Long partyId, @Bind("riskId") Long partyRiskId);

    @SqlQuery("SELECT * FROM party_fatca_details_view WHERE party_id= :partyId  AND party_fatca_details_id= CASE WHEN :fatcaId IS NOT NULL THEN :fatcaId ELSE party_fatca_details_id END")
    @RegisterBeanMapper(ViewFatcaDetailsCmd.class)
    Set<ViewFatcaDetailsCmd> findFatcaById(@Bind("partyId") Long partyId, @Bind("fatcaId") Long partyFatcaId);

    @SqlQuery("SELECT party_memo_id, party_id, memo_type_code, memo_type, severity, score, to_char(valid_from,'DD-MM-YYYY') AS valid_from, to_char(valid_to,'DD-MM-YYYY') AS valid_to FROM party_memo_view WHERE party_id= :partyId  AND party_memo_id= CASE WHEN :memoId IS NOT NULL THEN :memoId ELSE party_memo_id END")
    @RegisterBeanMapper(ViewMemosCmd.class)
    Set<ViewMemosCmd> findMemoById(@Bind("partyId") Long partyId, @Bind("memoId") Long partyMemoId);

    @SqlQuery("SELECT description FROM lookup_master WHERE code= :code  AND is_active =true AND type= 'CONTACT_TYPE' ")
    String findTypeValueByCode(@Bind("code") String typeCode);

    @SqlQuery("SELECT party_id FROM party WHERE party_identifier= :partyIdentifier")
    Long findIdByPartyIdentifier(@Bind("partyIdentifier") String partyIdentifier);

    @SqlQuery("SELECT * FROM party_xref_view WHERE party_id= :partyId")
    @RegisterBeanMapper(ViewXrefsCmd.class)
    Set<ViewXrefsCmd> findXref(@Bind("partyId") Long partyId);

    @SqlQuery("SELECT * FROM party where updated_at IS NULL limit :batchSize")
    @RegisterBeanMapper(Party.class)
    List<Party> findPartyDetailByLimit(@Bind("batchSize") Integer batchSize);

    @SqlQuery("SELECT * FROM party_address where party_id= :partyId")
    @RegisterBeanMapper(UpdateAddressCmd.class)
    Set<UpdateAddressCmd> findPartyAddressByPartyId(@Bind("partyId") Long partyId);

    @SqlQuery("SELECT * FROM party_contact_details where party_id= :partyId")
    @RegisterBeanMapper(UpdateContactDetailsCmd.class)
    Set<UpdateContactDetailsCmd> findContactDetailsByPartyId(@Bind("partyId") Long partyId);

    @SqlQuery("SELECT party_guardian_id, party_id, guardian_first_name, guardian_middle_name, guardian_last_name, guardian_relation, guardian_address_line1, guardian_address_line2, guardian_address_line3, guardian_ward_code, guardian_city_code, guardian_district_code, guardian_ward, guardian_district, guardian_city FROM party_guardian_view WHERE party_id= :partyId  AND party_guardian_id= CASE WHEN :guardianId IS NOT NULL THEN :guardianId ELSE party_guardian_id END")
    @RegisterBeanMapper(ViewGuardiansCmd.class)
    Set<ViewGuardiansCmd> findGuardianById(@Bind("partyId") Long partyId, @Bind("guardianId") Long partyGuardianId);

    @SqlUpdate("DELETE FROM party_guardian WHERE party_id= :partyId and party_guardian_id= :guardianId")
    boolean deleteGuardian(@Bind("partyId") Long partyId, @Bind("guardianId") Long partyGuardianId);

    @SqlQuery("SELECT * FROM address_master")
    @RegisterBeanMapper(AddressMaster.class)
    List<AddressMaster> findAllAddressMasterDetails();

    @SqlBatch("INSERT INTO address_master(address_master_id, type, code, description, parent_type, parent_code) VALUES (nextval('address_master_id_seq'),:am.type, :am.code, :am.description, :am.parentType, :am.parentCode)")
    boolean[] saveAddressMaster(@BindBean("am") List<AddressMaster> addressMasterDetails);

    @SqlQuery("SELECT * FROM country_master")
    @RegisterBeanMapper(CountryMaster.class)
    List<CountryMaster> findAllCountryMasterDetails();

    @SqlBatch("INSERT INTO country_master(country_master_id, code, description, isd_code, iso_code) VALUES (nextval('country_master_id_seq'), :cm.code, :cm.description, :cm.isdCode, :cm.isoCode)")
    boolean[] saveCountryMaster(@BindBean("cm") List<CountryMaster> countryMasterDetails);

    @SqlQuery("SELECT * FROM lookup_master")
    @RegisterBeanMapper(LookupMaster.class)
    List<LookupMaster> findAllLookupMasterDetails();

    @SqlBatch("INSERT INTO lookup_master(lookup_master_id, type, code, description) VALUES (nextval('lookup_master_id_seq'), :lm.type, :lm.code, :lm.description)")
    boolean[] saveLookupMaster(@BindBean("lm") List<LookupMaster> lookupMasterDetails);

}
