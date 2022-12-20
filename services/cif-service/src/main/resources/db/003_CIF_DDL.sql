--changeset Manoj:1
--set search_path to cif
--- Sequence Name : party_id_seq
CREATE SEQUENCE IF NOT EXISTS party_id_seq;
---Extension Name : uuid
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";
--- Table Name : party
CREATE TABLE IF NOT EXISTS party (
party_id bigint DEFAULT NEXTVAL('party_id_seq') NOT NULL,
party_identifier VARCHAR(50) NOT NULL UNIQUE DEFAULT uuid_generate_v4(),
type VARCHAR (50) NOT NULL,
salutation_code VARCHAR (50) NOT NULL,
full_name VARCHAR(200) NOT NULL,
first_name VARCHAR (100) NOT NULL,
middle_name VARCHAR (100),
last_name VARCHAR (100),
mother_maiden_name VARCHAR (100),
nick_name VARCHAR (100),
gender VARCHAR(50) NOT NULL,
date_of_birth TIMESTAMP without time zone NOT NULL,
place_of_birth VARCHAR (100) NOT NULL,
country_birth_code VARCHAR(50) NOT NULL,
primary_mobile_number VARCHAR (50) NOT NULL,
primary_email VARCHAR (100) NOT NULL,
marital_status VARCHAR (50) NOT NULL,
status VARCHAR(50) NOT NULL,
country_residence_code VARCHAR (50),
residency_type_code VARCHAR (50),
education_type_code VARCHAR (50),
profession_code VARCHAR (50),
profession_type_code VARCHAR (50),
job_position_type_code VARCHAR (50),
industry_type_code VARCHAR (50),
nationality_code  VARCHAR (50),
annual_income double precision ,
annual_turnover double precision,
tax_id VARCHAR (50),
date_of_incorp TIMESTAMP without time zone,
staff_code VARCHAR (50),
company_code VARCHAR (50),
group_code VARCHAR (50),
portfolio_code VARCHAR (50),
segment_type_code VARCHAR (50),
relation_type_code VARCHAR (50),
refferal_code VARCHAR (50),
promo_code VARCHAR (50),
national_id_type_code VARCHAR (50),
national_id VARCHAR (50),
aml_risk VARCHAR (50),
aml_risk_eval_date  TIMESTAMP without time zone,
aml_check_status boolean NOT NULL DEFAULT FALSE,
is_staff boolean,
is_deceased boolean NOT NULL DEFAULT FALSE,
is_insolvency boolean NOT NULL DEFAULT FALSE,
is_npa boolean NOT NULL DEFAULT FALSE,
is_willful_defaulter boolean NOT NULL DEFAULT FALSE,
willful_defaulter_date TIMESTAMP without time zone,
is_loan_overdue boolean NOT NULL DEFAULT FALSE,
is_suit_failed boolean NOT NULL DEFAULT FALSE,
is_pep boolean NOT NULL DEFAULT FALSE,
is_fatca_applicable boolean NOT NULL DEFAULT FALSE,
is_email_statement_reg boolean NOT NULL DEFAULT FALSE,
is_under_watchlist boolean NOT NULL DEFAULT FALSE,
is_deleted boolean NOT NULL DEFAULT FALSE,
created_at TIMESTAMP without time zone DEFAULT now(),
created_by VARCHAR (100),
updated_at TIMESTAMP without time zone,
updated_by VARCHAR (100),
created_by_channel VARCHAR (100),
updated_by_channel VARCHAR (100),
CONSTRAINT party_id_pkey PRIMARY KEY(party_id)
);
--- Sequence Name : party_address_id_seq
CREATE SEQUENCE IF NOT EXISTS party_address_id_seq;
--- Table Name : party_address
CREATE TABLE IF NOT EXISTS party_address (
party_address_id bigint DEFAULT NEXTVAL('party_address_id_seq') NOT NULL,
party_id bigint NOT NULL,
address_type_code VARCHAR(50) NOT NULL,
address_line1 VARCHAR (250) NOT NULL,
address_line2 VARCHAR (100) NOT NULL,
address_line3 VARCHAR (100),
zip_code INTEGER NOT NULL,
is_default boolean,
ward_code VARCHAR(50),
district_code VARCHAR(50),
city_code VARCHAR(50),
country_code VARCHAR(50),
document_id VARCHAR(100),
CONSTRAINT party_address_id_pkey PRIMARY KEY (party_address_id),
CONSTRAINT fk_party_address_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);
--- Sequence Name : party_asset_id_seq
CREATE SEQUENCE IF NOT EXISTS party_asset_id_seq;
--- Table Name : party_asset
CREATE TABLE IF NOT EXISTS party_asset (
party_asset_id bigint DEFAULT NEXTVAL('party_asset_id_seq') NOT NULL,
party_id bigint,
asset_type_code VARCHAR (50),
asset_name VARCHAR (100),
potential_value double precision,
is_mortgaged boolean,
CONSTRAINT party_asset_id_pkey PRIMARY KEY (party_asset_id),
CONSTRAINT fk_party_asset_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);

--- Sequence Name : party_contact_details_id_seq
CREATE SEQUENCE IF NOT EXISTS party_contact_details_id_seq;
  --- Table Name : party_contact_details
 CREATE TABLE IF NOT EXISTS party_contact_details (
  party_contact_details_id bigint DEFAULT NEXTVAL('party_contact_details_id_seq') NOT NULL,
  party_id bigint NOT NULL,
  contact_type_code VARCHAR (50),
  contact_value VARCHAR (100),
  isd_code VARCHAR (50),
  is_primary boolean,
  is_verified boolean,
  verified_mode VARCHAR (50),
  last_verified_date TIMESTAMP without time zone,
  is_dnd boolean,
  CONSTRAINT party_contact_channel_id_pkey PRIMARY KEY (party_contact_details_id),
  CONSTRAINT fk_contact_party_id FOREIGN KEY (party_id)
  REFERENCES party(party_id) ON DELETE CASCADE
  );

--- Sequence Name : party_risk_id_seq
CREATE SEQUENCE IF NOT EXISTS party_risk_id_seq;
--- Table Name : party_risk
CREATE TABLE IF NOT EXISTS party_risk (
party_risk_id bigint DEFAULT NEXTVAL('party_risk_id_seq') NOT NULL,
party_id bigint,
risk_type_code VARCHAR (50),
risk_score double precision ,
evaluation_date TIMESTAMP without time zone,
valid_until TIMESTAMP without time zone,
CONSTRAINT party_risk_id_pkey PRIMARY KEY (party_risk_id),
CONSTRAINT fk_party_risk_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);

--- Sequence Name : party_memo_id_seq
CREATE SEQUENCE IF NOT EXISTS party_memo_id_seq;
--- Table Name : party_memo
CREATE TABLE IF NOT EXISTS party_memo (
party_memo_id bigint DEFAULT NEXTVAL('party_memo_id_seq') NOT NULL,
party_id bigint,
memo_type_code VARCHAR (50),
severity VARCHAR (50),
risk_score double precision ,
valid_from TIMESTAMP without time zone,
valid_until TIMESTAMP without time zone,
CONSTRAINT party_memo_id_pkey PRIMARY KEY (party_memo_id),
CONSTRAINT fk_party_memo_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);

--- Sequence Name : party_document_id_seq
CREATE SEQUENCE IF NOT EXISTS party_document_id_seq;
--- Table Name : party_document
CREATE TABLE IF NOT EXISTS party_document (
party_document_id bigint DEFAULT NEXTVAL('party_document_id_seq') NOT NULL,
party_id bigint,
document_type_code VARCHAR (50),
document_number VARCHAR (50),
document_number_masked VARCHAR (50),
document_number_token VARCHAR (100),
issuing_date TIMESTAMP without time zone,
expiry_date TIMESTAMP without time zone,
issuing_place VARCHAR (50),
issuing_country_code VARCHAR (50),
is_poi BOOLEAN,
is_poa BOOLEAN,
dms_reference_id VARCHAR (1000),
verification_status VARCHAR (50),
additional_data VARCHAR (1000),
CONSTRAINT party_document_id_pkey PRIMARY KEY (party_document_id),
CONSTRAINT fk_party_document_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);

--- Sequence Name : party_relation_id_seq
CREATE SEQUENCE IF NOT EXISTS party_relation_id_seq;
--- Table Name : party_relation
CREATE TABLE IF NOT EXISTS party_relation (
party_relation_id bigint DEFAULT NEXTVAL('party_relation_id_seq') NOT NULL,
party_id BIGINT,
secondary_party_id VARCHAR (50),
party_relation_type_code VARCHAR (50),
inv_relation VARCHAR (50),
CONSTRAINT party_relation_id_pkey PRIMARY KEY (party_relation_id),
CONSTRAINT fk_party_relation_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);

--- Sequence Name : party_xref_id_seq
CREATE SEQUENCE IF NOT EXISTS party_xref_id_seq;
--- Table Name : party_xref
CREATE TABLE IF NOT EXISTS party_xref (
party_xref_id BIGINT DEFAULT NEXTVAL('party_xref_id_seq') NOT NULL,
party_id BIGINT,
system_code VARCHAR (50),
xref_id VARCHAR (50),
CONSTRAINT party_xref_id_pkey PRIMARY KEY (party_xref_id),
CONSTRAINT fk_party_xref_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);

--- Sequence Name : fatca_details_id_seq
CREATE SEQUENCE IF NOT EXISTS party_fatca_details_id_seq;
--- Table Name : fatca_details
CREATE TABLE IF NOT EXISTS party_fatca_details (
party_fatca_details_id BIGINT DEFAULT NEXTVAL('party_fatca_details_id_seq') NOT NULL,
party_id BIGINT,
place_of_incorporation VARCHAR (100),
country_of_incorporation VARCHAR (100),
country_of_residence VARCHAR (100),
incorporation_number VARCHAR (100),
board_rel_number VARCHAR (100),
report_bl_number VARCHAR (100),
original_report_bl_number VARCHAR (100),
fatca_tax_id VARCHAR (100),
document_reference_id VARCHAR (1000),
CONSTRAINT party_fatca_details_id_pkey PRIMARY KEY (party_fatca_details_id),
CONSTRAINT fk_party_fatca_details_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);

--- Sequence Name : party_guardian_id_seq
CREATE SEQUENCE IF NOT EXISTS party_guardian_id_seq;
--- Table Name : party_guardian
CREATE TABLE IF NOT EXISTS party_guardian (
party_guardian_id BIGINT DEFAULT NEXTVAL('party_guardian_id_seq') NOT NULL,
party_id BIGINT,
guardian_first_name VARCHAR (100),
guardian_middle_name VARCHAR (100),
guardian_last_name VARCHAR (100),
guardian_relation VARCHAR (50),
guardian_address_line1 VARCHAR (250),
guardian_address_line2 VARCHAR (100),
guardian_address_line3 VARCHAR (100),
guardian_ward_code VARCHAR (50),
guardian_city_code VARCHAR (50),
guardian_district_code VARCHAR (50),
CONSTRAINT party_guardian_id_pkey PRIMARY KEY (party_guardian_id),
CONSTRAINT fk_party_guardian_id FOREIGN KEY (party_id)
REFERENCES party(party_id) ON DELETE CASCADE
);
