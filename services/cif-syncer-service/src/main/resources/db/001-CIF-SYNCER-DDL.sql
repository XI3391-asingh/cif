-- Table: system_detail
-- Sequence Name : system_id_seq
create sequence IF NOT EXISTS system_id_seq;

-- DROP TABLE IF EXISTS system_detail;
CREATE TABLE system_detail (
  system_id bigint DEFAULT NEXTVAL('system_id_seq') NOT NULL,
  system_code VARCHAR(10) NOT NULL,
  system_type VARCHAR(40)  NOT NULL,
  description VARCHAR(200) ,
  CONSTRAINT system_pkey PRIMARY KEY (system_id)
);


create sequence IF NOT EXISTS change_config_id_seq;

-- Table: change_config
-- DROP TABLE IF EXISTS change_config;
CREATE TABLE IF NOT EXISTS change_config (
  change_config_id bigint DEFAULT NEXTVAL('change_config_id_seq') NOT NULL,
  system_id bigint NOT NULL,
  config_fields  VARCHAR(100) ARRAY NOT NULL,
  api_details jsonb NOT NULL,
  is_active boolean NOT NULL DEFAULT true,
  CONSTRAINT change_config_pkey PRIMARY KEY (change_config_id),
  CONSTRAINT change_config_system_id_key UNIQUE (system_id),
  CONSTRAINT change_config_id_fkey FOREIGN KEY (system_id) REFERENCES system_detail(system_id)
);


-- Table: adapter_config
-- Sequence Name : adapter_config_id_seq
create sequence IF NOT EXISTS adapter_config_id_seq;

-- DROP TABLE IF EXISTS adapter_config;
CREATE TABLE IF NOT EXISTS adapter_config (
  adapter_config_id bigint NOT NULL DEFAULT nextval(
    'adapter_config_id_seq'
  ),
  source_system bigint,
  integration_system bigint,
  is_create boolean NOT NULL DEFAULT true,
  is_update boolean NOT NULL DEFAULT true,
  CONSTRAINT adapter_config_id_pkey1 PRIMARY KEY (adapter_config_id),
  CONSTRAINT adapter_config_integration_system_fkey FOREIGN KEY (integration_system) REFERENCES system_detail(system_id),
  CONSTRAINT adapter_config_source_system_fkey FOREIGN KEY (source_system) REFERENCES system_detail(system_id)
);

-- Table: party_xref
-- Sequence Name : party_xref_id_seq
create sequence IF NOT EXISTS party_xref_id_seq;

-- DROP TABLE IF EXISTS party_xref;
CREATE TABLE IF NOT EXISTS party_xref (
  party_xref_id bigint NOT NULL DEFAULT nextval('party_xref_id_seq'),
  party_id VARCHAR(50) NOT NULL,
  system_id bigint,
  xref_id VARCHAR(50) ,
  CONSTRAINT party_xref_id_id_pkey PRIMARY KEY (party_xref_id),
  CONSTRAINT party_xref_system_id_fkey FOREIGN KEY (system_id) REFERENCES system_detail(system_id)
);

-- Table: syncer_transaction
-- Sequence Name : syncer_transaction_id_seq
create sequence IF NOT EXISTS syncer_transaction_id_seq;

-- DROP TABLE IF EXISTS syncer_transaction;
CREATE TABLE IF NOT EXISTS syncer_transaction (
  syncer_transaction_id bigint NOT NULL DEFAULT nextval(
    'syncer_transaction_id_seq'
  ),
  party_id VARCHAR(50) NOT NULL,
  system_id bigint,
  event_type VARCHAR(50)  NOT NULL,
  event_transaction_id VARCHAR(350)  NOT NULL,
  status VARCHAR(20)  NOT NULL,
  api_request jsonb,
  api_response jsonb,
  created_at timestamp without time zone DEFAULT now(),
  updated_at timestamp without time zone,
  CONSTRAINT syncer_transaction_id_pkey PRIMARY KEY (syncer_transaction_id),
  CONSTRAINT syncer_transaction_event_transaction_id_key UNIQUE (system_id,event_transaction_id),
  CONSTRAINT syncer_transaction_system_id_fkey FOREIGN KEY (system_id) REFERENCES system_detail(system_id)
);

