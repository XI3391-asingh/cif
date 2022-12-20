package com.cif.cifservice.core.party.services.event;

import com.cif.cifservice.api.PartyResponseCmd;
import com.cif.cifservice.api.UpdatePartyRequestCmd;
import com.cif.cifservice.api.ViewPartyCmd;
import com.cif.cifservice.core.party.domain.PartyDetailsView;
import com.cif.cifservice.core.party.event.*;
import com.cif.cifservice.core.party.services.PartyService;
import com.cif.cifservice.core.party.services.producer.ProducerService;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.db.PartyRepository;
import com.cif.cifservice.db.elasticsearch.ElasticsearchClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;
import org.apache.commons.codec.digest.Crypt;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class EventListnerTest {

    private final ElasticsearchClientRepository mockClientRepository = mock(ElasticsearchClientRepository.class);
    private final EventBus mockEventBus = mock(EventBus.class);
    private final ProducerService mockProducerService = mock(ProducerService.class);
    private final PartyService mockPartyService = mock(PartyService.class);

    private final PartyRepository partyRepository = mock(PartyRepository.class);
    private final ObjectMapper MAPPER = new ObjectMapper();
    private final CryptoUtil cryptoUtil= mock(CryptoUtil.class);
    private EventListener eventListener = new EventListener(mockProducerService, mockPartyService, mockEventBus, mockClientRepository,cryptoUtil);

    private final PartyResponseCmd partyResponseCmd = PartyResponseCmd.builder().party(ViewPartyCmd.builder().partyIdentifier("1234567890").firstName("Ramesh").build()).build();

    private final Long partyId = 1l;

    private final Object obj = new Object();

    @Test
   void should_return_true_when_verify_on_create_event(){
        CreateEvent createEvent = new CreateEvent(partyResponseCmd,partyId);

        doNothing().when(mockClientRepository).createAndUpdateDocument(obj, partyId);
        doNothing().when(mockEventBus).unregister(createEvent);
        eventListener.onCreateEvent(createEvent);

        verify(mockEventBus, times(1)).unregister(createEvent);
    }

    @Test
    void should_return_true_when_verify_on_generic_update_event() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        GenericUpdateEvent genericUpdateEvent = new GenericUpdateEvent(partyResponseCmd, partyId, updatePartyRequestCmd);

        doNothing().when(mockClientRepository).createAndUpdateDocument(obj, partyId);
        doNothing().when(mockEventBus).unregister(genericUpdateEvent);
        eventListener.onGenericUpdateEvent(genericUpdateEvent);

        verify(mockEventBus, times(1)).unregister(genericUpdateEvent);

    }

    @Test
    void should_return_true_when_verify_on_update_event() {
        PartyDetailsView partyDetailsView = new PartyDetailsView();
        partyDetailsView.setPartyId(partyId);
        List<PartyDetailsView> partyDetailList = Collections.singletonList(partyDetailsView);
        UpdateEvent updateEvent = new UpdateEvent(partyId, obj);

        when(partyRepository.findPartyDetail(partyId)).thenReturn(partyDetailList);
        doNothing().when(mockEventBus).unregister(updateEvent);
        eventListener.onUpdateEvent(updateEvent);

        verify(mockEventBus, times(1)).unregister(updateEvent);

    }

    @Test
    void should_return_true_when_verify_on_delete_event() {
        PartyDetailsView partyDetailsView = new PartyDetailsView();
        partyDetailsView.setPartyId(partyId);
        List<PartyDetailsView> partyDetailList = Collections.singletonList(partyDetailsView);
        DeleteEvent deleteEvent = new DeleteEvent(partyId);

        when(partyRepository.findPartyDetail(partyId)).thenReturn(partyDetailList);
        doNothing().when(mockEventBus).unregister(deleteEvent);
        eventListener.onDeleteEvent(deleteEvent);

        verify(mockEventBus, times(1)).unregister(deleteEvent);

    }
}
