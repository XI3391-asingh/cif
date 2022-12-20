package com.cif.cifservice.core.party.services.producer;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class ProducerServiceTest {

    private final KafkaProducer<String, Object> mockProducer = mock(KafkaProducer.class);
    private final ProducerRecord<String, Object> mockProducerRecord = mock(ProducerRecord.class);
    private final ProducerService producerService = new ProducerService(mockProducer, "party");

@Test
    void should_return_true_when_verify_publish_Event(){
    Object obj = new Object();
    when(mockProducer.send(mockProducerRecord, (result, exception) -> {
        if (exception == null) {
            result.topic();
        }
    })).thenReturn(null);

    producerService.publishEvent(obj, "Create");
}
}
