--country_master
INSERT INTO country_master(code, description, isd_code, iso_code)
VALUES ('01','Vietnam', '+84','VNM');

--address_master
INSERT INTO address_master (type, code, description,parent_type,parent_code)
values ('CITY', '01','Hồ Chí Minh','COUNTRY','01');
insert into address_master (type, code, description,parent_type,parent_code)
values ('CITY', '02','Hanoi','COUNTRY','02');
insert into address_master (type, code, description,parent_type,parent_code)
values ('WARD', '01','Hanoi','CITY', '01');
insert into address_master (type, code, description,parent_type,parent_code)
values ('WARD', '02','Haiphong','CITY', '02');
insert into address_master (type, code, description,parent_type,parent_code)
values ('DISTRICT', '01','district7','WARD', '01');

--lookup master
insert into lookup_master (type, code, description) values ('ADDRESS_TYPE', '01','Permanent Address');
insert into lookup_master (type, code, description) values ('ADDRESS_TYPE', '02','Correspondence Address');
insert into lookup_master (type, code, description) values ('NATIONAL_ID_TYPE', '01','National ID');
insert into lookup_master (type, code, description) values ('NATIONAL_ID_TYPE', '02','Citizen ID with chip');
insert into lookup_master (type, code, description) values ('NATIONAL_ID_TYPE', '03','Citizen ID without chip');
insert into lookup_master (type, code, description) values ('NATIONAL_ID_TYPE', '04','Passport');
insert into lookup_master (type, code, description) values ('OCCUPATION', '01','AGRICULTURE, FORESTRY AND FISH PRODUCTS');
insert into lookup_master (type, code, description) values ('OCCUPATION', '02','EXTRACTIVE');
insert into lookup_master (type, code, description) values ('OCCUPATION', '03','MANUFACTURING AND PROCESSING INDUSTRY');
insert into lookup_master (type, code, description) values ('OCCUPATION', '04','CPRODUCTION AND DISTRIBUTION OF ELECTRICITY, GAS, HOT WATER, STEAM AND AIR CONDITIONERS');
insert into lookup_master (type, code, description) values ('OCCUPATION', '05','Permanent Address');
insert into lookup_master (type, code, description) values ('JOB_POSITION', '01','Senior Manager');
insert into lookup_master (type, code, description) values ('JOB_POSITION', '02','Middle Manager');
insert into lookup_master (type, code, description) values ('JOB_POSITION', '03','Specialist');
insert into lookup_master (type, code, description) values ('SEGAMENT', '01','VIP');
insert into lookup_master (type, code, description) values ('NATIONALITY', '01','Vietnamese');
