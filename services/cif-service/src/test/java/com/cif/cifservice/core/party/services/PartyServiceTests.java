package com.cif.cifservice.core.party.services;

import ch.qos.logback.classic.Level;
import com.cif.cifservice.api.*;
import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.core.party.domain.PartyDetailsView;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.db.PartyRepository;
import com.cif.cifservice.db.PartyTransactionRepository;
import com.cif.cifservice.db.elasticsearch.ElasticsearchClientRepository;
import com.cif.cifservice.resources.exceptions.DatabasePersistException;
import com.cif.cifservice.resources.exceptions.DuplicateRecordException;
import com.cif.cifservice.resources.exceptions.InvalidParameterException;
import com.cif.cifservice.resources.exceptions.ServerSideException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.*;

import static io.dropwizard.jackson.Jackson.newObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class PartyServiceTests {

    @Mock
    private AutoCloseable closeable;

    private final PartyRepository partyRepository = mock(PartyRepository.class);
    private final PartyTransactionRepository partyTransactionRepository = mock(PartyTransactionRepository.class);

    private final ElasticsearchClientRepository clientRepository = mock(ElasticsearchClientRepository.class);

    Map<String, List<String>> universalDataFields = new HashMap<>() {{
        put("text", Collections.singletonList("test.test1"));
        put("number", Collections.singletonList("test.test1"));
        put("email", Collections.singletonList("test.test1"));
        put("date", Collections.singletonList("test.test1"));
        put("specialChar", Collections.singletonList("test.test1"));
    }};
    private final CryptoUtil cryptoUtil = mock(CryptoUtil.class);
    private PartyService partyService = new PartyService(partyRepository, partyTransactionRepository, new EventBus(), clientRepository, universalDataFields,cryptoUtil);

    private static final ObjectMapper MAPPER = newObjectMapper();
    private long partyId = 1l;

    private String partyIdentifier = "12345678912";

    private long partyAddressId = 1l;
    private long partyContactDetailsId = 1l;

    private long partyDocumentId = 1l;

    private long partyRiskId = 1l;

    private long partyFatcaDetailsId = 1l;
    private long partyMemosId = 1l;
    private long partyGuardianId = 1l;


    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void should_return_success_when_party_is_saved() throws IOException, IllegalBlockSizeException, BadPaddingException {
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        PartyDetailsView partyDetailsView = new PartyDetailsView();
        partyDetailsView.setPartyId(partyId);
        List<PartyDetailsView> partyDetailList = Collections.singletonList(partyDetailsView);
        DedupeFieldRequestCmd dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().mobileNumber(partyRequestCmd.getParty().getPrimaryMobileNumber()).build();

        when(cryptoUtil.encryptObjectUsingConfiguredField(partyRequestCmd)).thenReturn(partyRequestCmd);
        when(partyRepository.findDedupeDetails(dedupeFieldRequestCmd)).thenReturn(new ArrayList<>());
        when(partyTransactionRepository.createAllPartySection(partyRequestCmd)).thenReturn(partyId);
        when(partyRepository.findPartyDetail(partyId)).thenReturn(partyDetailList);
        Response actualResponse = partyService.save(partyRequestCmd);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(partyTransactionRepository, times(1)).createAllPartySection(partyRequestCmd);
        verify(partyRepository, times(1)).findPartyDetail(partyId);

    }

    @Test
    void should_return_response_when_exception_occur_during_save_party() throws IOException {
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        DedupeFieldRequestCmd dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().mobileNumber(partyRequestCmd.getParty().getPrimaryMobileNumber()).build();

        when(cryptoUtil.encryptObjectUsingConfiguredField(partyRequestCmd)).thenReturn(partyRequestCmd);
        when(partyRepository.findDedupeDetails(dedupeFieldRequestCmd)).thenReturn(new ArrayList<>());
        when(partyTransactionRepository.createAllPartySection(partyRequestCmd)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.save(partyRequestCmd), "Error occurred while saving the record. Please contact the system administrator ! ");

        assertEquals("Error occurred while saving the record. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(partyTransactionRepository, times(1)).createAllPartySection(partyRequestCmd);

    }

    @Test
    void should_return_success_when_fetch_party_details() {
        PartyDetailsView partyDetailsView = new PartyDetailsView();
        partyDetailsView.setPartyId(partyId);
        List<PartyDetailsView> partyDetailList = Collections.singletonList(partyDetailsView);
        PartyResponseCmd partyResponseCmd = PartyResponseCmd.builder().party(ViewPartyCmd.builder().build()).build();

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findPartyDetail(partyId)).thenReturn(partyDetailList);
		when(cryptoUtil.decryptObjectUsingConfiguredField(Mockito.any())).thenReturn(partyResponseCmd);
        Response actualResponse = partyService.getPartyDetails(Collections.singletonList(partyIdentifier));

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findPartyDetail(partyId);
    }

    @Test
    void should_return_error_when_fetch_party_details_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findPartyDetail(partyId)).thenReturn(new ArrayList<>());
        PartyResponseCmd partyResponseCmd = new PartyResponseCmd();
        when(cryptoUtil.decryptObjectUsingConfiguredField(Mockito.any())).thenReturn(partyResponseCmd);
        Response actualResponse = partyService.getPartyDetails(Collections.singletonList(partyIdentifier));

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findPartyDetail(partyId);
    }

    @Test
    void should_return_success_when_party_is_updated() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        PartyDetailsView partyDetailsView = new PartyDetailsView();
        partyDetailsView.setPartyId(partyId);
        List<PartyDetailsView> partyDetailList = Collections.singletonList(partyDetailsView);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findPartyIdByPartyIdAndMobileNumber(partyId, updatePartyRequestCmd.getParty().getPrimaryMobileNumber())).thenReturn(null);
        when(partyTransactionRepository.updateAllPartySection(updatePartyRequestCmd)).thenReturn(true);
        when(partyRepository.findPartyDetail(partyId)).thenReturn(partyDetailList);
        Response actualResponse = partyService.update(updatePartyRequestCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findPartyIdByPartyIdAndMobileNumber(partyId, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        verify(partyTransactionRepository, times(1)).updateAllPartySection(updatePartyRequestCmd);
        verify(partyRepository, times(1)).findPartyDetail(partyId);
    }

    @Test
    void should_return_error_when_party_is_not_updated() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        ;
        when(partyTransactionRepository.updateAllPartySection(updatePartyRequestCmd)).thenReturn(false);
        Response actualResponse = partyService.update(updatePartyRequestCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyTransactionRepository, times(1)).updateAllPartySection(updatePartyRequestCmd);

    }

    @Test
    void should_return_failure_when_exception_occur_during_party_update() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findPartyIdByPartyIdAndMobileNumber(partyId, updatePartyRequestCmd.getParty().getPrimaryMobileNumber())).thenReturn(null);
        when(partyTransactionRepository.updateAllPartySection(updatePartyRequestCmd)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.update(updatePartyRequestCmd), "Error occurred while updating the record. Please contact the system administrator ! ");

        assertEquals("Error occurred while updating the record. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findPartyIdByPartyIdAndMobileNumber(partyId, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        verify(partyTransactionRepository, times(1)).updateAllPartySection(updatePartyRequestCmd);

    }

    @Test
    void should_return_success_when_party_is_updated_for_soft_delete() {
        UpdatePartyRecordForSoftDeleteRequest softDeleteRequest = UpdatePartyRecordForSoftDeleteRequest.builder().partyIdentifier("12345678912").build();

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.updatePartyIsDeletedFlagByPartyId(partyId)).thenReturn(true);
        Response actualResponse = partyService.updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updatePartyIsDeletedFlagByPartyId(partyId);
    }

    @Test
    void should_return_error_when_party_not_is_updated_for_soft_delete() {
        UpdatePartyRecordForSoftDeleteRequest softDeleteRequest = UpdatePartyRecordForSoftDeleteRequest.builder().partyIdentifier("12345678912").build();

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.updatePartyIsDeletedFlagByPartyId(partyId)).thenReturn(false);
        Response actualResponse = partyService.updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updatePartyIsDeletedFlagByPartyId(partyId);
    }

    @Test
    void should_return_failure_when_exception_occur_when_party_is_updated_for_soft_delete() {
        UpdatePartyRecordForSoftDeleteRequest softDeleteRequest = UpdatePartyRecordForSoftDeleteRequest.builder().partyIdentifier("12345678912").build();

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.updatePartyIsDeletedFlagByPartyId(partyId)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest), "Error occurred while soft delete. Please contact the system administrator ! ");

        assertEquals("Error occurred while soft delete. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updatePartyIsDeletedFlagByPartyId(partyId);
    }

    @Test
    void should_return_success_when_address_is_saved() throws IOException {
        CreateAddressCmd createAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), CreateAddressCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveAddress(Collections.singleton(createAddressCmd), partyId)).thenReturn(array);
        when(partyRepository.findAddressDetailsByIds(partyId, null)).thenReturn(Collections.singleton(ViewAddressCmd.builder().build()));
        Response actualResponse = partyService.saveAddress(Collections.singleton(createAddressCmd), partyIdentifier);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveAddress(Collections.singleton(createAddressCmd), partyId);
        verify(partyRepository, times(1)).findAddressDetailsByIds(partyId, null);

    }

    @Test
    void should_return_error_when_address_is_not_saved() throws IOException {
        CreateAddressCmd createAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), CreateAddressCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveAddress(Collections.singleton(createAddressCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.saveAddress(Collections.singleton(createAddressCmd), partyIdentifier);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveAddress(Collections.singleton(createAddressCmd), partyId);
    }

    @Test
    void should_failure_when_exception_occur_during_address_is_saved() throws IOException {
        CreateAddressCmd createAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), CreateAddressCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveAddress(Collections.singleton(createAddressCmd), partyId)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.saveAddress(Collections.singleton(createAddressCmd), partyIdentifier), "Error occurred while creating address. Please contact the system administrator ! ");

        assertEquals("Error occurred while creating address. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).saveAddress(Collections.singleton(createAddressCmd), partyId);
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_address_is_updated() throws IOException {
        UpdateAddressCmd updateAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), UpdateAddressCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyTransactionRepository.updateAddress(Collections.singleton(updateAddressCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateAddress(partyIdentifier, partyAddressId, updateAddressCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyTransactionRepository, times(1)).updateAddress(Collections.singleton(updateAddressCmd), partyId);

    }

    @Test
    void should_return_error_when_address_is_not_updated() throws IOException {
        UpdateAddressCmd updateAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), UpdateAddressCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyTransactionRepository.updateAddress(Collections.singleton(updateAddressCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateAddress(partyIdentifier, partyAddressId, updateAddressCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyTransactionRepository, times(1)).updateAddress(Collections.singleton(updateAddressCmd), partyId);

    }

    @Test
    void should_return_failure_when_exception_occur_during_address_update() throws IOException {
        UpdateAddressCmd updateAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), UpdateAddressCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyTransactionRepository.updateAddress(Collections.singleton(updateAddressCmd), partyId)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.updateAddress(partyIdentifier, partyAddressId, updateAddressCmd), "Error occurred while update record. Please contact the system administrator ! ");

        assertEquals("Error occurred while update record. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyTransactionRepository, times(1)).updateAddress(Collections.singleton(updateAddressCmd), partyId);

    }

    @Test
    void should_return_success_when_fetch_address() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findAddressDetailsByIds(partyId, partyAddressId)).thenReturn(Collections.singleton(ViewAddressCmd.builder().partyAddressId(1).build()));
        Response actualResponse = partyService.fetchAddressDetails(partyIdentifier, partyAddressId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findAddressDetailsByIds(partyId, partyAddressId);
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_address() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findAddressDetailsByIds(partyId, partyAddressId)).thenReturn(new HashSet<>());
        Response actualResponse = partyService.fetchAddressDetails(partyIdentifier, partyAddressId);

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findAddressDetailsByIds(partyId, partyAddressId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_address() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());

        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.fetchAddressDetails(partyIdentifier, partyAddressId), "Error occurred while fetch address details. Please contact the system administrator ! ");

        assertEquals("Error occurred while fetch address details. Please contact the system administrator ! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_fetch_all_address() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findAddressDetailsByIds(partyId, null)).thenReturn(Collections.singleton(ViewAddressCmd.builder().partyAddressId(1).build()));
        Response actualResponse = partyService.fetchAddressDetails(partyIdentifier, null);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findAddressDetailsByIds(partyId, null);
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_delete_address() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.deleteAddressByPassedParameter(partyId, partyAddressId)).thenReturn(true);
        Response actualResponse = partyService.deleteAddressByPassedParameter(partyIdentifier, partyAddressId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).deleteAddressByPassedParameter(partyId, partyAddressId);
    }

    @Test
    void should_return_error_when_delete_address_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.deleteAddressByPassedParameter(partyId, partyAddressId)).thenReturn(false);
        Response actualResponse = partyService.deleteAddressByPassedParameter(partyIdentifier, partyAddressId);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).deleteAddressByPassedParameter(partyId, partyAddressId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_delete_address() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());

        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.deleteAddressByPassedParameter(partyIdentifier, partyAddressId), "Error occurred while deleting address. Please contact the system administrator!");

        assertEquals("Error occurred while deleting address. Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_contact_is_saved() throws IOException {
        CreateContactDetailsCmd createContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyTransactionRepository.saveContact(Collections.singleton(createContactDetailsCmd), partyId)).thenReturn(true);
        when(partyRepository.findContactDetailsById(partyId, null)).thenReturn(Collections.singleton(ViewContactDetailsCmd.builder().build()));
        Response actualResponse = partyService.saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyTransactionRepository, times(1)).saveContact(Collections.singleton(createContactDetailsCmd), partyId);
        verify(partyRepository, times(1)).findContactDetailsById(partyId, null);

    }

    @Test
    void should_return_error_when_contact_save_failed() throws IOException {
        CreateContactDetailsCmd createContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyTransactionRepository.saveContact(Collections.singleton(createContactDetailsCmd), partyId)).thenReturn(false);
        Response actualResponse = partyService.saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyTransactionRepository, times(1)).saveContact(Collections.singleton(createContactDetailsCmd), partyId);

    }

    @Test
    void should_return_failure_when_exception_occur_during_contact_save() throws IOException {
        CreateContactDetailsCmd createContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyTransactionRepository.saveContact(Collections.singleton(createContactDetailsCmd), partyId)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier), "Error occurred while saving contact. Please contact the system administrator!! ");

        assertEquals("Error occurred while saving contact. Please contact the system administrator!! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyTransactionRepository, times(1)).saveContact(Collections.singleton(createContactDetailsCmd), partyId);
    }

    @Test
    void should_return_success_when_contact_is_updated() throws IOException {
        UpdateContactDetailsCmd updateContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyTransactionRepository.updateContactDetails(partyId, updateContactDetailsCmd)).thenReturn(array[0]);
        Response actualResponse = partyService.updateContacts(partyIdentifier, partyContactDetailsId, updateContactDetailsCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_error_when_contact_update_failed() throws IOException {
        UpdateContactDetailsCmd updateContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyTransactionRepository.updateContactDetails(partyId, updateContactDetailsCmd)).thenReturn(array[0]);
        Response actualResponse = partyService.updateContacts(partyIdentifier, partyContactDetailsId, updateContactDetailsCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyTransactionRepository, times(1)).updateContactDetails(partyId, updateContactDetailsCmd);
    }


    @Test
    void should_return_failure_when_exception_occur_during_contact_update() throws IOException {
        UpdateContactDetailsCmd updateContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.updateContacts(partyIdentifier, partyContactDetailsId, updateContactDetailsCmd), "Error occurred while updating contact.Please contact the system administrator!! ");

        assertEquals("Error occurred while updating contact.Please contact the system administrator!! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_fetch_contact() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findContactDetailsById(partyId, partyContactDetailsId)).thenReturn(Collections.singleton(ViewContactDetailsCmd.builder().partyContactDetailsId(1).build()));
        Response actualResponse = partyService.fetchContactDetails(partyIdentifier, partyContactDetailsId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findContactDetailsById(partyId, partyContactDetailsId);
    }

    @Test
    void should_return_error_when_fetch_contact_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findContactDetailsById(partyId, partyContactDetailsId)).thenReturn(new HashSet<>());
        Response actualResponse = partyService.fetchContactDetails(partyIdentifier, partyContactDetailsId);

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findContactDetailsById(partyId, partyContactDetailsId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_contact() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.fetchContactDetails(partyIdentifier, partyContactDetailsId), "Error occurred while fetch contact details.Please contact the system administrator! ");

        assertEquals("Error occurred while fetch contact details.Please contact the system administrator! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_fetch_all_contacts() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findContactDetailsById(partyId, null)).thenReturn(Collections.singleton(ViewContactDetailsCmd.builder().partyContactDetailsId(1).build()));
        Response actualResponse = partyService.fetchContactDetails(partyIdentifier, null);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findContactDetailsById(partyId, null);
    }

    @Test
    void should_return_success_when_delete_contact() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.deleteContact(partyId, partyContactDetailsId)).thenReturn(true);
        Response actualResponse = partyService.deleteContact(partyIdentifier, partyContactDetailsId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).deleteContact(partyId, partyContactDetailsId);
    }

    @Test
    void should_return_error_when_delete_contact_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.deleteContact(partyId, partyContactDetailsId)).thenReturn(false);
        Response actualResponse = partyService.deleteContact(partyIdentifier, partyContactDetailsId);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).deleteContact(partyId, partyContactDetailsId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_delete_contact() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.deleteContact(partyIdentifier, partyContactDetailsId), "Error occurred while delete contact.Please contact the system administrator!");

        assertEquals("Error occurred while delete contact.Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_document_is_saved() throws IOException {
        CreateDocumentCmd createDocumentCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateDocument.json"), CreateDocumentCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyRepository.saveDocument(Collections.singleton(createDocumentCmd), partyId)).thenReturn(array);
        when(partyRepository.findDocumentById(partyId, null)).thenReturn(Collections.singleton(ViewDocumentsCmd.builder().build()));
        Response actualResponse = partyService.saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveDocument(Collections.singleton(createDocumentCmd), partyId);
        verify(partyRepository, times(1)).findDocumentById(partyId, null);

    }

    @Test
    void should_return_error_when_document_not_saved() throws IOException {
        CreateDocumentCmd createDocumentCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateDocument.json"), CreateDocumentCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyRepository.saveDocument(Collections.singleton(createDocumentCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveDocument(Collections.singleton(createDocumentCmd), partyId);

    }

    @Test
    void should_return_failure_when_exception_occur_during_document_save() throws IOException {
        CreateDocumentCmd createDocumentCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateDocument.json"), CreateDocumentCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier), "Error occurred while saving document .Please contact the system administrator!! ");

        assertEquals("Error occurred while saving document .Please contact the system administrator!! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_document_is_updated() throws IOException {
        UpdateDocumentsCmd updateDocumentsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateDocument.json"), UpdateDocumentsCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.updateDocument(Collections.singleton(updateDocumentsCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateDocuments(partyIdentifier, partyDocumentId, updateDocumentsCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updateDocument(Collections.singleton(updateDocumentsCmd), partyId);

    }

    @Test
    void should_return_error_when_document_is_not_updated() throws IOException {
        UpdateDocumentsCmd updateDocumentsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateDocument.json"), UpdateDocumentsCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.updateDocument(Collections.singleton(updateDocumentsCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateDocuments(partyIdentifier, partyDocumentId, updateDocumentsCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updateDocument(Collections.singleton(updateDocumentsCmd), partyId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_document_update() throws IOException {
        UpdateDocumentsCmd updateDocumentsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateDocument.json"), UpdateDocumentsCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.updateDocuments(partyIdentifier, partyDocumentId, updateDocumentsCmd), "Error occurred while updating document.Please contact the system administrator! ");

        assertEquals("Error occurred while updating document.Please contact the system administrator! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_fetch_document() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findDocumentById(partyId, partyDocumentId)).thenReturn(Collections.singleton(ViewDocumentsCmd.builder().partyDocumentId(1).build()));
        Response actualResponse = partyService.fetchDocuments(partyIdentifier, partyDocumentId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findDocumentById(partyId, partyDocumentId);
    }

    @Test
    void should_return_error_when_fetch_document_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findDocumentById(partyId, partyDocumentId)).thenReturn(new HashSet<>());
        Response actualResponse = partyService.fetchDocuments(partyIdentifier, partyDocumentId);

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findDocumentById(partyId, partyDocumentId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_document() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.fetchDocuments(partyIdentifier, partyDocumentId), "Error occurred while fetch document details.Please contact the system administrator!");

        assertEquals("Error occurred while fetch document details.Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_fetch_all_documents() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findDocumentById(partyId, null)).thenReturn(Collections.singleton(ViewDocumentsCmd.builder().partyDocumentId(1).build()));
        Response actualResponse = partyService.fetchDocuments(partyIdentifier, null);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findDocumentById(partyId, null);
    }

    @Test
    void should_return_success_when_delete_document() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.deleteDocument(partyId, partyDocumentId)).thenReturn(true);
        Response actualResponse = partyService.deleteDocuments(partyIdentifier, partyDocumentId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).deleteDocument(partyId, partyDocumentId);
    }

    @Test
    void should_return_error_when_delete_document_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.deleteDocument(partyId, partyDocumentId)).thenReturn(false);
        Response actualResponse = partyService.deleteDocuments(partyIdentifier, partyDocumentId);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).deleteDocument(partyId, partyDocumentId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_delete_document() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());

        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.deleteDocuments(partyIdentifier, partyDocumentId), "Error occurred while deleting document .Please contact the system administrator!");

        assertEquals("Error occurred while deleting document .Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_risk_is_saved() throws IOException {
        CreateRisksCmd createRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateRisk.json"), CreateRisksCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveRisks(Collections.singleton(createRisksCmd), partyId)).thenReturn(array);
        when(partyRepository.findRiskById(partyId, null)).thenReturn(Collections.singleton(ViewRisksCmd.builder().build()));
        Response actualResponse = partyService.saveRisk(Collections.singleton(createRisksCmd), partyIdentifier);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveRisks(Collections.singleton(createRisksCmd), partyId);
        verify(partyRepository, times(1)).findRiskById(partyId, null);

    }

    @Test
    void should_return_error_when_risk_is_not_saved() throws IOException {
        CreateRisksCmd createRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateRisk.json"), CreateRisksCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveRisks(Collections.singleton(createRisksCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.saveRisk(Collections.singleton(createRisksCmd), partyIdentifier);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveRisks(Collections.singleton(createRisksCmd), partyId);

    }

    @Test
    void should_return_failure_when_exception_occur_during_risk_save() throws IOException {
        CreateRisksCmd createRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateRisk.json"), CreateRisksCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.saveRisk(Collections.singleton(createRisksCmd), partyIdentifier), "Error occurred while saving risk .Please contact the system administrator! ");

        assertEquals("Error occurred while saving risk .Please contact the system administrator! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_risk_is_updated() throws IOException {
        UpdateRisksCmd updateRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateRisk.json"), UpdateRisksCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyRepository.updateRisks(Collections.singleton(updateRisksCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateRisk(partyIdentifier, partyAddressId, updateRisksCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updateRisks(Collections.singleton(updateRisksCmd), partyId);

    }

    @Test
    void should_return_error_when_risk_is_not_update() throws IOException {
        UpdateRisksCmd updateRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateRisk.json"), UpdateRisksCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyRepository.updateRisks(Collections.singleton(updateRisksCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateRisk(partyIdentifier, partyAddressId, updateRisksCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updateRisks(Collections.singleton(updateRisksCmd), partyId);

    }

    @Test
    void should_return_failure_when_exception_occur_during_risk_update() throws IOException {
        UpdateRisksCmd updateRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateRisk.json"), UpdateRisksCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.updateRisk(partyIdentifier, partyRiskId, updateRisksCmd), "Error occurred while updating risk .Please contact the system administrator! ");

        assertEquals("Error occurred while updating risk .Please contact the system administrator! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_fetch_risk() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findRiskById(partyId, partyRiskId)).thenReturn(Collections.singleton(ViewRisksCmd.builder().partyRiskId(1).build()));
        Response actualResponse = partyService.fetchRisks(partyIdentifier, partyRiskId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findRiskById(partyId, partyRiskId);
    }

    @Test
    void should_return_error_when_fetch_risk_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findRiskById(partyId, partyRiskId)).thenReturn(new HashSet<>());
        Response actualResponse = partyService.fetchRisks(partyIdentifier, partyRiskId);

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findRiskById(partyId, partyRiskId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_risk() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.fetchRisks(partyIdentifier, partyRiskId), "Error occurred while fetching risk details .Please contact the system administrator!");

        assertEquals("Error occurred while fetching risk details .Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_fetch_all_risks() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findRiskById(partyId, null)).thenReturn(Collections.singleton(ViewRisksCmd.builder().partyRiskId(1).build()));
        Response actualResponse = partyService.fetchRisks(partyIdentifier, null);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findRiskById(partyId, null);
    }

    @Test
    void should_return_success_when_fatca_is_saved() throws IOException {
        CreateFatcaDetailsCmd createFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateFatca.json"), CreateFatcaDetailsCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyRepository.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyId)).thenReturn(array);
        when(partyRepository.findFatcaById(partyId, null)).thenReturn(Collections.singleton(ViewFatcaDetailsCmd.builder().build()));
        Response actualResponse = partyService.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyId);
        verify(partyRepository, times(1)).findFatcaById(partyId, null);

    }

    @Test
    void should_return_error_when_fatca_is_not_saved() throws IOException {
        CreateFatcaDetailsCmd createFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateFatca.json"), CreateFatcaDetailsCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyId);

    }

    @Test
    void should_return_failure_when_exception_occur_during_fatca_is_saved() throws IOException {
        CreateFatcaDetailsCmd createFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateFatca.json"), CreateFatcaDetailsCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier), "Error occurred while saving fatca details. Please contact the system administrator!");

        assertEquals("Error occurred while saving fatca details. Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_fatca_is_updated() throws IOException {
        UpdateFatcaDetailsCmd updateFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateFatca.json"), UpdateFatcaDetailsCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyRepository.updateFatcaDetails(Collections.singleton(updateFatcaDetailsCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateFatcaDetails(partyIdentifier, partyFatcaDetailsId, updateFatcaDetailsCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updateFatcaDetails(Collections.singleton(updateFatcaDetailsCmd), partyId);

    }

    @Test
    void should_return_error_when_fatca_is_not_updated() throws IOException {
        UpdateFatcaDetailsCmd updateFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateFatca.json"), UpdateFatcaDetailsCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(1l);
        when(partyRepository.updateFatcaDetails(Collections.singleton(updateFatcaDetailsCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateFatcaDetails(partyIdentifier, partyFatcaDetailsId, updateFatcaDetailsCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updateFatcaDetails(Collections.singleton(updateFatcaDetailsCmd), partyId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fatca_update() throws IOException {
        UpdateFatcaDetailsCmd updateFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateFatca.json"), UpdateFatcaDetailsCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.updateFatcaDetails(partyIdentifier, partyFatcaDetailsId, updateFatcaDetailsCmd), "Error occurred while updating fatca details .Please contact the system administrator!");

        assertEquals("Error occurred while updating fatca details .Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_fetch_fatca() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findFatcaById(partyId, partyFatcaDetailsId)).thenReturn(Collections.singleton(ViewFatcaDetailsCmd.builder().partyFatcaDetailsId(1).build()));
        Response actualResponse = partyService.fetchFatcaDetails(partyIdentifier, partyFatcaDetailsId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findFatcaById(partyId, partyFatcaDetailsId);
    }

    @Test
    void should_return_error_when_fetch_fatca_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findFatcaById(partyId, partyFatcaDetailsId)).thenReturn(new HashSet<>());
        Response actualResponse = partyService.fetchFatcaDetails(partyIdentifier, partyFatcaDetailsId);

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findFatcaById(partyId, partyFatcaDetailsId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_fatca() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.fetchFatcaDetails(partyIdentifier, partyFatcaDetailsId), "Error occurred while fetching fatca details . Please contact the system administrator!");

        assertEquals("Error occurred while fetching fatca details . Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_fetch_all_fatca() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findFatcaById(partyId, null)).thenReturn(Collections.singleton(ViewFatcaDetailsCmd.builder().partyFatcaDetailsId(1).build()));
        Response actualResponse = partyService.fetchFatcaDetails(partyIdentifier, null);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findFatcaById(partyId, null);
    }

    @Test
    void should_return_success_when_memo_is_saved() throws IOException {
        CreateMemosCmd createMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateMemo.json"), CreateMemosCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveMemo(Collections.singleton(createMemosCmd), partyId)).thenReturn(array);
        when(partyRepository.findMemoById(partyId, null)).thenReturn(Collections.singleton(ViewMemosCmd.builder().build()));
        Response actualResponse = partyService.saveMemos(Collections.singleton(createMemosCmd), partyIdentifier);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveMemo(Collections.singleton(createMemosCmd), partyId);
        verify(partyRepository, times(1)).findMemoById(partyId, null);

    }

    @Test
    void should_return_error_when_memo_is_not_saved() throws IOException {
        CreateMemosCmd createMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateMemo.json"), CreateMemosCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveMemo(Collections.singleton(createMemosCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.saveMemos(Collections.singleton(createMemosCmd), partyIdentifier);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveMemo(Collections.singleton(createMemosCmd), partyId);

    }

    @Test
    void should_return_failure_when_exception_occur_during_memo_save() throws IOException {
        CreateMemosCmd createMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateMemo.json"), CreateMemosCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.saveMemos(Collections.singleton(createMemosCmd), partyIdentifier), "Error occurred while saving memo.Please contact the system administrator!");

        assertEquals("Error occurred while saving memo.Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_memo_is_updated() throws IOException {
        UpdateMemosCmd updateMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateMemo.json"), UpdateMemosCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.updateMemo(Collections.singleton(updateMemosCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateMemos(partyIdentifier, partyMemosId, updateMemosCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updateMemo(Collections.singleton(updateMemosCmd), partyId);

    }

    @Test
    void should_return_error_when_memo_not_is_updated() throws IOException {
        UpdateMemosCmd updateMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateMemo.json"), UpdateMemosCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.updateMemo(Collections.singleton(updateMemosCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.updateMemos(partyIdentifier, partyMemosId, updateMemosCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).updateMemo(Collections.singleton(updateMemosCmd), partyId);
    }


    @Test
    void should_return_failure_when_exception_occur_during_memo_update() throws IOException {
        UpdateMemosCmd updateMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateMemo.json"), UpdateMemosCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.updateMemos(partyIdentifier, partyMemosId, updateMemosCmd), "Error occurred while update memos details .Please contact the system administrator!");

        assertEquals("Error occurred while update memos details .Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_fetch_memo() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findMemoById(partyId, partyMemosId)).thenReturn(Collections.singleton(ViewMemosCmd.builder().partyMemoId(1).build()));
        Response actualResponse = partyService.fetchMemos(partyIdentifier, partyMemosId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findMemoById(partyId, partyMemosId);
    }

    @Test
    void should_return_error_when_fetch_memo_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findMemoById(partyId, partyMemosId)).thenReturn(new HashSet<>());
        Response actualResponse = partyService.fetchMemos(partyIdentifier, partyMemosId);

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findMemoById(partyId, partyMemosId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_memo() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findMemoById(partyId, partyMemosId)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.fetchMemos(partyIdentifier, partyMemosId), "Error occurred while fetching memos details .Please contact the system administrator!");

        assertEquals("Error occurred while fetching memos details .Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findMemoById(partyId, partyMemosId);
    }

    @Test
    void should_return_success_when_fetch_all_memos() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findMemoById(partyId, null)).thenReturn(Collections.singleton(ViewMemosCmd.builder().partyMemoId(1).build()));
        Response actualResponse = partyService.fetchMemos(partyIdentifier, null);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findMemoById(partyId, null);
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

    @Test
    void should_return_success_when_dedupe_verification_is_done() {
        DedupeFieldRequestCmd dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().countryCode("+91").mobileNumber("9898989898").build();

        when(partyRepository.findDedupeDetails(dedupeFieldRequestCmd)).thenReturn(Collections.singletonList(partyId));
        Response actualResponse = partyService.checkDedupe(dedupeFieldRequestCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findDedupeDetails(dedupeFieldRequestCmd);

    }

    @Test
    void should_return_failure_when_exception_occur_during_dedupe_verification() {
        DedupeFieldRequestCmd dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().countryCode("+91").mobileNumber("9898989898").build();

        when(partyRepository.findDedupeDetails(dedupeFieldRequestCmd)).thenThrow(new RuntimeException("Exception occurred"));
        Response actualResponse = partyService.checkDedupe(dedupeFieldRequestCmd);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findDedupeDetails(dedupeFieldRequestCmd);
    }

    @Test
    void should_return_success_when_search_is_done() {
        PartyDistinctiveSearchCmd partyDistinctiveSearchCmd = PartyDistinctiveSearchCmd.builder().partyIdentifier("12345678912").firstName("Ramesh").mobileNumber("9999999999").build();
        when(cryptoUtil.encryptObjectUsingConfiguredField(partyDistinctiveSearchCmd)).thenReturn(partyDistinctiveSearchCmd);
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.fetchPartyDetailsByPassedParameter(partyId, partyDistinctiveSearchCmd, "first_name", "asc", 100, 1, true)).thenReturn(Collections.singletonList(PartyRecordCmd.builder().build()));
        Response actualResponse = partyService.getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).fetchPartyDetailsByPassedParameter(partyId, partyDistinctiveSearchCmd, "first_name", "asc", 100, 1, true);
    }

    @Test
    void should_return_error_when_search_is_failed() {
        PartyDistinctiveSearchCmd partyDistinctiveSearchCmd = PartyDistinctiveSearchCmd.builder().partyIdentifier("12345678912").firstName("Ramesh").mobileNumber("9999999999").build();
        when(cryptoUtil.encryptObjectUsingConfiguredField(partyDistinctiveSearchCmd)).thenReturn(partyDistinctiveSearchCmd);
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.fetchPartyDetailsByPassedParameter(partyId, partyDistinctiveSearchCmd, "first_name", "asc", 100, 1, true)).thenReturn(new ArrayList<>());
        Response actualResponse = partyService.getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1);

        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).fetchPartyDetailsByPassedParameter(partyId, partyDistinctiveSearchCmd, "first_name", "asc", 100, 1, true);
    }


    @Test
    void should_failure_when_exception_occur_during_search() {
        PartyDistinctiveSearchCmd partyDistinctiveSearchCmd = PartyDistinctiveSearchCmd.builder().partyIdentifier("12345678912").firstName("Ramesh").mobileNumber("9999999999").build();
        when(cryptoUtil.encryptObjectUsingConfiguredField(partyDistinctiveSearchCmd)).thenReturn(partyDistinctiveSearchCmd);
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.fetchPartyDetailsByPassedParameter(partyId, partyDistinctiveSearchCmd, "first_name", "asc", 100, 1, true)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1), "Error occurred while searching record . Please contact the system administrator! ");

        assertEquals("Error occurred while searching record . Please contact the system administrator! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).fetchPartyDetailsByPassedParameter(partyId, partyDistinctiveSearchCmd, "first_name", "asc", 100, 1, true);
    }

    @Test
    void should_return_success_when_email_is_updated() {
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier("12345678912").primaryEmail("sample@test.com").build()).build();
        PartyDetailsView partyDetailsView = new PartyDetailsView();
        partyDetailsView.setPartyId(partyId);
        List<PartyDetailsView> partyDetailList = Collections.singletonList(partyDetailsView);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findPartyIdByPartyIdAndMobileNumber(partyId, "9898979796")).thenReturn(1l);
        when(partyTransactionRepository.updateAllPartySection(updatePartyRequestCmd)).thenReturn(true);
        when(partyRepository.findPartyDetail(partyId)).thenReturn(partyDetailList);
        Response actualResponse = partyService.update(updatePartyRequestCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyTransactionRepository, times(1)).updateAllPartySection(updatePartyRequestCmd);
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findPartyDetail(partyId);
    }

    @Test
    void should_return_success_when_advance_search_is_done() {
        AdvanceSearchRequestCmd advanceSearchRequestCmd = AdvanceSearchRequestCmd.builder().build();

        when(clientRepository.getDocumentData(advanceSearchRequestCmd)).thenReturn(new HashSet<>());
        Response actualResponse = partyService.fetchDataUsingElasticSearch(advanceSearchRequestCmd);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(clientRepository, times(1)).getDocumentData(advanceSearchRequestCmd);
    }

    @Test
    void should_return_exception_when_exception_occur_during_advance_search() {
        AdvanceSearchRequestCmd advanceSearchRequestCmd = AdvanceSearchRequestCmd.builder().build();

        when(clientRepository.getDocumentData(advanceSearchRequestCmd)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.fetchDataUsingElasticSearch(advanceSearchRequestCmd), "Error occurred while performing an advanced search .Please contact the system administrator!");

        assertEquals("Error occurred while performing an advanced search .Please contact the system administrator!", databasePersistException.getMessage());
        verify(clientRepository, times(1)).getDocumentData(advanceSearchRequestCmd);
    }

    @Test
    void should_throw_exception_when_contact_value_mobile_number_is_invalid() throws IOException {
        String contactTypeCode = "01";
        String contactValue = "858703245";

        when(partyTransactionRepository.findTypeValueByCode(contactTypeCode)).thenReturn("Mobile");
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> partyService.validateContact(contactTypeCode, contactValue), "Mobile number should be numeric and in range of 10 to 12 digits");
        assertEquals("Mobile number should be numeric and in range of 10 to 12 digits", invalidParameterException.getMessage());
    }

    @Test
    void should_return_exception_when_contact_value_email_is_invalid() {
        String contactTypeCode = "02";
        String contactValue = "abhi@";
        when(partyTransactionRepository.findTypeValueByCode(contactTypeCode)).thenReturn("Email");
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> partyService.validateContact(contactTypeCode, contactValue), "Invalid email id ");
        assertEquals("Invalid email id ", invalidParameterException.getMessage());


    }

    @Test
    void should_return_failure_when_exception_occur_during_validate_party_record() {
        when(partyRepository.findPartyIdByPartyIdAndMobileNumber(1, "9898989898")).thenReturn(1l);

        DuplicateRecordException duplicateRecordException = assertThrows(DuplicateRecordException.class, () -> partyService.validatePartyRecord(1, "9898989898"), "The record is already registered with a mobile number!");

        assertEquals("The record is already registered with a mobile number!", duplicateRecordException.getMessage());

    }

    @Test
    void should_throw_exception_when_contact_value_email_is_invalid() throws IOException {
        String contactTypeCode = "02";
        String contactValue = "abhi@";

        when(partyTransactionRepository.findTypeValueByCode(contactTypeCode)).thenReturn("Email");
        InvalidParameterException invalidParameterException = assertThrows(InvalidParameterException.class, () -> partyService.validateContact(contactTypeCode, contactValue), "Invalid email id ");
        assertEquals("Invalid email id ", invalidParameterException.getMessage());


    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_party_details() {
        when(partyRepository.findPartyDetail(1l)).thenThrow(new RuntimeException());

        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.fetchPartyDetails(1l), "Error occurred while fetch party details. Please contact the system administrator ! ");

        assertEquals("Error occurred while fetch party details. Please contact the system administrator ! ", databasePersistException.getMessage());

    }

    @Test
    void should_return_data_when_universal_search_success() throws IOException {
        Config configurationData = MAPPER.readValue(getClass().getResource("/fixtures/CreateAdmin.json"), Config.class);
        UniversalSearchRequest request = UniversalSearchRequest.builder().searchFor("abc").build();

        when(clientRepository.getDocumentData(Mockito.any(AdvanceSearchRequestCmd.class))).thenReturn(Mockito.anySet());
        when(cryptoUtil.getUniversalSearchConfigData(configurationData)).thenReturn(configurationData);

        Response textSearchResponse = partyService.performUniversalSearchBasedOnUserData(request);
        request.setSearchFor("123");
        Response numberSearchResponse = partyService.performUniversalSearchBasedOnUserData(request);
        request.setSearchFor("ABC123");
        Response numberTextSearchResponse = partyService.performUniversalSearchBasedOnUserData(request);


        assertEquals(HttpStatus.OK_200, textSearchResponse.getStatus());
        assertEquals(HttpStatus.OK_200, numberSearchResponse.getStatus());
        assertEquals(HttpStatus.OK_200, numberTextSearchResponse.getStatus());

        verify(clientRepository, times(3)).getDocumentData(Mockito.any(AdvanceSearchRequestCmd.class));
    }

    @Test
    void should_throw_exception_when_universal_search_failed() throws IOException {
        Config configurationData = MAPPER.readValue(getClass().getResource("/fixtures/CreateAdmin.json"), Config.class);
        UniversalSearchRequest request = UniversalSearchRequest.builder().searchFor("abc@test.com").build();

        when(clientRepository.getDocumentData(Mockito.any(AdvanceSearchRequestCmd.class))).thenThrow(new RuntimeException());
        when(cryptoUtil.getUniversalSearchConfigData(configurationData)).thenReturn(configurationData);

        ServerSideException emailSearchException = assertThrows(ServerSideException.class, () -> partyService.performUniversalSearchBasedOnUserData(request));
        request.setSearchFor("123");
        ServerSideException numberSearchException = assertThrows(ServerSideException.class, () -> partyService.performUniversalSearchBasedOnUserData(request));

        assertEquals("Error occurred while performing universal search .Please contact the system administrator!", emailSearchException.getMessage());
        assertEquals("Error occurred while performing universal search .Please contact the system administrator!", numberSearchException.getMessage());
        verify(clientRepository, times(2)).getDocumentData(Mockito.any(AdvanceSearchRequestCmd.class));
    }
    @Test
    void should_return_success_when_fetch_all_xref() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findXref(partyId)).thenReturn(Collections.singleton(ViewXrefsCmd.builder().partyXrefId(1).build()));
        Response actualResponse = partyService.fetchXref(partyIdentifier);


        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findXref(partyId);

    }
    @Test
    void should_return_failure_when_exception_occur_during_fetch_all_xref() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () ->  partyService.fetchXref(partyIdentifier), " Error occurred while fetching xref details .Please contact the system administrator!");


        assertEquals("Error occurred while fetching xref details .Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);


    }

    @Test
    void should_return_error_when_fetch_all_xref_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.findXref(partyId)).thenReturn(new HashSet<>());
        Response actualResponse = partyService.fetchXref(partyIdentifier);


        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).findXref(partyId);

    }

    @Test
    void should_return_success_when_guardian_is_saved() throws IOException {
        CreateGuardianCmd createGuardianCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateGuardian.json"), CreateGuardianCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.TRUE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveGuardian(Collections.singleton(createGuardianCmd), partyId)).thenReturn(array);
        when(partyRepository.findGuardianById(partyId, null)).thenReturn(Collections.singleton(ViewGuardiansCmd.builder().build()));
        Response actualResponse = partyService.saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier);

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveGuardian(Collections.singleton(createGuardianCmd), partyId);
        verify(partyRepository, times(1)).findGuardianById(partyId, null);

    }

    @Test
    void should_return_error_when_guardian_is_not_saved() throws IOException {
        CreateGuardianCmd createGuardianCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateGuardian.json"), CreateGuardianCmd.class);
        boolean[] array = new boolean[1];
        Arrays.fill(array, Boolean.FALSE);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.saveGuardian(Collections.singleton(createGuardianCmd), partyId)).thenReturn(array);
        Response actualResponse = partyService.saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).saveGuardian(Collections.singleton(createGuardianCmd), partyId);

    }

    @Test
    void should_return_failure_when_exception_occur_during_guardian_save() throws IOException {
        CreateGuardianCmd createGuardianCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateGuardian.json"), CreateGuardianCmd.class);

        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier), "Error occurred while saving guardian .Please contact the system administrator! ");

        assertEquals("Error occurred while saving guardian .Please contact the system administrator! ", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);

    }

    @Test
    void should_return_success_when_delete_guardian() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.deleteGuardian(partyId, partyGuardianId)).thenReturn(true);
        Response actualResponse = partyService.deleteGuardians(partyIdentifier, partyGuardianId);

        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).deleteGuardian(partyId, partyGuardianId);
    }

    @Test
    void should_return_error_when_delete_guardian_is_failed() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenReturn(partyId);
        when(partyRepository.deleteGuardian(partyId, partyGuardianId)).thenReturn(false);
        Response actualResponse = partyService.deleteGuardians(partyIdentifier, partyGuardianId);

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
        verify(partyRepository, times(1)).deleteGuardian(partyId, partyGuardianId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_delete_guardian() {
        when(partyRepository.findIdByPartyIdentifier(partyIdentifier)).thenThrow(new RuntimeException());
        DatabasePersistException databasePersistException = assertThrows(DatabasePersistException.class, () -> partyService.deleteGuardians(partyIdentifier, partyGuardianId), "Error occurred while deleting guardian .Please contact the system administrator!");

        assertEquals("Error occurred while deleting guardian .Please contact the system administrator!", databasePersistException.getMessage());
        verify(partyRepository, times(1)).findIdByPartyIdentifier(partyIdentifier);
    }

}
