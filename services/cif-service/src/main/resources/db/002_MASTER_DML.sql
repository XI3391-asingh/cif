--changeset Manoj:1
--country_master
INSERT INTO country_master(code, description, isd_code, iso_code)
VALUES ('01','Vietnam', '+84','VNM');

INSERT INTO country_master(code, description, isd_code, iso_code)
VALUES ('02','Singapore', '+65','SG');

--address_master
INSERT INTO address_master (type, code, description,parent_type,parent_code)
values ('CITY', '01','Hồ Chí Minh','COUNTRY','01');
insert into address_master (type, code, description,parent_type,parent_code)
values ('CITY', '02','Hanoi','COUNTRY','01');
INSERT INTO address_master (type, code, description,parent_type,parent_code)
values ('CITY', '03','Singapore','COUNTRY','02');
insert into address_master (type, code, description,parent_type,parent_code)
values ('CITY', '04','Singapore ','COUNTRY','02');
insert into address_master (type, code, description,parent_type,parent_code)
values ('WARD', '01','Hanoi','CITY', '01');
insert into address_master (type, code, description,parent_type,parent_code)
values ('WARD', '02','Haiphong','CITY', '02');
insert into address_master (type, code, description,parent_type,parent_code)
values ('DISTRICT', '01','district7','WARD', '01');
insert into address_master (type, code, description,parent_type,parent_code)
values ('DISTRICT', '02','district8','WARD', '02');

-- lookup_master
insert into lookup_master(type, code, description) values ('ADDRESS_TYPE', '01','Permanent Address');
insert into lookup_master(type, code, description) values ('ADDRESS_TYPE', '02','Correspondence Address');
insert into lookup_master(type, code, description) values ('ADDRESS_TYPE', '03','Office address');
insert into lookup_master(type, code, description) values ('ADDRESS_TYPE', '04','Temporary address');
insert into lookup_master(type, code, description) values ('CONTACT_TYPE', '01','Mobile');
insert into lookup_master(type, code, description) values ('CONTACT_TYPE', '02','Email');
insert into lookup_master(type, code, description) values ('CONTACT_TYPE', '03','Facebook');
insert into lookup_master(type, code, description) values ('CONTACT_TYPE', '04','Twitter');
insert into lookup_master(type, code, description) values ('CONTACT_TYPE', '05','Fax');
insert into lookup_master(type, code, description) values ('PARTY_SEGMENT', '01','Segment01');
insert into lookup_master(type, code, description) values ('PARTY_SEGMENT', '02','Segment02');
insert into lookup_master(type, code, description) values ('COMPANY_TYPE', '01','Vietnam Electricity');
insert into lookup_master(type, code, description) values ('COMPANY_TYPE', '02','Sioux High Pvt. ');
insert into lookup_master(type, code, description) values ('RESIDENCY_TYPE', '01','Vietnam Residency 1');
insert into lookup_master(type, code, description) values ('RESIDENCY_TYPE', '02','Vietnam Residency 2');
insert into lookup_master(type, code, description) values ('RESIDENCY_TYPE', '03','Singaporean Residency 1');
insert into lookup_master(type, code, description) values ('RESIDENCY_TYPE', '04','Singaporean Residency 2');
insert into lookup_master(type, code, description) values ('EDUCATION_TYPE', '01','Postgraduate');
insert into lookup_master(type, code, description) values ('EDUCATION_TYPE', '02','Undergraduate');
insert into lookup_master(type, code, description) values ('EDUCATION_TYPE', '03','Doctorate');
insert into lookup_master(type, code, description) values ('EDUCATION_TYPE', '04','Phd');
insert into lookup_master(type, code, description) values ('INDUSTRY_TYPE', '01','Textiles');
insert into lookup_master(type, code, description) values ('INDUSTRY_TYPE', '02','Food processing');
insert into lookup_master(type, code, description) values ('INDUSTRY_TYPE', '03','Banking');
insert into lookup_master(type, code, description) values ('INDUSTRY_TYPE', '04','IT');
insert into lookup_master(type, code, description) values ('INDUSTRY_TYPE', '05','Government Service');
insert into lookup_master(type, code, description) values ('NATIONALITY', '01','Vietnamese');
insert into lookup_master(type, code, description) values ('NATIONALITY', '02','Indian');
insert into lookup_master(type, code, description) values ('NATIONALITY', '03','Singaporean');
insert into lookup_master(type, code, description) values ('SALUTATION', '01','Mr.');
insert into lookup_master(type, code, description) values ('SALUTATION', '02','Ms.');
insert into lookup_master(type, code, description) values ('SALUTATION', '03','Dr.');
insert into lookup_master(type, code, description) values ('RISK_CATEGORY', '01','Low');
insert into lookup_master(type, code, description) values ('RISK_CATEGORY', '02','High');
insert into lookup_master(type, code, description) values ('RELATION_TYPE', '01','Relation 1');
insert into lookup_master(type, code, description) values ('RELATION_TYPE', '02','Relation 2');
insert into lookup_master(type, code, description) values ('PROFESSION_TYPE', '01','Business analyst');
insert into lookup_master(type, code, description) values ('PROFESSION_TYPE', '02','Designer');
insert into lookup_master(type, code, description) values ('PROFESSION_TYPE', '03','Full Time');
insert into lookup_master(type, code, description) values ('PROFESSION_TYPE', '04','Part Time');
insert into lookup_master(type, code, description) values ('PROFESSION', '01','Managers');
insert into lookup_master(type, code, description) values ('PROFESSION', '02','Technicians ');
insert into lookup_master(type, code, description) values ('PROFESSION', '03','Salaried ');
insert into lookup_master(type, code, description) values ('PROFESSION', '04','Self Employed');
insert into lookup_master(type, code, description) values ('PROFESSION', '05','Government Service');
insert into lookup_master(type, code, description) values ('ASSET_TYPE', '01','Car');
insert into lookup_master(type, code, description) values ('ASSET_TYPE', '02','Laptop');
insert into lookup_master(type, code, description) values ('NOMINEES_RELATION_TYPE', '01','Father');
insert into lookup_master(type, code, description) values ('NOMINEES_RELATION_TYPE', '02','Mother');
insert into lookup_master(type, code, description) values ('DOCUMENT_TYPE', '01','Passport');
insert into lookup_master(type, code, description) values ('DOCUMENT_TYPE', '02','National ID');
insert into lookup_master(type, code, description) values ('NATIONAL_ID_TYPE', '01','National Id');
insert into lookup_master(type, code, description) values ('NATIONAL_ID_TYPE', '02','Passport');
insert into lookup_master(type, code, description) values ('JOB_POSITION', '01','Senior Manager');
insert into lookup_master(type, code, description) values ('JOB_POSITION', '02','Specialist');
insert into lookup_master(type, code, description) values ('JOB_POSITION', '03','Executive');
insert into lookup_master(type, code, description) values ('JOB_POSITION', '04','Manager');
insert into lookup_master(type, code, description) values ('JOB_POSITION', '05','Director');
insert into lookup_master(type, code, description) values ('JOB_POSITION', '06','HOD');
insert into lookup_master(type, code, description) values ('MEMO_TYPE', '01','Confirmation');
insert into lookup_master(type, code, description) values ('MEMO_TYPE', '02','Request');
insert into lookup_master(type, code, description) values ('INTEGRATION', 'TM','Thought Machine');
insert into lookup_master(type, code, description) values ('INTEGRATION', 'E6','E6 Machine');
insert into lookup_master(type, code, description) values ('INTEGRATION', 'APPIAN','APPIAN Machine');

