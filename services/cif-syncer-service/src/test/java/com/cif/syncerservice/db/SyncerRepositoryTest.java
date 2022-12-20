package com.cif.syncerservice.db;

import com.cif.syncerservice.core.syncer.domain.PartyXref;
import com.cif.syncerservice.core.syncer.domain.SyncerTransaction;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SyncerRepositoryTest extends JdbiTest {
    private SyncerRepository syncerRepository;

    private int partyXrefId;
    private SyncerTransaction syncerTransaction;

    @BeforeAll
    void setup() {
        syncerRepository = jdbi.onDemand(SyncerRepository.class);
        syncerTransaction = new SyncerTransaction();
        syncerTransaction.setEventTransactionId("1");
        syncerTransaction.setEventType("TEST");
        syncerTransaction.setPartyId("1");
        syncerTransaction.setSystemCode("TEST");
        syncerTransaction.setSystemId(1);
        syncerTransaction.setApiRequest("{\"firstName\":\"Test\"}");
        syncerTransaction.setApiResponse("{\"firstName\":\"Test\"}");
        syncerTransaction.setStatus("In-Progress");
    }

    @Test
    @Order(1)
    void savePartyXrefRecord() {
        PartyXref partyXref = createPartyXrefTestData();

        partyXrefId = syncerRepository.savePartyXrefRecord(partyXref);

        assertNotNull(partyXrefId);
    }

    @Test
    @Order(2)
    void fetchPartyXrefByPartyId() {
        var response = syncerRepository.fetchPartyXrefByPartyId("1", 1);

        assertNotNull(response);
        assertThat(response.getPartyXrefId()).isEqualTo(partyXrefId);
    }

    @Test
    @Order(3)
    void saveTransactionHistory() {
        var syncerTransactionId = syncerRepository.saveTransactionHistory(syncerTransaction);
        syncerTransaction.setSyncerTransactionId(syncerTransactionId);

        assertNotNull(syncerTransactionId);
    }

    @Test
    @Order(4)
    void fetchSyncerTransactionDataByEventTransactionID() {
        var response = syncerRepository.fetchSyncerTransactionDataByEventTransactionID(syncerTransaction.getEventTransactionId(), 1);

        assertEquals(syncerTransaction.getPartyId(), response.getPartyId());
    }

    @Test
    @Order(5)
    void updateTransactionHistory() {
        syncerTransaction.setStatus("Completed");
        syncerTransaction.setUpdatedAt(LocalDateTime.now());

        var syncerTransactionUpdateId = syncerRepository.updateTransactionHistory(syncerTransaction);

        assertTrue(syncerTransactionUpdateId);
    }

    private PartyXref createPartyXrefTestData() {
        PartyXref partyXref = new PartyXref();
        partyXref.setPartyId("1");
        partyXref.setSystemCode("TEST");
        partyXref.setSystemId(1);
        partyXref.setXrefId("1");
        return partyXref;
    }
}