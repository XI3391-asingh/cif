--set search_path to cif
--- Sequence Name : party_id_seq
CREATE SEQUENCE IF NOT EXISTS party_id_seq;
--- Table Name : party
CREATE TABLE IF NOT EXISTS party (
	party_id bigint NOT NULL DEFAULT nextval('party_id_seq'),
	party_type varchar(50)NOT NULL,
	full_name varchar(200) NOT NULL,
	first_name varchar(100) NOT NULL,
	middle_name varchar(100),
	last_name varchar(100) NOT NULL,
	gender varchar(20),
	date_of_birth TIMESTAMP without time zone NOT NULL,
	marital_status varchar(50),
	primary_mobile_number varchar(16) NOT NULL,
	primary_email varchar(100),
	status varchar(50) NOT NULL,
	national_id_type_code varchar(50) NOT NULL,
	national_id varchar(50) NOT NULL,
	nationality_code varchar(50),
	country_of_residence_code varchar(50),
	job_position_type_code varchar(50),
	occupation_type_code varchar(20) NOT NULL,
	segment_code varchar(50),
	tax_id varchar(50),
	aml_risk varchar(20) NULL,
	aml_risk_eval_date TIMESTAMP without time zone NULL,
	is_aml_status boolean DEFAULT false,
	is_pep boolean  DEFAULT false,
	is_fatca_applicable boolean NULL DEFAULT false,
	created_at TIMESTAMP without time zone NULL DEFAULT now(),
	created_by varchar(50),
	updated_at TIMESTAMP without time zone NULL,
	updated_by varchar(50),
	created_by_channel varchar(50),
	updated_by_channel varchar(50),
	CONSTRAINT party_id_pkey PRIMARY KEY (party_id)
);
--- Sequence Name : party_address_id_seq
CREATE SEQUENCE IF NOT EXISTS party_address_id_seq;
--- Table Name : party_address
CREATE TABLE IF NOT EXISTS party_address (
    party_address_id bigint NOT NULL DEFAULT nextval('party_address_id_seq'),
    party_id bigint NOT NULL,
    address_type_code varchar(50) NOT NULL,
    address_line1 varchar(500) NOT NULL,
    address_line2 varchar(250),
    address_line3 varchar(250) ,
    zip_code integer,
    is_default boolean  DEFAULT false,
    ward_code varchar(50),
    district_code varchar(50),
    city_code varchar(50),
    country_code varchar(50),
    CONSTRAINT party_address_id_pkey PRIMARY KEY (party_address_id)
);
