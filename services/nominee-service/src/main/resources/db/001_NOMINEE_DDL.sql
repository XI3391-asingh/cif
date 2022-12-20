--- Sequence Name : nominee_id_seq
CREATE SEQUENCE IF NOT EXISTS nominee_id_seq;
--- Table Name : nominee
CREATE TABLE IF NOT EXISTS nominee (
nominee_id bigint DEFAULT NEXTVAL('nominee_id_seq') NOT NULL,
party_id bigint NOT NULL,
salutation VARCHAR (50),
first_name VARCHAR (100),
middle_name VARCHAR (100),
last_name VARCHAR (100),
relation VARCHAR (50),
national_id VARCHAR (50),
CONSTRAINT nominee_id_pkey PRIMARY KEY (nominee_id)
);

--- Sequence Name : nominee_account_mapping_id_seq
CREATE SEQUENCE IF NOT EXISTS nominee_account_mapping_id_seq;
--- Table Name : nominee_account_mapping
CREATE TABLE IF NOT EXISTS nominee_account_mapping (
mapping_id BIGINT DEFAULT NEXTVAL('nominee_account_mapping_id_seq') NOT NULL,
party_id BIGINT NOT NULL,
nominee_id BIGINT NOT NULL,
account_number VARCHAR (50) NOT NULL,
CONSTRAINT mapping_id_pkey PRIMARY KEY (mapping_id),
CONSTRAINT nominee_account_mapping_party_id_account_number_nominee_id_key UNIQUE (party_id, account_number, nominee_id),
CONSTRAINT fk_nominee_account_mapping_id FOREIGN KEY (nominee_id)
REFERENCES nominee(nominee_id)
);