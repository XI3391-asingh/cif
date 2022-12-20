package com.cif.cifservice.core.party.event;

import com.cif.cifservice.api.PartyResponseCmd;
import com.cif.cifservice.api.UpdatePartyRequestCmd;
import com.cif.cifservice.core.party.services.PartyService;
import com.cif.cifservice.core.party.services.producer.ProducerService;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.db.elasticsearch.ElasticsearchClientRepository;
import com.cif.cifservice.resources.exceptions.ServerSideException;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public final class EventListener {

    private final Logger logger = getLogger(EventListener.class);

    private static final String CREATE_EVENT = "CREATE";
    private static final String UPDATE_EVENT = "UPDATE";

    private static final String DELETE_EVENT = "DELETE";

    private final ProducerService producerService;

    private final PartyService partyService;

    private final EventBus eventBus;
    private final ElasticsearchClientRepository clientRepository;

    private final CryptoUtil cryptoUtil;

    public EventListener(ProducerService producerService, PartyService partyService, EventBus eventBus, ElasticsearchClientRepository clientRepository, CryptoUtil cryptoUtil) {
        this.producerService = producerService;
        this.partyService = partyService;
        this.eventBus = eventBus;
        this.clientRepository = clientRepository;
        this.cryptoUtil = cryptoUtil;
    }

    @Subscribe
    public void onCreateEvent(CreateEvent createEvent) {
        logger.info("{} event publishing to Kafka pipeline for party id {} ", CREATE_EVENT, createEvent.partyId());
        onPublish(new TopicData(new HashSet<>(), createEvent.partyResponseCmd()), CREATE_EVENT, createEvent.partyId());
        eventBus.unregister(createEvent);
    }

    @Subscribe
    public void onGenericUpdateEvent(GenericUpdateEvent genericUpdateEvent) {
        logger.info("Generic {} event publishing to Kafka pipeline for party id {} ", UPDATE_EVENT, genericUpdateEvent.partyId());
        onPublish(new TopicData(getGenericApiMetaFields(genericUpdateEvent.updatePartyRequestCmd()), genericUpdateEvent.updatedDbPartyRecord()), UPDATE_EVENT, genericUpdateEvent.partyId());
        eventBus.unregister(genericUpdateEvent);

    }

    @Subscribe
    public void onUpdateEvent(UpdateEvent updateEvent) {
        var partyId = updateEvent.partyId();
        logger.info(" {} event publishing to Kafka pipeline for party id {} ", UPDATE_EVENT, partyId);
        var parties = partyService.fetchPartyDetails(partyId);
        onPublish(new TopicData(getMetaFields(updateEvent.metaFields()), parties), UPDATE_EVENT, partyId);
        eventBus.unregister(updateEvent);
    }

    @Subscribe
    public void onDeleteEvent(DeleteEvent deleteEvent) {
        var partyId = deleteEvent.partyId();
        logger.info(" {} event publishing to Kafka pipeline for party id {} ", DELETE_EVENT, partyId);
        var parties = partyService.fetchPartyDetails(partyId);

        clientRepository.createAndUpdateDocument(new TopicData(new HashSet<Object>(), parties).payload(), partyId);
        onPublish(new TopicData(new HashSet<Object>(), parties), DELETE_EVENT, partyId);
        eventBus.unregister(deleteEvent);
    }

    public void onPublish(TopicData topicData, String eventType, Long partyId) {
        clientRepository.createAndUpdateDocument(topicData.payload(), partyId);
        if (topicData.payload() instanceof PartyResponseCmd) {
            logger.info("Topic data processing to kafka pipeline for party id {} and event type {} ", partyId, eventType);
            if (ObjectUtils.isNotEmpty(producerService)) {
                PartyResponseCmd partyResponseCmd = cryptoUtil.decryptObjectUsingConfiguredField((PartyResponseCmd) topicData.payload());
                Runnable producer = () -> {
                    producerService.publishEvent(new TopicData(topicData.metaFields(), partyResponseCmd), eventType);
                };
                Thread thread = new Thread(producer);
                thread.start();
            }
        }
    }

    public Object getMetaFields(Object changedData) {
        return getFields(changedData, new HashSet<>());
    }

    public Object getGenericApiMetaFields(UpdatePartyRequestCmd updatePartyRequestCmd) {
        var metaFields = new HashSet<>();
        if (updatePartyRequestCmd.getParty() != null) {
            getFields(updatePartyRequestCmd.getParty(), metaFields);
        }
        if (updatePartyRequestCmd.getOccupationDetail() != null) {
            getFields(updatePartyRequestCmd.getOccupationDetail(), metaFields);
        }
        if (updatePartyRequestCmd.getPartyFlag() != null) {
            getFields(updatePartyRequestCmd.getPartyFlag(), metaFields);
        }

        if (!updatePartyRequestCmd.getAddress().isEmpty()) {
            updatePartyRequestCmd.getAddress().stream().forEach(address -> getFields(address, metaFields));
        }
        if (!updatePartyRequestCmd.getContactDetails().isEmpty()) {
            updatePartyRequestCmd.getContactDetails().stream().forEach(contact -> getFields(contact, metaFields));
        }
        if (!updatePartyRequestCmd.getDocuments().isEmpty()) {
            updatePartyRequestCmd.getDocuments().stream().forEach(document -> getFields(document, metaFields));
        }
        if (!updatePartyRequestCmd.getAssets().isEmpty()) {
            updatePartyRequestCmd.getAssets().stream().forEach(asset -> getFields(asset, metaFields));
        }
        if (!updatePartyRequestCmd.getMemos().isEmpty()) {
            updatePartyRequestCmd.getMemos().stream().forEach(memos -> getFields(memos, metaFields));
        }

        if (!updatePartyRequestCmd.getRisks().isEmpty()) {
            updatePartyRequestCmd.getRisks().stream().forEach(risks -> getFields(risks, metaFields));
        }

        if (!updatePartyRequestCmd.getFatcaDetails().isEmpty()) {
            updatePartyRequestCmd.getFatcaDetails().stream().forEach(fatcaDetails -> getFields(fatcaDetails, metaFields));
        }

        if (!updatePartyRequestCmd.getRelations().isEmpty()) {
            updatePartyRequestCmd.getRelations().stream().forEach(relations -> getFields(relations, metaFields));
        }

        if (!updatePartyRequestCmd.getGuardians().isEmpty()) {
            updatePartyRequestCmd.getGuardians().stream().forEach(guardians -> getFields(guardians, metaFields));
        }

        return metaFields;
    }

    public Set<Object> getFields(Object object, Set<Object> metaFields) {
        for (Field field : object.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                Object fieldValue = field.get(object);
                if (fieldValue != null) {
                    metaFields.add(field.getName());
                }
            } catch (IllegalAccessException e) {
                throw new ServerSideException("ERROR", e.getMessage(), e);
            }
        }
        return metaFields;
    }
}
