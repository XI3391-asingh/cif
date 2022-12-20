package com.cif.syncerservice;

import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
class KafkaProducerFactoryTest {

    @Test
    void should_return_valid_consumer_if_properties_is_available() {
        KafkaProducerFactory kafkaProducerFactory = new KafkaProducerFactory();
        kafkaProducerFactory.setAcks(Optional.of("all"));
        kafkaProducerFactory.setBatchSize(1);
        kafkaProducerFactory.setBootstrapServers("localhost:9092");
        kafkaProducerFactory.setBufferMemory(1L);
        kafkaProducerFactory.setCompressionType("test");
        kafkaProducerFactory.setLinger(1);
        kafkaProducerFactory.setEnableIdempotence(true);
        kafkaProducerFactory.setKeySerializer("test");
        kafkaProducerFactory.setEnableIdempotence(false);
        kafkaProducerFactory.setMaxInFlightRequestsPerConnection(Optional.of(1));
        kafkaProducerFactory.setMaxPollBlockTime(Optional.empty());
        kafkaProducerFactory.setMaxPollBlockTime(Optional.empty());
        kafkaProducerFactory.setValueSerializer("org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducerFactory.setKeySerializer("org.apache.kafka.common.serialization.StringSerializer");
        kafkaProducerFactory.setName("test");
        kafkaProducerFactory.setRetries(Optional.of(1));
        kafkaProducerFactory.setTransactionalId(Optional.empty());
        kafkaProducerFactory.setType("test");

        KafkaProducer<String, Object> producer = kafkaProducerFactory.buildKafkaProducer();

        assertThat(kafkaProducerFactory.getAcks().get()).isNotNull();
    }

}