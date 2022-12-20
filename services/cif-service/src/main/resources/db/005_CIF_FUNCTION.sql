--liquibase formatted sql
--changeset Manoj:1 splitStatements:false dbms:postgresql
CREATE OR REPLACE FUNCTION check_dedupe(
countrycode character varying,
mobilenumber character varying,
fullname character varying,
dateofbirth date,
nationalidtypecode character varying,
nationalid character varying,
nationalitycode character varying
)

RETURNS TABLE(party_id bigint, status text, full_name text, first_name text, middle_name text,
			 last_name text,gender text,date_of_birth TIMESTAMP,primary_mobile_number text,primary_email text,national_id_type_code text,national_id_type text,national_id text)
LANGUAGE 'plpgsql'

COST 100
VOLATILE
ROWS 1000

AS $$
DECLARE
query_str text;
national_id_type_value text;
BEGIN

IF((mobilenumber IS NULL OR mobilenumber = '') AND (fullname IS NULL OR fullname = '')  AND (nationalid IS NULL OR nationalid = '') AND (nationalidtypecode IS NULL OR nationalidtypecode = '') AND (dateofbirth IS NULL))THEN
RAISE EXCEPTION 'INPUT PARAMETER EMPTY';
END IF;

--CASE1 : MOBILE NUMBER + COUNTRY CODE
IF((mobilenumber IS NOT NULL AND mobilenumber != '')  AND (countrycode IS NOT NULL AND countrycode != '') AND (mobilenumber IS NULL OR mobilenumber = ''))THEN
  RAISE EXCEPTION 'Mobile number and country code are required to identify dedupe !';
END IF;

--CASE2 : DATEOFBIRTH + FULLNAME
IF((dateofbirth IS NOT NULL) AND ((fullname IS NULL OR fullname = '')))THEN
  RAISE EXCEPTION 'Date Of Birth and Full Name are required to identify dedupe !';
END IF;

IF((nationalidtypecode IS NOT NULL AND nationalidtypecode != '') AND (nationalid IS NOT NULL AND nationalid != ''))THEN
   SELECT UPPER(lm.description::text) AS national_id_type  INTO  national_id_type_value FROM lookup_master AS lm where code=''||nationalidtypecode||'' AND type = 'NATIONAL_ID_TYPE' AND is_active = true;
   --RAISE NOTICE 'National ID TYPE : %', national_id_type_value;
END IF;

--CASE3 : NATIONAL ID TYPE + NATIONALITY CODE + NATIONALITY CODE
IF((national_id_type_value IS NOT NULL AND national_id_type_value!='' AND national_id_type_value='PASSPORT') AND (nationalid IS NOT NULL AND nationalid!='')
    AND (nationalitycode IS NULL OR nationalitycode = '')) THEN
     RAISE EXCEPTION 'National Id type is PASSPORT then National Id, National Id type code and Nationality code is required to identify dedupe !';
END IF;

query_str:='SELECT p.party_id,p.status::text,p.full_name::text,p.first_name::text,p.middle_name::text,p.last_name::text,p.gender::text,p.date_of_birth,p.primary_mobile_number::text,
    		p.primary_email::text,p.national_id_type_code::text,nit.description::text AS national_id_type,p.national_id::text FROM party p
			LEFT JOIN lookup_master nit ON nit.type=p.national_id_type_code AND nit.type = ''NATIONAL_ID_TYPE'' AND nit.is_active = true
			WHERE 1=1';

IF(countrycode IS NOT NULL AND countrycode != '')THEN
query_str:=query_str||' AND p.country_residence_code  = '''||countrycode||''' ';
END IF;

IF(mobilenumber IS NOT NULL AND mobilenumber != '')THEN
query_str:=query_str||' AND p.primary_mobile_number = '''||mobilenumber||''' ';
END IF;

IF(fullname IS NOT NULL AND fullname != '')THEN
query_str:=query_str||' AND p.full_name  = '''||fullname||''' ';
END IF;

IF(dateofbirth IS NOT NULL)THEN
query_str:=query_str||' AND p.date_of_birth = '''||dateofbirth||''' ';
END IF;

IF(nationalid IS NOT NULL AND nationalid != '')THEN
query_str:=query_str||' AND p.national_id  = '''||nationalid||''' ';
END IF;

IF(nationalidtypecode IS NOT NULL AND nationalidtypecode != '')THEN
query_str:=query_str||' AND p.national_id_type_code = '''||nationalidtypecode||''' ';
END IF;

IF(nationalitycode IS NOT NULL AND nationalitycode != '')THEN
query_str:=query_str||' AND p.nationality_code = '''||nationalitycode||''' ';
END IF;

query_str:=query_str||'ORDER BY p.party_id';

--RAISE NOTICE 'QUERY : %', query_str;

RETURN QUERY EXECUTE query_str;

END;
$$;
