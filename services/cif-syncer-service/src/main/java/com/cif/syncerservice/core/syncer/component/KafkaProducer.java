package com.cif.syncerservice.core.syncer.component;

import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;


public class KafkaProducer {
    private final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final Producer producer;
    private final Set<String> topics;

    public KafkaProducer(Set<String> topics, Producer kafkaProducer) {
        this.topics = topics;
        this.producer = kafkaProducer;
    }

    public <T> void sendMessageToTopic(String keyName, T requestDTO) {
        log.info("Sending Kafka Message to topic . [key:{}],[topic:{}]", keyName, topics);
        try {
            topics.forEach(topic -> {
                ProducerRecord<String, T> producerRecord = new ProducerRecord<>(topic, keyName, requestDTO);
                producer.send(producerRecord, (metadata, e) -> {
                    if (e != null) {
                        log.info("Kafka Produced Message Un-Successful to [topic:{}], [key:{}] and [value:{}]", topic, keyName, requestDTO);
                    } else {
                        log.info("Kafka Produced Message Successfully to [topic:{}], [key:{}] and [value:{}]", topic, keyName, requestDTO);
                    }
                });
            });
        } catch (Exception e) {
            log.error("Exception: Error sending data to topic {}",
                    e.getMessage());
        } finally {
            producer.flush();
        }

    }
}
