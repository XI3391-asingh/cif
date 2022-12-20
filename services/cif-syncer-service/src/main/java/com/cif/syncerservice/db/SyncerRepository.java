package com.cif.syncerservice.db;


import com.cif.syncerservice.core.syncer.domain.*;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.customizer.BindList;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.List;

public interface SyncerRepository {
    @SqlQuery("SELECT * FROM PARTY_XREF WHERE PARTY_ID=:partyId and system_id=:systemId")
    @RegisterBeanMapper(PartyXref.class)
    PartyXref fetchPartyXrefByPartyId(@Bind("partyId") String partyId, @Bind("systemId") int systemId);

    @SqlQuery("""
            select cc.change_config_id,sc.system_code,sc.system_id,sc.system_type,cc.config_fields,
            cc.api_details,cc.is_active from change_config cc
            inner join system_detail sc  on sc.system_id=cc.system_id where cc.is_active=true;
            """)
    @RegisterBeanMapper(ChangeConfig.class)
    List<ChangeConfig> fetchChangeConfigData();

    @SqlQuery("""
            select cc.change_config_id,sc.system_code,sc.system_id,sc.system_type,cc.config_fields,
            cc.api_details,cc.is_active from change_config cc
            inner join system_detail sc  on sc.system_id=cc.system_id;
            """)
    @RegisterBeanMapper(ChangeConfig.class)
    List<ChangeConfig> fetchChangeConfigDataAll();

    @SqlQuery("""
            select ac.adapter_config_id ,sc.system_code as source_system_code,
            integration_system.system_code as integration_system_code,
            ac.is_create,ac.is_update,ac.source_system as source_system_id,ac.integration_system as integration_system_id
            from adapter_config ac
            inner join system_detail sc  on sc.system_id=ac.source_system
            inner join system_detail integration_system  on integration_system.system_id=ac.integration_system;            
            """)
    @RegisterBeanMapper(AdapterConfig.class)
    List<AdapterConfig> fetchAdapterConfigData();

    @SqlQuery("SELECT * FROM syncer_transaction WHERE event_transaction_id= :eventTransactionId and system_id= :systemId")
    @RegisterBeanMapper(SyncerTransaction.class)
    SyncerTransaction fetchSyncerTransactionDataByEventTransactionID(@Bind("eventTransactionId") String eventTransactionId, @Bind("systemId") int systemId);

    @SqlQuery("SELECT kafka_payload FROM syncer_transaction WHERE party_id= :partyId and syncer_transaction_id in (select max(syncer_transaction_id) from syncer_transaction where party_id= :partyId group by party_id)")
    @RegisterBeanMapper(SyncerTransaction.class)
    String fetchPreviousRequestPayloadByPartyIdAndCreateEvent(@Bind("partyId") int partyId);

    @SqlUpdate("INSERT INTO party_xref(party_xref_id, party_id,system_id) VALUES (nextval('party_xref_id_seq'), :p.partyId, :p.systemId)")
    @GetGeneratedKeys
    int savePartyXrefRecord(@BindBean("p") PartyXref partyXref);

    @SqlUpdate("""
            INSERT INTO syncer_transaction(syncer_transaction_id, party_id,system_id,event_type,
            event_transaction_id,status,created_at) VALUES (nextval('syncer_transaction_id_seq'),
            :tr.partyId,:tr.systemId,:tr.eventType,:tr.eventTransactionId,
            :tr.status, :tr.createdAt)
             """)
    @GetGeneratedKeys
    int saveTransactionHistory(@BindBean("tr") SyncerTransaction syncerTransaction);

    @SqlUpdate("""
            UPDATE syncer_transaction SET api_request=CAST(:tr.apiRequest AS JSONB), api_response=CAST(:tr.apiResponse AS JSONB),
            status=:tr.status,updated_at=:tr.updatedAt WHERE syncer_transaction_id=:tr.syncerTransactionId;
            """)
    boolean updateTransactionHistory(@BindBean("tr") SyncerTransaction syncerTransaction);

    @SqlUpdate("UPDATE party_xref SET xref_id= :p.xrefId where party_xref_id= :p.partyXrefId")
    boolean updatePartyXrefRecord(@BindBean("p") PartyXref partyXref);

    @SqlQuery("SELECT * FROM syncer_transaction WHERE status= :status ORDER BY updated_at")
    @RegisterBeanMapper(SyncerTransaction.class)
    List<SyncerTransaction> fetchSyncerTransactionByTransactionStatusOrderByUpdatedAt(@Bind("status") String status);

    @SqlQuery("select system_code,system_id FROM system_detail WHERE system_code in (<systemCode>)")
    @RegisterBeanMapper(SystemDetail.class)
    List<SystemDetail> fetchSystemDetailsBySystemCode(@BindList("systemCode") List<String> systemCode);

    @SqlQuery("select system_code,system_type,system_id,description FROM system_detail")
    @RegisterBeanMapper(SystemDetail.class)
    List<SystemDetail> fetchSystemDetails();

    @SqlUpdate("""
            INSERT INTO change_config(system_id,config_fields,api_details,is_active) 
            VALUES (:cr.systemId,:cr.configFields,:cr.apiDetails::jsonb,:cr.isActive)
             """)
    @GetGeneratedKeys
    int saveConfigData(@BindBean("cr") ChangeConfig cr);

    @SqlQuery("""
            select sc.system_code,sc.system_id,cc.config_fields,
            cc.api_details,cc.is_active from change_config cc
            inner join system_detail sc  on sc.system_id=cc.system_id where 
            cc.change_config_id= :configId;
            """)
    @RegisterBeanMapper(ChangeConfig.class)
    ChangeConfig findChangeConfigByConfigId(@Bind("configId") Integer configId);


    @SqlQuery("""
            select cc.change_config_id,sc.system_code,sc.system_id,cc.config_fields,
            cc.api_details,cc.is_active,sc.system_type from change_config cc
            inner join system_detail sc  on sc.system_id=cc.system_id where 
            sc.system_code= :systemCode;
            """)
    @RegisterBeanMapper(ChangeConfig.class)
    ChangeConfig findChangeConfigBySystemCode(@Bind("systemCode") String systemCode);

    @SqlUpdate("""
            UPDATE change_config SET config_fields= :cg.configFields,
            api_details=:cg.apiDetails::jsonb,is_active=:cg.isActive
            where change_config_id=:cg.changeConfigId
             """)
    boolean updateConfigData(@BindBean("cg") ChangeConfig cg);

    @SqlUpdate("""
            INSERT INTO system_detail(system_code,system_type,description) 
            VALUES (:system.systemCode,:system.systemType,:system.description)
             """)
    @GetGeneratedKeys
    int saveSystemDetail(@BindBean("system") SystemDetail systemDetail);


    @SqlUpdate("""
            INSERT INTO adapter_config(source_system,integration_system,is_create,is_update) 
            VALUES (:adapterConfig.sourceSystemId,:adapterConfig.integrationSystemId,:adapterConfig.isCreate,:adapterConfig.isUpdate)
             """)
    @GetGeneratedKeys
    int saveAdapterConfig(@BindBean("adapterConfig") AdapterConfig adapterConfig);

    @SqlQuery("""
            select ac.adapter_config_id,sc.system_code as source_system_code,
            integration_system.system_code as integration_system_code,
            ac.source_system as source_system_id,ac.integration_system as integration_system_id,
            ac.is_create,ac.is_update
            from adapter_config ac
            inner join system_detail sc  on sc.system_id=ac.source_system
            inner join system_detail integration_system  on integration_system.system_id=ac.integration_system
            where ac.adapter_config_id= :adapterConfigId;
            """)
    @RegisterBeanMapper(AdapterConfig.class)
    AdapterConfig findAdapterConfigByAdapterConfigId(@Bind("adapterConfigId") int adapterConfigId);


    @SqlQuery("""
            select ac.adapter_config_id,sc.system_id as source_system_id,sc.system_code as source_system_code,
            integration_system.system_code as integration_system_code,
            integration_system.system_id as integration_system_id,
            ac.is_create,ac.is_update
            from adapter_config ac
            inner join system_detail sc  on sc.system_id=ac.source_system
            inner join system_detail integration_system  on integration_system.system_id=ac.integration_system
            where sc.system_code= :systemCode;
            """)
    @RegisterBeanMapper(AdapterConfig.class)
    List<AdapterConfig> findAdapterConfigBySystemCode(@Bind("systemCode") String systemCode);


    @SqlUpdate("""
            UPDATE adapter_config SET is_create= :adapterConfig.isCreate,
            is_update= :adapterConfig.isUpdate
            where adapter_config_id=:adapterConfig.adapterConfigId
             """)
    boolean updateAdapterData(@BindBean("adapterConfig") AdapterConfig adapterConfig);

    @SqlUpdate("""
            UPDATE system_detail SET system_code= :system.systemCode,system_type= :system.systemType ,description=:system.description) 
            where system_id=:systemDetail.systemId
             """)
    boolean updateSystemDetail(@BindBean("system") SystemDetail systemDetail);

    @SqlQuery("""
            select system_code,system_id,system_type FROM system_detail WHERE
            system_code= :systemCode and system_type= :systemType
            """)
    @RegisterBeanMapper(SystemDetail.class)
    SystemDetail fetchSystemDetailsBySystemCodeAndSystemType(@Bind("systemCode") String systemCode, @Bind("systemType") String systemType);
}