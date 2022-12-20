--changeset finx:10
create table kyc_detail
(
    id                                   bigserial
        constraint kyc_detail_pk
            primary key,
    prospect_id                          int8,
    mobile_number                        varchar   not null,
    mobile_number_hashcode               varchar   not null,
    prospect_full_name                   varchar   not null,
    date_of_birth                        date      not null,
    kyc_system                           varchar   not null,
    external_request_id                  varchar,
    kyc_type                             varchar   not null,
    kyc_status                           varchar   not null,
    document_type                        varchar   not null,
    document_id_number                   varchar   not null,
    original_kyc_details                 json      not null,
    kyc_details                          json      not null,
    is_manual_verification_needed        bool      not null,
    manual_verification_needed_reason varchar,
    is_kyc_data_modified                 bool,
    created_by                           varchar   not null,
    created_date                         timestamp not null,
    last_updated_by                      varchar   not null,
    last_updated_date                    timestamp not null
);
