package com.cif.cifservice.db;

import com.cif.cifservice.api.*;
import com.cif.cifservice.core.party.domain.PartyDetailsView;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PartyRepositoryTest extends JdbiTest {

    private final ObjectMapper MAPPER = new ObjectMapper();
    private PartyRepository partyRepository;
    private PartyRequestCmd partyRequestCmd;
    private UpdatePartyRequestCmd updatePartyRequestCmd;
    private UpdateOccupationDetailCmd updateOccupationDetailCmd;
    private CreateAddressCmd createAddressCmd;
    private UpdateAddressCmd updateAddressCmd;
    private CreateContactDetailsCmd createContactDetailsCmd;
    private UpdateContactDetailsCmd updateContactDetailsCmd;
    private CreateDocumentCmd createDocumentCmd;
    private UpdateDocumentsCmd updateDocumentsCmd;
    private CreateRisksCmd createRisksCmd;
    private UpdateRisksCmd updateRisksCmd;
    private CreateFatcaDetailsCmd createFatcaDetailsCmd;
    private UpdateFatcaDetailsCmd updateFatcaDetailsCmd;
    private CreateMemosCmd createMemosCmd;
    private UpdateMemosCmd updateMemosCmd;
    private CreateGuardianCmd createGuardianCmd;
    private long partyId;

    @BeforeAll
    void setup() throws IOException {
        partyRepository = jdbi.onDemand(PartyRepository.class);
        partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        updateOccupationDetailCmd = Optional.ofNullable(updatePartyRequestCmd.getOccupationDetail()).orElse(new UpdateOccupationDetailCmd());
        createAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), CreateAddressCmd.class);
        updateAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateAddress.json"), UpdateAddressCmd.class);
        createContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);
        updateContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);
        createDocumentCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateDocument.json"), CreateDocumentCmd.class);
        updateDocumentsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateDocument.json"), UpdateDocumentsCmd.class);
        createRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateRisk.json"), CreateRisksCmd.class);
        updateRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateRisk.json"), UpdateRisksCmd.class);
        createFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateFatca.json"), CreateFatcaDetailsCmd.class);
        updateFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateFatca.json"), UpdateFatcaDetailsCmd.class);
        createMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateMemo.json"), CreateMemosCmd.class);
        updateMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateMemo.json"), UpdateMemosCmd.class);
        createGuardianCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateGuardian.json"), CreateGuardianCmd.class);
    }

    @Test
    @Order(1)
    void should_return_success_response_when_party_is_persist() {
        partyId = partyRepository.saveParty(partyRequestCmd.getParty(), partyRequestCmd.getOccupationDetail());
        assertThat(partyId).isPositive();
    }

    @Test
    @Order(2)
    void should_return_response_when_fetch_party() {
        List<PartyDetailsView> partyDetails = partyRepository.findPartyDetail(partyId);
        assertThat(partyDetails.size()).isGreaterThan(0);
    }

    @Test
    @Order(3)
    void should_return_empty_response_when_fetch_party() {
        List<PartyDetailsView> partyDetails = partyRepository.findPartyDetail(0L);
        assertThat(partyDetails).isEmpty();
    }

    @Test
    @Order(4)
    void should_return_true_when_party_is_updated() {
        var updatePartyFlagCmd = Optional.ofNullable(updatePartyRequestCmd.getPartyFlag()).orElse(new UpdatePartyFlagCmd());
        var result = partyRepository.updateParty(partyId, updatePartyRequestCmd.getParty(), updateOccupationDetailCmd, LocalDateTime.now(), "user", updatePartyFlagCmd);
        assertTrue(result);
    }

    @Test
    @Order(5)
    void should_return_false_when_party_is_not_updated() {
        UpdatePartyRequestCmd.builder().party(updatePartyRequestCmd.getParty().partyIdentifier("1234567890123")).occupationDetail(Optional.ofNullable(updatePartyRequestCmd.getOccupationDetail()).orElse(new UpdateOccupationDetailCmd())).build();
        var updatePartyFlagCmd = Optional.ofNullable(updatePartyRequestCmd.getPartyFlag()).orElse(new UpdatePartyFlagCmd());
        var result = partyRepository.updateParty(0L, updatePartyRequestCmd.getParty(), updatePartyRequestCmd.getOccupationDetail(), LocalDateTime.now(), "user", updatePartyFlagCmd);
        assertFalse(result);
    }

    @Test
    @Order(6)
    void should_return_success_response_when_address_is_saved() {
        var id = partyRepository.saveAddress(Collections.singleton(createAddressCmd), partyId);
        assertTrue(id[0]);
    }

    @Test
    @Order(7)
    void should_return_true_when_address_is_updated() {
        var result = partyRepository.updateAddress(Collections.singleton(updateAddressCmd), partyId);
        assertTrue(result[0]);
    }

    @Test
    @Order(8)
    void should_return_false_when_address_is_not_updated() {
        var result = partyRepository.updateAddress(Collections.singleton(updateAddressCmd), 0L);
        assertFalse(result[0]);
    }

    @Test
    @Order(9)
    void should_return_response_when_fetch_address() {
        Set<ViewAddressCmd> addressDetails = partyRepository.findAddressDetailsByIds(partyId, null);
        assertThat(addressDetails.size()).isPositive();
    }

    @Test
    @Order(10)
    void should_return_empty_response_when_fetch_address() {
        Set<ViewAddressCmd> addressDetails = partyRepository.findAddressDetailsByIds(0L, null);
        assertThat(addressDetails).isEmpty();
    }

    @Test
    @Order(11)
    void should_return_true_when_address_is_deleted() {
        boolean result = partyRepository.deleteAddressByPassedParameter(partyId, 1L);
        assertTrue(result);
    }

    @Test
    @Order(12)
    void should_return_false_when_address_is_not_deleted() {
        boolean result = partyRepository.deleteAddressByPassedParameter(partyId, 0);
        assertFalse(result);
    }

    @Test
    @Order(13)
    void should_return_success_response_when_contact_is_saved() {
        var id = partyRepository.saveContactDetails(Collections.singleton(createContactDetailsCmd), partyId);
        assertTrue(id[0]);
    }

    @Test
    @Order(14)
    void should_return_true_when_contact_is_updated() {
        var result = partyRepository.updateContactDetails(Collections.singleton(updateContactDetailsCmd), partyId);
        assertTrue(result[0]);
    }

    @Test
    @Order(15)
    void should_return_false_when_contact_is_not_updated() {
        var result = partyRepository.updateContactDetails(Collections.singleton(updateContactDetailsCmd), 0L);
        assertFalse(result[0]);
    }

    @Test
    @Order(16)
    void should_return_response_when_fetch_contact() {
        Set<ViewContactDetailsCmd> contactDetails = partyRepository.findContactDetailsById(partyId, null);
        assertThat(contactDetails.size()).isGreaterThan(0);
    }

    @Test
    @Order(17)
    void should_return_empty_response_when_fetch_contact() {
        Set<ViewContactDetailsCmd> contactDetails = partyRepository.findContactDetailsById(0L, null);
        assertThat(contactDetails).isEmpty();
    }

    @Test
    @Order(18)
    void should_return_true_when_contact_is_deleted() {
        boolean result = partyRepository.deleteContact(partyId, 1L);
        assertTrue(result);
    }

    @Test
    @Order(19)
    void should_return_false_when_contact_is_not_deleted() {
        boolean result = partyRepository.deleteAddressByPassedParameter(partyId, 0);
        assertFalse(result);
    }

    @Test
    @Order(20)
    void should_return_success_response_when_document_is_saved() {
        var id = partyRepository.saveDocument(Collections.singleton(createDocumentCmd), partyId);
        assertTrue(id[0]);
    }

    @Test
    @Order(21)
    void should_return_true_when_document_is_updated() {
        var result = partyRepository.updateDocument(Collections.singleton(updateDocumentsCmd), partyId);
        assertTrue(result[0]);
    }

    @Test
    @Order(22)
    void should_return_false_when_document_is_not_updated() {
        var result = partyRepository.updateDocument(Collections.singleton(updateDocumentsCmd), 0L);
        assertFalse(result[0]);
    }

    @Test
    @Order(23)
    void should_return_response_when_fetch_document() {
        Set<ViewDocumentsCmd> documentDetails = partyRepository.findDocumentById(partyId, null);
        assertThat(documentDetails.size()).isGreaterThan(0);
    }

    @Test
    @Order(24)
    void should_return_empty_response_when_fetch_document() {
        Set<ViewDocumentsCmd> documentDetails = partyRepository.findDocumentById(0L, null);
        assertThat(documentDetails).isEmpty();
    }

    @Test
    @Order(25)
    void should_return_true_when_document_is_deleted() {
        boolean result = partyRepository.deleteDocument(partyId, 1L);
        assertTrue(result);
    }

    @Test
    @Order(26)
    void should_return_false_when_document_is_not_deleted() {
        boolean result = partyRepository.deleteDocument(partyId, 0L);
        assertFalse(result);
    }

    @Test
    @Order(27)
    void should_return_success_response_when_risk_is_saved() {
        var id = partyRepository.saveRisks(Collections.singleton(createRisksCmd), partyId);
        assertTrue(id[0]);
    }

    @Test
    @Order(28)
    void should_return_true_when_risk_is_updated() {
        var result = partyRepository.updateRisks(Collections.singleton(updateRisksCmd), partyId);
        assertTrue(result[0]);
    }

    @Test
    @Order(29)
    void should_return_false_when_risk_is_not_updated() {
        var result = partyRepository.updateRisks(Collections.singleton(updateRisksCmd), 0L);
        assertFalse(result[0]);
    }

    @Test
    @Order(30)
    void should_return_response_when_fetch_risk() {
        Set<ViewRisksCmd> riskDetails = partyRepository.findRiskById(partyId, null);
        assertThat(riskDetails.size()).isGreaterThan(0);
    }

    @Test
    @Order(31)
    void should_return_empty_response_when_fetch_risk() {
        Set<ViewRisksCmd> riskDetails = partyRepository.findRiskById(0L, null);
        assertThat(riskDetails).isEmpty();
    }

    @Test
    @Order(32)
    void should_return_success_response_when_fatca_is_saved() {
        var id = partyRepository.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyId);
        assertTrue(id[0]);
    }


    @Test
    @Order(33)
    void should_return_true_when_fatca_is_updated() {
        var result = partyRepository.updateFatcaDetails(Collections.singleton(updateFatcaDetailsCmd), partyId);
        assertTrue(result[0]);
    }

    @Test
    @Order(34)
    void should_return_false_when_fatca_is_not_updated() {
        var result = partyRepository.updateFatcaDetails(Collections.singleton(updateFatcaDetailsCmd), 0L);
        assertFalse(result[0]);
    }

    @Test
    @Order(35)
    void should_return_response_when_fetch_fatca() {
        Set<ViewFatcaDetailsCmd> fatcaDetails = partyRepository.findFatcaById(partyId, null);
        assertThat(fatcaDetails.size()).isGreaterThan(0);
    }

    @Test
    @Order(36)
    void should_return_empty_response_when_fetch_fatca() {
        Set<ViewFatcaDetailsCmd> fatcaDetails = partyRepository.findFatcaById(0L, null);
        assertThat(fatcaDetails).isEmpty();
    }

    @Test
    @Order(37)
    void should_return_success_response_when_memo_is_saved() {
        var id = partyRepository.saveMemo(Collections.singleton(createMemosCmd), partyId);
        assertTrue(id[0]);
    }

    @Test
    @Order(38)
    void should_return_true_when_memo_is_updated() {
        var result = partyRepository.updateMemo(Collections.singleton(updateMemosCmd), partyId);
        assertTrue(result[0]);
    }

    @Test
    @Order(39)
    void should_return_false_when_memo_is_not_updated() {
        var result = partyRepository.updateMemo(Collections.singleton(updateMemosCmd), 0L);
        assertFalse(result[0]);
    }

    @Test
    @Order(40)
    void should_return_response_when_fetch_memo() {
        Set<ViewMemosCmd> memoDetails = partyRepository.findMemoById(partyId, null);
        assertThat(memoDetails.size()).isGreaterThan(0);
    }

    @Test
    @Order(41)
    void should_return_empty_response_when_fetch_memo() {
        Set<ViewMemosCmd> memoDetails = partyRepository.findMemoById(0L, null);
        assertThat(memoDetails).isEmpty();
    }

    @Test
    @Order(42)
    void should_return_true_when_party_is_deleted() {
        boolean result = partyRepository.updatePartyIsDeletedFlagByPartyId(partyId);
        assertTrue(result);
    }

    @Test
    @Order(43)
    void should_return_false_when_party_is_not_deleted() {
        boolean result = partyRepository.updatePartyIsDeletedFlagByPartyId(0L);
        assertFalse(result);
    }

    @Test
    @Order(44)
    void should_return_success_response_when_guardian_is_saved() {
        var id = partyRepository.saveGuardian(Collections.singleton(createGuardianCmd), partyId);
        assertTrue(id[0]);
    }

    @Test
    @Order(45)
    void should_return_true_when_guardian_is_deleted() {
        boolean result = partyRepository.deleteGuardian(partyId, 1L);
        assertTrue(result);
    }

    @Test
    @Order(46)
    void should_return_true_when_guardian_is_not_deleted() {
        boolean result = partyRepository.deleteGuardian(partyId, 0L);
        assertFalse(result);
    }

}
