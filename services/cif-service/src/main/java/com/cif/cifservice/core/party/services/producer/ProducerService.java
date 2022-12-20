package com.cif.cifservice.core.party.services.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.slf4j.Logger;

import java.util.UUID;

import static org.slf4j.LoggerFactory.getLogger;

public class ProducerService {

    private final Logger log = getLogger(ProducerService.class);
    private final KafkaProducer<String, Object> producer;

    private final String topic;


    public ProducerService(KafkaProducer<String, Object> kafkaProducer, String topic) {
        this.producer = kafkaProducer;
        this.topic = topic;
    }

    public void publishEvent(Object payload, String eventName) {
        ProducerRecord<String, Object> producerRecord = new ProducerRecord<>(topic, eventName, payload);
        producerRecord.headers().add(new RecordHeader("transactionId", UUID.randomUUID().toString().getBytes()));
        producer.send(producerRecord, (result, exception) -> {
            if (exception == null) {
                log.info("Topic data successfully published to Kafka topic {}, partition {} with offset {}", result.topic(), result.partition(), result.offset());
            } else {
                log.error("Unable to publish topic {}", exception);
            }
        });
    }
}
