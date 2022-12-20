package com.cif.syncerservice.core.syncer.component;

import com.cif.syncerservice.core.syncer.services.SyncerService;
import com.cif.syncerservice.db.SyncerRepository;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.MockConsumer;
import org.apache.kafka.clients.consumer.OffsetResetStrategy;
import org.apache.kafka.common.TopicPartition;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(DropwizardExtensionsSupport.class)
class MultithreadedConsumerTest {

    private final ExecutorService executor = Executors.newFixedThreadPool(1);
    MockConsumer mockConsumer = new MockConsumer<>(OffsetResetStrategy.EARLIEST);
    SyncerService syncerService = mock(SyncerService.class);
    SyncerRepository syncerRepository = mock(SyncerRepository.class);
    MultithreadedConsumer multithreadedConsumer = new MultithreadedConsumer(singletonList("topic"), mockConsumer, syncerService, syncerRepository, executor);


    @Test
    void should_consumed_correctly_if_topics_is_subscribed() {
        mockConsumer.schedulePollTask(() -> {
            mockConsumer.rebalance(Collections.singletonList(new TopicPartition("topic", 0)));
            mockConsumer.addRecord(new ConsumerRecord<String, String>("topic",
                    0, 0L, "mykey", "myvalue0"));
        });
        mockConsumer.schedulePollTask(() -> multithreadedConsumer.stopConsuming());
        HashMap<TopicPartition, Long> startOffsets = new HashMap<>();
        TopicPartition tp = new TopicPartition("topic", 0);
        startOffsets.put(tp, 0L);
        mockConsumer.updateBeginningOffsets(startOffsets);

        multithreadedConsumer.run();

        assertThat(mockConsumer.closed()).isTrue();
    }

    @Test
    void should_handle_all_record_when_partition_revoked() {
        List<TopicPartition> topicPartitionList = Collections.singletonList(new TopicPartition("topic", 1));

        multithreadedConsumer.onPartitionsRevoked(topicPartitionList);

        assertThat(mockConsumer.closed()).isFalse();
    }


}