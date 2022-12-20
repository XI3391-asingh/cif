package com.finx.customerservice.db;

import com.finx.customerservice.api.OccupationDetailCmd;
import com.finx.customerservice.api.PartyAddressCmd;
import com.finx.customerservice.api.PartyCmd;
import com.finx.customerservice.api.PartyFlagCmd;
import com.finx.customerservice.core.customer.domain.Party;
import com.finx.customerservice.core.customer.domain.PartyDetailsView;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Set;

public interface PartyRepository {

    @SqlQuery("SELECT party_id,first_name,middle_name,last_name,primary_email,primary_mobile_number,date_of_birth FROM party where primary_mobile_number = :mobileNumber")
    @RegisterBeanMapper(Party.class)
    List<Party> fetchPartyDetailsByMobileNumber(@Bind("mobileNumber") String mobileNumber);

    @SqlUpdate("INSERT INTO party (party_id,party_type, full_name, first_name, middle_name, last_name, gender, date_of_birth, marital_status, primary_mobile_number, primary_email, status, national_id_type_code, national_id, nationality_code, country_of_residence_code, job_position_type_code, occupation_type_code, segment_code, tax_id, aml_risk, aml_risk_eval_date, is_aml_status, is_pep, is_fatca_applicable,created_by_channel) \n"
           + " VALUES(nextval('party_id_seq'),:p.partyType, :p.fullName, :p.firstName, :p.middleName, :p.lastName, :p.gender, TO_DATE(:p.dateOfBirth,'dd-MM-yyyy'), :p.maritalStatus, :p.primaryMobileNumber, :p.primaryEmail, :p.status, :p.nationalIdTypeCode, :p.nationalId, :p.nationalityCode, :p.countryOfResidenceCode, :p.jobPositionTypeCode, :po.occupationTypeCode, :p.segmentCode, :p.taxId, :p.amlRisk, TO_DATE(:p.amlRiskEvalDate,'dd-MM-yyyy'),:pf.isAmlStatus, :pf.isPep,:pf.isFatcaApplicable,:p.createdByChannel)")
    @GetGeneratedKeys
    Long saveParty(@BindBean("p") PartyCmd partyCmd, @BindBean("po") OccupationDetailCmd occupationDetailCmd, @BindBean("pf")PartyFlagCmd partyFlagCmd);

    @SqlBatch("INSERT INTO party_address(party_address_id, party_id, address_type_code, address_line1, "
            + " address_line2, address_line3, zip_code,is_default, ward_code, district_code, city_code,country_code)"
            + " VALUES (nextval('party_address_id_seq'), :partyId, :pa.addressTypeCode, :pa.addressLine1,:pa.addressLine2, :pa.addressLine3,:pa.zipCode,"
            + " :pa.isDefault, :pa.wardCode, :pa.districtCode, :pa.cityCode,:pa.countryCode)")
    boolean[] saveAddress(@BindBean("pa") Set<PartyAddressCmd> address, @Bind("partyId") Long partyId);

    @SqlQuery("SELECT * FROM party_details_view WHERE party_id= :partyId")
    @RegisterBeanMapper(PartyDetailsView.class)
    List<PartyDetailsView> findPartyDetail(@Bind("partyId") Long partyId);
}
