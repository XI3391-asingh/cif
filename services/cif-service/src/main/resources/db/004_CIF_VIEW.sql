--changeset Manoj:1
-- View: party_details_view
CREATE OR REPLACE VIEW party_details_view  AS
  SELECT p.party_id,
    p.party_identifier,
    p.type,
    p.salutation_code,
    sc.description AS salutation,
    p.full_name,
    p.first_name,
    p.middle_name,
    p.last_name,
    p.mother_maiden_name,
    p.nick_name,
    p.gender,
    p.date_of_birth,
    p.place_of_birth,
    p.country_birth_code,
    cbc.description AS country_of_birth,
    p.primary_mobile_number,
    p.primary_email,
    p.marital_status,
    p.status,
    p.country_residence_code,
    crc.description AS country_residence,
    p.residency_type_code,
    rtc.description AS residency_type,
    p.education_type_code,
    etc.description AS education_type,
    p.profession_code,
    pro.description AS profession,
    p.profession_type_code,
    pt.description AS profession_type,
    p.job_position_type_code,
    jp.description AS job_position,
    p.industry_type_code,
    itc.description AS industry_type,
    p.nationality_code,
    nc.description AS nationality,
    p.annual_income,
    p.annual_turnover,
    p.tax_id,
    p.date_of_incorp,
    p.staff_code,
    p.company_code,
    com.description AS company,
    p.group_code,
    p.portfolio_code,
    p.segment_type_code,
    seg.description AS segment,
    p.relation_type_code,
    rt.description AS relation_type,
    p.refferal_code,
    p.promo_code,
    p.national_id_type_code,
    nit.description AS national_id_type,
    p.national_id,
    p.aml_risk_eval_date,
    p.aml_risk,
    p.aml_check_status,
    p.is_staff,
    p.is_deceased,
    p.is_insolvency,
    p.is_npa,
    p.is_willful_defaulter,
    p.willful_defaulter_date,
    p.is_loan_overdue,
    p.is_suit_failed,
    p.is_pep,
    p.is_fatca_applicable,
    p.is_email_statement_reg,
    p.is_under_watchlist,
    p.is_deleted,
    p.created_at,
    p.created_by,
    p.updated_at,
    p.updated_by,
    p.created_by_channel,
    p.updated_by_channel,
    pa.party_address_id,
    pa.address_type_code,
    a.description AS address_type,
    pa.address_line1,
    pa.address_line2,
    pa.address_line3,
    pa.zip_code,
    pa.is_default,
    pa.ward_code,
    w.description AS ward,
    pa.district_code,
    d.description AS district,
    pa.city_code,
    c.description AS city,
    pa.country_code,
    cm.description AS country_address,
    pa.document_id,
    pc.party_contact_details_id,
    pc.contact_type_code,
    con.description AS contact_type,
    pc.contact_value,
    pc.isd_code,
    pc.is_primary,
    pc.is_verified,
    pc.verified_mode,
    pc.last_verified_date,
    pc.is_dnd,
    pas.party_asset_id,
    pas.asset_type_code,
    asst.description AS asset_type,
    pas.asset_name,
    pas.potential_value,
    pas.is_mortgaged,
    prc.party_risk_id,
    prc.risk_type_code,
    ctmrtc.description AS risk_type,
    prc.risk_score,
    prc.evaluation_date,
    prc.valid_until,
    pm.party_memo_id,
    pm.memo_type_code,
    ctmpm.description AS memo_type,
    pm.severity,
    pm.risk_score AS score,
    pm.valid_from,
    pm.valid_until AS valid_to,
    pd.party_document_id,
    pd.document_type_code,
    ctmpd.description AS document_type,
    pd.document_number,
    pd.document_number_masked,
    pd.document_number_token,
    pd.issuing_date,
    pd.expiry_date,
    pd.issuing_place,
    pd.issuing_country_code,
    cmpd.description AS issuing_country,
    pd.is_poi,
    pd.is_poa,
    pd.dms_reference_id,
    pd.verification_status,
    pd.additional_data,
    prel.party_relation_id,
    prel.secondary_party_id,
    prel.party_relation_type_code,
    ctmprel.description AS party_relation_type,
    prel.inv_relation,
    px.party_xref_id,
    px.system_code,
    px.xref_id,
    fd.party_fatca_details_id,
    fd.place_of_incorporation,
    fd.country_of_incorporation,
    fd.country_of_residence,
    fd.incorporation_number,
    fd.board_rel_number,
    fd.report_bl_number,
    fd.original_report_bl_number,
    fd.fatca_tax_id,
    fd.document_reference_id,
    pg.party_guardian_id,
    pg.guardian_first_name,
    pg.guardian_middle_name,
    pg.guardian_last_name,
    pg.guardian_relation,
    pg.guardian_address_line1,
    pg.guardian_address_line2,
    pg.guardian_address_line3,
    pg.guardian_ward_code,
    pgw.description AS guardian_ward,
    pg.guardian_district_code,
    pgd.description AS guardian_district,
    pg.guardian_city_code,
    pgpr.description AS guardian_city
   FROM party p
     LEFT JOIN country_master cbc ON p.country_birth_code::text = cbc.code::text AND cbc.is_active = true
     LEFT JOIN country_master crc ON p.country_residence_code::text = crc.code::text AND crc.is_active = true
     LEFT JOIN lookup_master com ON p.company_code::text = com.code::text AND com.type::text = 'COMPANY_TYPE'::text AND com.is_active = true
     LEFT JOIN lookup_master rtc ON p.residency_type_code::text = rtc.code::text AND rtc.type::text = 'RESIDENCY_TYPE'::text AND rtc.is_active = true
     LEFT JOIN lookup_master etc ON p.education_type_code::text = etc.code::text AND etc.type::text = 'EDUCATION_TYPE'::text AND etc.is_active = true
     LEFT JOIN lookup_master itc ON p.industry_type_code::text = itc.code::text AND itc.type::text = 'INDUSTRY_TYPE'::text AND itc.is_active = true
     LEFT JOIN lookup_master nc ON p.nationality_code::text = nc.code::text AND nc.type::text = 'NATIONALITY'::text AND nc.is_active = true
     LEFT JOIN lookup_master sc ON p.salutation_code::text = sc.code::text AND sc.type::text = 'SALUTATION'::text AND sc.is_active = true
     LEFT JOIN lookup_master rt ON p.relation_type_code::text = rt.code::text AND rt.type::text = 'RELATION_TYPE'::text AND rt.is_active = true
     LEFT JOIN lookup_master pt ON p.profession_type_code::text = pt.code::text AND pt.type::text = 'PROFESSION_TYPE'::text AND pt.is_active = true
     LEFT JOIN lookup_master pro ON p.profession_code::text = pro.code::text AND pro.type::text = 'PROFESSION'::text AND pro.is_active = true
     LEFT JOIN lookup_master seg ON p.segment_type_code::text = seg.code::text AND seg.type::text = 'PARTY_SEGMENT'::text AND seg.is_active = true
     LEFT JOIN lookup_master nit ON p.national_id_type_code::text = nit.code::text AND nit.type::text = 'NATIONAL_ID_TYPE'::text AND nit.is_active = true
     LEFT JOIN lookup_master jp ON p.job_position_type_code::text = jp.code::text AND jp.type::text = 'JOB_POSITION'::text AND jp.is_active = true
     LEFT JOIN party_address pa ON p.party_id = pa.party_id
     LEFT JOIN lookup_master a ON pa.address_type_code::text = a.code::text AND a.type::text = 'ADDRESS_TYPE'::text
     LEFT JOIN address_master c ON pa.city_code::text = c.code::text AND c.type::text = 'CITY'::text
     LEFT JOIN address_master d ON pa.district_code::text = d.code::text AND d.type::text = 'DISTRICT'::text
     LEFT JOIN address_master w ON pa.ward_code::text = w.code::text AND w.type::text = 'WARD'::text
     LEFT JOIN country_master cm ON pa.country_code::text = cm.code::text
     LEFT JOIN party_contact_details pc ON p.party_id = pc.party_id
     LEFT JOIN lookup_master con ON pc.contact_type_code::text = con.code::text AND con.type::text = 'CONTACT_TYPE'::text AND con.is_active = true
     LEFT JOIN party_asset pas ON p.party_id = pas.party_id
     LEFT JOIN lookup_master asst ON pas.asset_type_code::text = asst.code::text AND asst.type::text = 'ASSET_TYPE'::text AND asst.is_active = true
	 LEFT JOIN party_risk prc ON p.party_id = prc.party_id
	 LEFT JOIN lookup_master ctmrtc on prc.risk_type_code ::text = ctmrtc.code:: text AND ctmrtc.type = 'RISK_CATEGORY'::text AND ctmrtc.is_active = true
	 LEFT JOIN party_memo pm ON p.party_id = pm.party_id
	 LEFT JOIN lookup_master ctmpm ON pm.memo_type_code ::text =ctmpm.code:: text AND ctmpm.type = 'MEMO_TYPE'::text AND ctmpm.is_active = true
	 LEFT JOIN party_document pd ON p.party_id = pd.party_id
	 LEFT JOIN lookup_master ctmpd ON pd.document_type_code ::text =ctmpd.code:: text AND ctmpd.type = 'DOCUMENT_TYPE'::text AND ctmpd.is_active = true
	 LEFT JOIN country_master cmpd ON pd.issuing_country_code ::text =cmpd.code:: text AND cmpd.is_active = true
	 LEFT JOIN party_relation prel ON p.party_id = prel.party_id
	 LEFT JOIN lookup_master ctmprel ON prel.party_relation_type_code ::text =ctmprel.code:: text AND ctmprel.type = 'RELATION_TYPE'::text AND ctmprel.is_active = true
	 LEFT JOIN party_xref px ON p.party_id = px.party_id
     LEFT JOIN party_fatca_details fd ON p.party_id = fd.party_id
	 LEFT JOIN party_guardian pg ON p.party_id = pg.party_id
	 LEFT JOIN address_master pgw ON pg.guardian_ward_code::text = pgw.code::text AND pgw.type::text = 'WARD'::text AND pgw.is_active = true
     LEFT JOIN address_master pgd ON pg.guardian_district_code::text = pgd.code::text AND pgd.type::text = 'DISTRICT'::text AND pgd.is_active = true
     LEFT JOIN address_master pgpr ON pg.guardian_city_code::text = pgpr.code::text AND pgpr.type::text = 'CITY'::text AND pgpr.is_active = true;

-- View: party_view
CREATE OR REPLACE VIEW party_view
AS
SELECT p.party_id,
    p.party_identifier,
    p.type,
    p.salutation_code,
    sc.description AS salutation,
    p.full_name,
    p.first_name,
    p.middle_name,
    p.last_name,
    p.mother_maiden_name,
    p.nick_name,
    p.gender,
    p.date_of_birth,
    p.place_of_birth,
    p.country_birth_code,
    cbc.description AS country_of_birth,
    p.primary_mobile_number,
    p.primary_email,
    p.marital_status,
    p.status,
    p.country_residence_code,
    crc.description AS country_residence,
    p.residency_type_code,
    rtc.description AS residency_type,
    p.education_type_code,
    etc.description AS education_type,
    p.profession_code,
    pro.description AS profession,
    p.profession_type_code,
    pt.description AS profession_type,
    p.job_position_type_code,
    jp.description AS job_position,
    p.industry_type_code,
    itc.description AS industry_type,
    p.nationality_code,
    nc.description AS nationality,
    p.annual_income,
    p.annual_turnover,
    p.tax_id,
    p.date_of_incorp,
    p.staff_code,
    p.company_code,
    com.description AS company,
    p.group_code,
    p.portfolio_code,
    p.segment_type_code,
    seg.description AS segment,
    p.relation_type_code,
    rt.description AS relation_type,
    p.refferal_code,
    p.promo_code,
    p.national_id_type_code,
    nit.description AS national_id_type,
    p.national_id,
    p.aml_risk_eval_date,
    p.aml_risk,
    p.aml_check_status,
    p.is_staff,
    p.is_deceased,
    p.is_insolvency,
    p.is_npa,
    p.is_willful_defaulter,
    p.willful_defaulter_date,
    p.is_loan_overdue,
    p.is_suit_failed,
    p.is_pep,
    p.is_fatca_applicable,
    p.is_email_statement_reg,
    p.is_under_watchlist,
    p.is_deleted,
    p.created_at,
    p.created_by,
    p.updated_at,
    p.updated_by,
    p.created_by_channel,
    p.updated_by_channel
    FROM party p
     LEFT JOIN country_master cbc ON p.country_birth_code::text = cbc.code::text AND cbc.is_active = true
     LEFT JOIN country_master crc ON p.country_residence_code::text = crc.code::text AND crc.is_active = true
     LEFT JOIN lookup_master com ON p.company_code::text = com.code::text AND com.type::text = 'COMPANY_TYPE'::text AND com.is_active = true
     LEFT JOIN lookup_master rtc ON p.residency_type_code::text = rtc.code::text AND rtc.type::text = 'RESIDENCY_TYPE'::text AND rtc.is_active = true
     LEFT JOIN lookup_master etc ON p.education_type_code::text = etc.code::text AND etc.type::text = 'EDUCATION_TYPE'::text AND etc.is_active = true
     LEFT JOIN lookup_master itc ON p.industry_type_code::text = itc.code::text AND itc.type::text = 'INDUSTRY_TYPE'::text AND itc.is_active = true
     LEFT JOIN lookup_master nc ON p.nationality_code::text = nc.code::text AND nc.type::text = 'NATIONALITY'::text AND nc.is_active = true
     LEFT JOIN lookup_master sc ON p.salutation_code::text = sc.code::text AND sc.type::text = 'SALUTATION'::text AND sc.is_active = true
     LEFT JOIN lookup_master rt ON p.relation_type_code::text = rt.code::text AND rt.type::text = 'RELATION_TYPE'::text AND rt.is_active = true
     LEFT JOIN lookup_master pt ON p.profession_type_code::text = pt.code::text AND pt.type::text = 'PROFESSION_TYPE'::text AND pt.is_active = true
     LEFT JOIN lookup_master pro ON p.profession_code::text = pro.code::text AND pro.type::text = 'PROFESSION'::text AND pro.is_active = true
     LEFT JOIN lookup_master seg ON p.segment_type_code::text = seg.code::text AND seg.type::text = 'PARTY_SEGMENT'::text AND seg.is_active = true
     LEFT JOIN lookup_master nit ON p.national_id_type_code::text = nit.code::text AND nit.type::text = 'NATIONAL_ID_TYPE'::text AND nit.is_active = true
     LEFT JOIN lookup_master jp ON p.job_position_type_code::text = jp.code::text AND jp.type::text = 'JOB_POSITION'::text AND jp.is_active = true;

-- View: party_address_view
CREATE OR REPLACE VIEW party_address_view
 AS
 SELECT pa.party_address_id,
    p.party_id,
    pa.address_type_code,
    a.description AS address_type,
    pa.address_line1,
    pa.address_line2,
    pa.address_line3,
    pa.zip_code AS city_zip_code,
    pa.is_default,
    pa.ward_code,
    w.description AS ward,
    pa.district_code,
    d.description AS district,
    pa.city_code,
    c.description AS city,
    pa.country_code,
    cm.description AS country,
    pa.document_id
   FROM party_address pa
     LEFT JOIN party p ON p.party_id = pa.party_id
     LEFT JOIN lookup_master a ON pa.address_type_code::text = a.code::text AND a.type::text = 'ADDRESS_TYPE'::text
     LEFT JOIN address_master c ON pa.city_code::text = c.code::text AND c.type::text = 'CITY'::text
     LEFT JOIN address_master d ON pa.district_code::text = d.code::text AND d.type::text = 'DISTRICT'::text
     LEFT JOIN address_master w ON pa.ward_code::text = w.code::text AND w.type::text = 'WARD'::text
     LEFT JOIN country_master cm ON pa.country_code::text = cm.code::text;

-- View: party_contact_view
CREATE OR REPLACE VIEW party_contact_view
 AS
 SELECT pc.party_contact_details_id,
    p.party_id,
    pc.contact_type_code,
    con.description AS contact_type,
    pc.contact_value,
    pc.isd_code,
    pc.is_primary,
    pc.is_verified,
    pc.verified_mode,
    pc.last_verified_date::date AS last_verified_date,
    pc.is_dnd
   FROM party_contact_details pc
     LEFT JOIN party p ON p.party_id = pc.party_id
     LEFT JOIN lookup_master con ON pc.contact_type_code::text = con.code::text AND con.type::text = 'CONTACT_TYPE'::text AND con.is_active = true;

-- View: party_asset_view
CREATE OR REPLACE VIEW party_asset_view  AS
 SELECT
    pas.party_asset_id,
	p.party_id,
    pas.asset_type_code,
	asst.description AS asset_type,
    pas.asset_name,
    pas.potential_value,
    pas.is_mortgaged
   FROM party_asset pas
     LEFT JOIN party p ON p.party_id = pas.party_id
     LEFT JOIN lookup_master asst ON pas.asset_type_code::text = asst.code::text AND asst.type::text = 'ASSET_TYPE'::text AND asst.is_active = true;

-- View: party_risk_view
CREATE OR REPLACE VIEW party_risk_view  AS
 SELECT
    prc.party_risk_id,
	p.party_id,
    prc.risk_type_code,
	ctmrtc.description AS risk_type,
	prc.risk_score,
	prc.evaluation_date::date AS evaluation_date,
	prc.valid_until::date AS valid_until
   FROM party_risk prc
     LEFT JOIN party p ON p.party_id = prc.party_id
     LEFT JOIN lookup_master ctmrtc on prc.risk_type_code ::text = ctmrtc.code:: text AND ctmrtc.type = 'RISK_CATEGORY'::text AND ctmrtc.is_active = true;

-- View: party_memo_view
CREATE OR REPLACE VIEW party_memo_view  AS
 SELECT
   pm.party_memo_id,
   p.party_id,
   pm.memo_type_code,
   ctmpm.description AS memo_type,
   pm.severity,
   pm.risk_score AS score,
   pm.valid_from::date AS valid_from,
   pm.valid_until::date AS valid_to
   FROM party_memo pm
     LEFT JOIN party p ON p.party_id = pm.party_id
     LEFT JOIN lookup_master ctmpm ON pm.memo_type_code ::text =ctmpm.code:: text AND ctmpm.type = 'MEMO_TYPE'::text AND ctmpm.is_active = true;

-- View: party_document_view
CREATE OR REPLACE VIEW party_document_view  AS
 SELECT
    pd.party_document_id,
	p.party_id,
	pd.document_type_code,
	ctmpd.description AS document_type,
	pd.document_number,
	pd.document_number_masked,
	pd.document_number_token,
	pd.issuing_date::date AS issuing_date,
	pd.expiry_date::date AS expiry_date,
	pd.issuing_place,
	pd.issuing_country_code,
	cmpd.description AS issuing_country,
	pd.is_poi,
	pd.is_poa,
	pd.dms_reference_id,
	pd.verification_status,
	pd.additional_data
   FROM party_document pd
     LEFT JOIN party p ON p.party_id = pd.party_id
   	 LEFT JOIN lookup_master ctmpd ON pd.document_type_code ::text =ctmpd.code:: text AND ctmpd.type = 'DOCUMENT_TYPE'::text AND ctmpd.is_active = true
	 LEFT JOIN country_master cmpd ON pd.issuing_country_code ::text =cmpd.code:: text AND cmpd.is_active = true;

-- View: party_relation_view
CREATE OR REPLACE VIEW party_relation_view  AS
 SELECT
    prel.party_relation_id,
    p.party_id,
	prel.secondary_party_id,
	prel.party_relation_type_code,
	ctmprel.description AS party_relation_type,
	prel.inv_relation
   FROM party_relation prel
     LEFT JOIN party p ON p.party_id = prel.party_id
	 LEFT JOIN lookup_master ctmprel ON prel.party_relation_type_code ::text =ctmprel.code:: text AND ctmprel.type = 'RELATION_TYPE'::text AND ctmprel.is_active = true;

-- View: party_fatca_details_view
CREATE OR REPLACE VIEW party_fatca_details_view  AS
 SELECT
    fd.party_fatca_details_id,
	p.party_id,
	fd.place_of_incorporation,
	fd.country_of_incorporation,
	fd.country_of_residence,
	fd.incorporation_number,
	fd.board_rel_number,
	fd.report_bl_number,
	fd.original_report_bl_number,
	fd.fatca_tax_id,
	fd.document_reference_id
	FROM party_fatca_details fd
     LEFT JOIN party p ON p.party_id = fd.party_id;

-- View: party_guardian_view
CREATE OR REPLACE VIEW party_guardian_view  AS
 SELECT
    pg.party_guardian_id,
	p.party_id,
	pg.guardian_first_name,
	pg.guardian_middle_name,
	pg.guardian_last_name,
	pg.guardian_relation,
	pg.guardian_address_line1,
	pg.guardian_address_line2,
	pg.guardian_address_line3,
	pg.guardian_ward_code,
	pgw.description AS guardian_ward,
	pg.guardian_district_code,
	pgd.description AS guardian_district,
	pg.guardian_city_code,
	pgpr.description AS guardian_city
	FROM party_guardian pg
    LEFT JOIN party p ON p.party_id = pg.party_id
	LEFT JOIN address_master pgw ON pg.guardian_ward_code::text = pgw.code::text AND pgw.type::text = 'WARD'::text AND pgw.is_active = true
    LEFT JOIN address_master pgd ON pg.guardian_district_code::text = pgd.code::text AND pgd.type::text = 'DISTRICT'::text AND pgd.is_active = true
    LEFT JOIN address_master pgpr ON pg.guardian_city_code::text = pgpr.code::text AND pgpr.type::text = 'CITY'::text AND pgpr.is_active = true;

-- View: party_xref_view
CREATE OR REPLACE VIEW party_xref_view
 AS
 SELECT px.party_xref_id,
    p.party_id,
    px.system_code,
    csc.description AS system_name,
    px.xref_id
    FROM party_xref px
    LEFT JOIN party p ON p.party_id = px.party_id
    LEFT JOIN lookup_master csc ON px.system_code::text = csc.code::text AND csc.type = 'INTEGRATION_TYPE';
