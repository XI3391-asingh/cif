package com.masterdata.masterdataservice.db;

import com.masterdata.masterdataservice.api.*;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.SqlBatch;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;
import java.util.Set;


public interface MasterDataRepository {

    @SqlBatch("INSERT INTO lookup_master(lookup_master_id, type, code, description, created_by)"
            + " VALUES (nextval('lookup_master_id_seq'), :lm.type, :lm.code, :lm.description,:lm.createdBy)")
    boolean[] saveLookUpMasterData(@BindBean("lm") List<CreateMasterCmd> createMasterCmds);

    @SqlBatch("INSERT INTO address_master(address_master_id, type, code, description, created_by)"
            + " VALUES (nextval('address_master_id_seq'), :am.type, :am.code, :am.description,:am.createdBy)")
    boolean[] saveAddressMasterData(@BindBean("am") List<CreateMasterCmd> createMasterCmds);

    @SqlQuery("SELECT type, code, description, created_by FROM lookup_master")
    @RegisterBeanMapper(ViewMasterDataCmd.class)
    Set<ViewMasterDataCmd> findAllLookUpMasterData();

    @SqlQuery("SELECT type, code, description, created_by FROM address_master")
    @RegisterBeanMapper(ViewMasterDataCmd.class)
    Set<ViewMasterDataCmd> findAllAddressMasterData();

    @SqlUpdate("""
            UPDATE lookup_master SET
            type= CASE WHEN :lm.type IS NOT NULL THEN :lm.type ELSE type END,
            code= CASE WHEN :lm.code IS NOT NULL THEN :lm.code ELSE code END,
            description= CASE WHEN :lm.description IS NOT NULL THEN :lm.description ELSE description END,
            created_by= CASE WHEN :lm.createdBy IS NOT NULL THEN :lm.createdBy ELSE created_by END
            WHERE lookup_master_id= :masterDataId 
            """)
    boolean updateLookUpMasterData(@Bind("masterDataId") Long masterDataId, @BindBean("lm") UpdateMasterDataRequestCmd updateMasterDataRequestCmd);

    @SqlUpdate("""
            UPDATE address_master SET
            type= CASE WHEN :am.type IS NOT NULL THEN :am.type ELSE type END,
            code= CASE WHEN :am.code IS NOT NULL THEN :am.code ELSE code END,
            description= CASE WHEN :am.description IS NOT NULL THEN :am.description ELSE description END,
            created_by= CASE WHEN :am.createdBy IS NOT NULL THEN :am.createdBy ELSE created_by END
            WHERE address_master_id= :masterDataId 
            """)
    boolean updateAddressMasterData(@Bind("masterDataId") Long masterDataId, @BindBean("am") UpdateMasterDataRequestCmd updateMasterDataRequestCmd);

    @SqlUpdate("DELETE FROM lookup_master WHERE lookup_master_id= :masterDataId")
    boolean deleteLookUpMasterData(@Bind("masterDataId") long masterDataId);

    @SqlUpdate("DELETE FROM address_master WHERE address_master_id= :masterDataId")
    boolean deleteAddressMasterData(@Bind("masterDataId") long masterDataId);


    @SqlBatch("INSERT INTO country_master(country_master_id, code, description, isd_code, created_by)"
            + " VALUES (nextval('country_master_id_seq'), :cm.code, :cm.description, :cm.isdCode,:cm.createdBy)")
    boolean[] saveCountryMasterData(@BindBean("cm") List<CreateCountryMasterCmd> createCountryMasterCmd);

    @SqlQuery("SELECT code, description, isd_code, created_by FROM country_master")
    @RegisterBeanMapper(ViewCountryMasterDataCmd.class)
    Set<ViewCountryMasterDataCmd> findAllCountryMasterData();

    @SqlUpdate("""
            UPDATE country_master SET
            code= CASE WHEN :cm.code IS NOT NULL THEN :cm.code ELSE code END,
            description= CASE WHEN :cm.description IS NOT NULL THEN :cm.description ELSE description END,
            isd_code= CASE WHEN :cm.isdCode IS NOT NULL THEN :cm.isdCode ELSE isd_code END,
            created_by= CASE WHEN :cm.createdBy IS NOT NULL THEN :cm.createdBy ELSE created_by END
            WHERE country_master_id= :countryMasterDataId 
            """)
    boolean updateCountryMasterData(@Bind("countryMasterDataId") Long countryMasterDataId, @BindBean("cm") UpdateCountryMasterCmd updateCountryMasterCmd);

    @SqlUpdate("DELETE FROM country_master WHERE country_master_id= :countryMasterDataId")
    boolean deleteCountryMasterData(@Bind("countryMasterDataId") long countryMasterDataId);

    @SqlQuery("SELECT country_master_id FROM country_master WHERE code =:code")
    Long findCountryMasterRecordByCode(@Bind("code") String code);

    @SqlQuery("SELECT lookup_master_id FROM lookup_master WHERE code =:code")
    Long findLookupMasterRecordByCode(@Bind("code") String code);

    @SqlQuery("SELECT address_master_id FROM address_master WHERE code =:code")
    Long findAddressMasterRecordByCode(@Bind("code") String code);
}
