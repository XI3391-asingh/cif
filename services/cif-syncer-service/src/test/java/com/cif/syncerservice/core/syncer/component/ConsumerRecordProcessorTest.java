package com.cif.syncerservice.core.syncer.component;

import com.cif.syncerservice.core.syncer.domain.AdapterConfig;
import com.cif.syncerservice.core.syncer.domain.ChangeConfig;
import com.cif.syncerservice.core.syncer.services.SyncerService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class ConsumerRecordProcessorTest {

    private ConsumerRecord<String, Object> consumerRecord = new ConsumerRecord<>("TEST", 1, 1, "CREATE", "kafkaValue");
    private ObjectMapper objectMapper = new ObjectMapper();
    private ChangeConfig changeConfig = mock(ChangeConfig.class);
    private AdapterConfig adapterConfig = mock(AdapterConfig.class);
    private SyncerService syncerService = mock(SyncerService.class);

    private ConsumerRecordProcessor processor = new ConsumerRecordProcessor(singletonList(consumerRecord), singletonList(adapterConfig), syncerService, singletonList(changeConfig));

    @BeforeEach
    void setup() {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Test
    void should_complete_successfully_when_no_exception_occurred() {
        MockitoAnnotations.openMocks(this);
        doNothing().when(syncerService).processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));

        processor.run();

        assertTrue(processor.isFinished());
        verify(syncerService, times(1)).processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));

    }

    @Test
    void should_throw_error_when_exception_occurred() {
        MockitoAnnotations.openMocks(this);
        doThrow(RuntimeException.class).when(syncerService).processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));

        processor.run();

        assertThat(processor.getCurrentOffset()).isEqualTo(2L);
        verify(syncerService, times(1)).processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));

    }

    @Test
    void should_return_without_processing_if_processor_is_stopped() {
        MockitoAnnotations.openMocks(this);
        doThrow(RuntimeException.class).when(syncerService).processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));

        processor.stop();
        processor.run();

        assertNotEquals(3L, processor.getCurrentOffset());
        verify(syncerService, times(0)).processRecord(consumerRecord, singletonList(adapterConfig), singletonList(changeConfig));

    }


}