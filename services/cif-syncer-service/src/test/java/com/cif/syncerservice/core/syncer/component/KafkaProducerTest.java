package com.cif.syncerservice.core.syncer.component;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.apache.kafka.clients.producer.MockProducer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(DropwizardExtensionsSupport.class)
class KafkaProducerTest {

    MockProducer mockProducer = new MockProducer<>(true, new StringSerializer(), new StringSerializer());

    private KafkaProducer kafkaProducer = new KafkaProducer(singleton("topic"), mockProducer);

    @Test
    void should_send_message_when_no_exception() {
        kafkaProducer.sendMessageToTopic("soccer", "{\"site\" : \"baeldung\"}");

        assertTrue(mockProducer.history().size() == 1);
    }
}