package com.cif.cifservice.db;

import com.cif.cifservice.api.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.COLLECTION;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PartyTransactionRepositoryTests extends JdbiTest {

    private final ObjectMapper MAPPER = new ObjectMapper();
    private PartyTransactionRepository partyTransactionRepository;

    private PartyRequestCmd partyRequestCmd;


    @BeforeEach
    void beforeTest() throws IOException {
        partyTransactionRepository = jdbi.onDemand(PartyTransactionRepository.class);
        partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        var partyId = partyTransactionRepository.createAllPartySection(partyRequestCmd);
    }

    @Test
    void should_return_response_when_party_is_saved() {
        var partyId = partyTransactionRepository.createAllPartySection(partyRequestCmd);
        assertThat(partyId).isGreaterThan(0);
    }


    @Test
    void should_return_true_when_party_is_updated() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        partyRequestCmd.getParty().setPartyIdentifier("12345678912");
        partyTransactionRepository.createAllPartySection(partyRequestCmd);
        var result = partyTransactionRepository.updateAllPartySection(updatePartyRequestCmd);
        assertTrue(result);
    }

    @Test
    void should_return_false_when_party_is_not_updated() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        UpdatePartyRequestCmd.builder().party(updatePartyRequestCmd.getParty().partyIdentifier("1234567890")).occupationDetail(Optional.ofNullable(updatePartyRequestCmd.getOccupationDetail()).orElse(new UpdateOccupationDetailCmd())).build();
        var result = partyTransactionRepository.updateAllPartySection(updatePartyRequestCmd);
        assertFalse(result);
    }

    @Test
    void should_return_true_when_contact_is_saved() throws IOException {
        CreateContactDetailsCmd createContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);
        var result = partyTransactionRepository.saveContact(Collections.singleton(createContactDetailsCmd),1L);
        assertTrue(result);

    }
    @Test
    void should_return_true_when_contact_is_updated() throws IOException {
        UpdateContactDetailsCmd contactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);
        var result = partyTransactionRepository.updateContactDetails(1,contactDetailsCmd);
        assertTrue(result);
    }

    @Test
    void should_return_true_when_primary_email_is_updated() throws  IOException {
       var result = partyTransactionRepository.updatePartyPrimaryMobileNumberAndPrimaryEmail(true,1L,"02", "test@gmail.com");
       assertTrue(result);
    }




}
