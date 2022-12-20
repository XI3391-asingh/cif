--- Sequence Name : country_master_id_seq
CREATE SEQUENCE country_master_id_seq;
--- Table Name : country
CREATE TABLE country_master (
country_master_id bigint DEFAULT NEXTVAL('country_master_id_seq') NOT NULL,
code VARCHAR(50) NOT NULL,
description VARCHAR(100),
isd_code VARCHAR(50),
iso_code VARCHAR(50),
is_active boolean DEFAULT TRUE,
created_date TIMESTAMP without time zone DEFAULT now(),
created_by VARCHAR (50),
modified_date TIMESTAMP without time zone,
modified_by VARCHAR (50),
CONSTRAINT country_id_master_pkey PRIMARY KEY (country_master_id)
);
--- Sequence Name : address_master_id_seq
CREATE SEQUENCE IF NOT EXISTS address_master_id_seq;
--- Table Name : address_master
CREATE TABLE IF NOT EXISTS address_master (
address_master_id bigint DEFAULT NEXTVAL('address_master_id_seq') NOT NULL,
type VARCHAR (100) NOT NULL ,
code VARCHAR (100)  NOT NULL,
description VARCHAR(200),
parent_type  VARCHAR (100),
parent_code VARCHAR(100),
is_active boolean DEFAULT TRUE,
created_date TIMESTAMP without time zone DEFAULT now(),
created_by VARCHAR (50),
modified_date TIMESTAMP without time zone,
modified_by VARCHAR (50),
CONSTRAINT address_master_id_pkey PRIMARY KEY (address_master_id)
);

--- Sequence Name : lookup_master_id_seq
CREATE SEQUENCE lookup_master_id_seq;
--- Table Name : lookup_master
CREATE TABLE lookup_master (
lookup_master_id bigint DEFAULT NEXTVAL('lookup_master_id_seq') NOT NULL,
type VARCHAR(100) NOT NULL ,
code VARCHAR (100) NOT NULL ,
description VARCHAR(200),
is_active boolean DEFAULT TRUE,
created_date TIMESTAMP without time zone DEFAULT now(),
created_by VARCHAR (50),
modified_date TIMESTAMP without time zone,
modified_by VARCHAR (50),
CONSTRAINT lookup_master_id_pk PRIMARY KEY (lookup_master_id)
);
