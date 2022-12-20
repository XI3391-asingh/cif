CREATE OR REPLACE VIEW party_details_view  AS
 SELECT p.party_id,
    p.party_type,
	p.full_name,
    p.first_name,
    p.middle_name,
    p.last_name,
    p.gender,
    p.date_of_birth,
    p.primary_mobile_number,
    p.primary_email,
    p.marital_status,
	p.status,
    p.country_of_residence_code,
    crc.description AS country_of_residence,
    p.job_position_type_code,
    jp.description AS job_position,
    p.occupation_type_code,
    oc.description AS occupation,
    p.nationality_code,
    nc.description AS nationality,
    p.tax_id,
    p.segment_code,
    seg.description AS segment,
    p.national_id_type_code,
    nit.description AS national_id_type,
    p.national_id,
	p.aml_risk,
    p.aml_risk_eval_date,
    p.is_aml_status,
    p.is_pep,
    p.is_fatca_applicable,
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
	pa.city_code,
 	c.description AS city,
    pa.ward_code,
    w.description AS ward,
    pa.district_code,
    d.description AS district,
	pa.country_code,
    cm.description AS country

   FROM party p
     LEFT JOIN country_master crc ON p.country_of_residence_code::text = crc.code::text AND crc.is_active = true
	 LEFT JOIN lookup_master nc ON p.nationality_code::text = nc.code::text AND nc.type::text = 'NATIONALITY'::text AND nc.is_active = true
     LEFT JOIN lookup_master seg ON p.segment_code::text = seg.code::text AND seg.type::text = 'SEGAMENT'::text AND seg.is_active = true
     LEFT JOIN lookup_master nit ON p.national_id_type_code::text = nit.code::text AND nit.type::text = 'NATIONAL_ID_TYPE'::text AND nit.is_active = true
     LEFT JOIN lookup_master jp ON p.job_position_type_code::text = jp.code::text AND jp.type::text = 'JOB_POSITION'::text AND jp.is_active = true
     LEFT JOIN lookup_master oc ON p.occupation_type_code::text = oc.code::text AND oc.type::text = 'OCCUPATION'::text AND oc.is_active = true
     LEFT JOIN party_address pa ON p.party_id = pa.party_id
 	 LEFT JOIN lookup_master a ON pa.address_type_code::text = a.code::text AND a.type::text = 'ADDRESS_TYPE'::text AND a.is_active = true
     LEFT JOIN address_master c ON pa.city_code::text = c.code::text AND c.type::text = 'CITY'::text
     LEFT JOIN address_master d ON pa.district_code::text = d.code::text AND d.type::text = 'DISTRICT'::text
     LEFT JOIN address_master w ON pa.ward_code::text = w.code::text AND w.type::text = 'WARD'::text
     LEFT JOIN country_master cm ON pa.country_code::text = cm.code::text
