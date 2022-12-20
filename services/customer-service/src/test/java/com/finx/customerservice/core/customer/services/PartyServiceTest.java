package com.finx.customerservice.core.customer.services;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.customerservice.api.*;
import com.finx.customerservice.core.customer.domain.Party;
import com.finx.customerservice.core.customer.domain.PartyDetailsView;
import com.finx.customerservice.db.PartyRepository;
import com.finx.customerservice.db.PartyTransactionRepository;
import com.finx.customerservice.resources.exceptions.DatabaseException;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class PartyServiceTest {

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    private final PartyRepository mockPartyRepository = mock(PartyRepository.class);
    private final PartyTransactionRepository mockPartyTransactionRepository = mock(PartyTransactionRepository.class);
    private final PartyService partyService = new PartyService(mockPartyRepository, mockPartyTransactionRepository);

    private final ObjectMapper MAPPER = new ObjectMapper();

    @Test
    void Should_Return_Party_Record_When_MobileNumber_Exists() {
        PartySearchCmd partySearchRequest = new PartySearchCmd();
        partySearchRequest.setMobileNumber("1111111111");
        Party party = new Party();
        party.setPartyId(1L);
        party.setPrimaryMobileNumber("1111111111");
        party.setDateOfBirth(LocalDateTime.now().toLocalDate());
        party.setFirstName("Test1");
        party.setLastName("Test2");
        party.setMiddleName("Test3");
        party.setPrimaryEmail("test@test.com");

        when(mockPartyRepository.fetchPartyDetailsByMobileNumber(partySearchRequest.getMobileNumber())).thenReturn(singletonList(party));
        PartySearchResponse response = partyService.findPartyDetailsByMobileNumber(partySearchRequest);

        assertThat(response.getData().get(0).getPartyId()).isEqualTo(1L);
        verify(mockPartyRepository, times(1)).fetchPartyDetailsByMobileNumber(partySearchRequest.getMobileNumber());
    }

    @Test
    void Should_Return_No_Record_When_MobileNumber_Not_Exists() {
        PartySearchCmd partySearchRequest = new PartySearchCmd();
        partySearchRequest.setMobileNumber("1111111111");

        when(mockPartyRepository.fetchPartyDetailsByMobileNumber(partySearchRequest.getMobileNumber())).thenReturn(Collections.EMPTY_LIST);
        PartySearchResponse response = partyService.findPartyDetailsByMobileNumber(partySearchRequest);

        assertThat(response.getData().size()).isEqualTo(0);
        verify(mockPartyRepository, times(1)).fetchPartyDetailsByMobileNumber(partySearchRequest.getMobileNumber());
    }

    @Test
    void Should_Return_Failure_Response_When_Party_Record_Fetch_Failed() {
        PartySearchCmd partySearchRequest = new PartySearchCmd();
        partySearchRequest.setMobileNumber("1111111111");

        when(mockPartyRepository.fetchPartyDetailsByMobileNumber(partySearchRequest.getMobileNumber())).thenThrow(new RuntimeException());
        DatabaseException thrown = assertThrows(
                DatabaseException.class,
                () -> partyService.findPartyDetailsByMobileNumber(partySearchRequest),
                "Error occurred while fetching party detail"
        );

        assertThat(thrown.getMessage()).isEqualTo("Error occurred while fetching party detail");
        verify(mockPartyRepository, times(1)).fetchPartyDetailsByMobileNumber(partySearchRequest.getMobileNumber());
    }

    @Test
    void should_return_response_when_party_detail_is_saved() throws IOException {
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        Integer partyId = partyRequestCmd.getParty().getPartyId();
        OccupationDetailCmd occupationDetailCmd = partyRequestCmd.getOccupationDetail();
        PartyResponseCmd partyResponseCmd = new PartyResponseCmd();
        PartyCmd partyInfo = new PartyCmd();
        partyInfo.setPartyId(partyId);
        partyResponseCmd.setParty(partyInfo);
        PartyDetailsView partyDetailsView = new PartyDetailsView();
        partyDetailsView.setPartyId(partyId.longValue());
        List<PartyDetailsView> partyDetailList = Collections.singletonList(partyDetailsView);
        PartyFlagCmd partyFlagCmd = partyRequestCmd.getPartyFlag();

        when(mockPartyTransactionRepository.createAllPartyRelatedRecords(partyRequestCmd, occupationDetailCmd, partyFlagCmd)).thenReturn(partyId.longValue());
        when(mockPartyRepository.findPartyDetail(partyId.longValue())).thenReturn(partyDetailList);
        PartyResponseCmd response = partyService.save(partyRequestCmd);

        assertThat(response.getParty().getPartyId()).isEqualTo(partyId);
        verify(mockPartyTransactionRepository, times(1)).createAllPartyRelatedRecords(partyRequestCmd, occupationDetailCmd, partyFlagCmd);
        verify(mockPartyRepository, times(1)).findPartyDetail(partyId.longValue());

    }

    @Test
    void should_return_failure_response_when_exception_occur_during_save() throws IOException {
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        OccupationDetailCmd occupationDetailCmd = partyRequestCmd.getOccupationDetail();
        PartyFlagCmd partyFlagCmd = partyRequestCmd.getPartyFlag();
        when(mockPartyTransactionRepository.createAllPartyRelatedRecords(partyRequestCmd, occupationDetailCmd, partyFlagCmd)).thenThrow(new RuntimeException());
        DatabaseException databaseException = assertThrows(
                DatabaseException.class, () -> partyService.save(partyRequestCmd),
                "Failed To Create Party");

        assertEquals("Failed To Create Party", databaseException.getMessage());
        verify(mockPartyTransactionRepository, times(1)).createAllPartyRelatedRecords(partyRequestCmd, occupationDetailCmd, partyFlagCmd);
    }

    @Test
    void should_return_response_when_party_detail_found_during_fetch() {
        Long partyId = 1l;
        PartyDetailsView partyDetailsView = new PartyDetailsView();
        partyDetailsView.setPartyId(partyId);
        List<PartyDetailsView> partyDetailsViewList = Collections.singletonList(partyDetailsView);
        when(mockPartyRepository.findPartyDetail(partyId)).thenReturn(partyDetailsViewList);
        PartyResponseCmd partyResponseCmd = partyService.fetchPartyDetails(partyId);
        assertEquals(partyResponseCmd.getParty().getPartyId().longValue(), partyDetailsViewList.get(0).getPartyId().longValue());
        verify(mockPartyRepository, times(1)).findPartyDetail(partyId);
    }

    @Test
    void should_return_null_response_when_party_detail_not_found_during_fetch() throws IOException {
        Long partyId = 3l;
        List<PartyDetailsView> partyDetailsViewList = new ArrayList<>();
        when(mockPartyRepository.findPartyDetail(partyId)).thenReturn(partyDetailsViewList);
        PartyResponseCmd partyResponseCmd = partyService.fetchPartyDetails(partyId);
        assertThat(partyResponseCmd.getParty()).isNull();
        verify(mockPartyRepository, times(1)).findPartyDetail(partyId);
    }

    @Test
    void should_return_failure_response_when_exception_occurs_during_fetch() throws IOException {
        Long partyId = 1l;
        when(mockPartyRepository.findPartyDetail(partyId)).thenThrow(new RuntimeException());
        DatabaseException databaseException = assertThrows(DatabaseException.class,
                () -> partyService.fetchPartyDetails(partyId),
                "Failed To Get Party Details");
        assertEquals("Failed To Get Party Details",databaseException.getMessage());
        verify(mockPartyRepository, times(1)).findPartyDetail(partyId);
    }

    @Test
    void should_return_null_when_validation_success() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        ErrorMessage response = partyService.validatePartyRecord(partyRequestCmd.getParty());
        assertThat(response).isNull();
    }


    @Test
    void should_return_error_when_gender_validation_failed() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        partyRequestCmd = partyRequestCmd.builder().party(PartyCmd.builder().gender("Test").build()).build();
        ErrorMessage response = partyService.validatePartyRecord(partyRequestCmd.getParty());
        assertThat(response).isNotNull();
    }
    @Test
    void should_return_error_when_created_by_channel_validation_failed() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        partyRequestCmd = partyRequestCmd.builder().party(PartyCmd.builder().createdByChannel("Test").build()).build();
        ErrorMessage response = partyService.validatePartyRecord(partyRequestCmd.getParty());
        assertThat(response).isNotNull();
    }

    @Test
    void should_return_error_when_marital_status_validation_failed() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        partyRequestCmd = partyRequestCmd.builder().party(PartyCmd.builder().maritalStatus("Test").build()).build();
        ErrorMessage response = partyService.validatePartyRecord(partyRequestCmd.getParty());
        assertThat(response).isNotNull();
    }

    @Test
    void should_return_error_when_party_type_validation_failed() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        partyRequestCmd = partyRequestCmd.builder().party(PartyCmd.builder().partyType("Test").build()).build();
        ErrorMessage response = partyService.validatePartyRecord(partyRequestCmd.getParty());
        assertThat(response).isNotNull();
    }
    @Test
    void should_return_error_when_status_type_validation_failed() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        partyRequestCmd = partyRequestCmd.builder().party(PartyCmd.builder().status("Test").build()).build();
        ErrorMessage response = partyService.validatePartyRecord(partyRequestCmd.getParty());
        assertThat(response).isNotNull();
    }

    @Test
    void Should_return_party_record_when_mobile_number_validation_failed() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        String mobileNumber= partyRequestCmd.getParty().getPrimaryMobileNumber();
        Party party = new Party();
        party.setPartyId(1L);
        party.setPrimaryMobileNumber(mobileNumber);
        when(mockPartyRepository.fetchPartyDetailsByMobileNumber(mobileNumber)).thenReturn(singletonList(party));
        ErrorMessage response = partyService.validatePartyRecord(partyRequestCmd.getParty());
        assertThat(response).isNotNull();
        verify(mockPartyRepository, times(1)).fetchPartyDetailsByMobileNumber(mobileNumber);
    }
}