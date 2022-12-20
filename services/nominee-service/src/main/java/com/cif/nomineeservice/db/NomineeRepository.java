package com.cif.nomineeservice.db;

import com.cif.nomineeservice.api.CreateNomineeRequest;
import com.cif.nomineeservice.api.NomineeMappingRequest;
import com.cif.nomineeservice.core.nominee.domain.Nominee;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.customizer.BindBean;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Set;

public interface NomineeRepository {

    @SqlUpdate("""
            INSERT INTO NOMINEE(nominee_id, party_id, salutation, first_name,
            middle_name, last_name, relation,national_id)
            VALUES (nextval('nominee_id_seq'),:nominee.partyId,
            :nominee.salutation,:nominee.firstName,:nominee.middleName,
            :nominee.lastName,:nominee.relation,:nominee.nationalId)
            """)
    @GetGeneratedKeys("nominee_id")
    int save(@BindBean("nominee") CreateNomineeRequest nominee);

    @SqlQuery("""
            SELECT * FROM NOMINEE WHERE :partyId=party_id ORDER BY nominee_id DESC
            """)
    @RegisterBeanMapper(Nominee.class)
    Set<Nominee> findByPartyId(@Bind("partyId") int partyId);

    @SqlQuery("""
            SELECT * FROM NOMINEE WHERE  nominee_id = :nomineeId
            """)
    @RegisterBeanMapper(Nominee.class)
    Nominee find(@Bind("nomineeId") int nomineeId);

    @SqlUpdate("""
            UPDATE nominee SET salutation=:nominee.salutation,first_name=:nominee.firstName,middle_name=:nominee.middleName,last_name=:nominee.lastName, relation=:nominee.relation, 
            national_id=:nominee.nationalId WHERE nominee_id=:nominee.nomineeId
            """)
    boolean updateNominee(@BindBean("nominee") Nominee nominee);

    @SqlUpdate("""
            DELETE FROM NOMINEE WHERE NOMINEE_ID= :nomineeId
            """)
    boolean delete(@Bind("nomineeId") Integer nomineeId);

    @SqlUpdate("INSERT INTO nominee_account_mapping(mapping_id, party_id, nominee_id, account_number) VALUES (nextval('nominee_account_mapping_id_seq'), :nm.partyId, :nm.nomineeId, :nm.accountNumber)")
    boolean saveNomineeMapping(@BindBean("nm") NomineeMappingRequest nomineeMappingRequest);

    @SqlUpdate("DELETE FROM nominee_account_mapping WHERE mapping_id= :nomineeMappingId")
    boolean deleteNomineeMapping(@Bind("nomineeMappingId") long nomineeMappingId);


}
