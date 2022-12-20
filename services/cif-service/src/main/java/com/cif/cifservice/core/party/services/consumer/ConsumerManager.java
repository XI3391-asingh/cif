package com.cif.cifservice.core.party.services.consumer;

import com.cif.cifservice.core.party.services.PartyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.*;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.errors.WakeupException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

public class ConsumerManager implements ConsumerRebalanceListener, Runnable {

    private final Consumer<String, String> consumer;
    private final ExecutorService executor = Executors.newFixedThreadPool(6);
    private final Map<TopicPartition, Task> activeTasks = new HashMap<>();
    private final Map<TopicPartition, OffsetAndMetadata> offsetsToCommit = new HashMap<>();
    private final AtomicBoolean stopped = new AtomicBoolean(false);
    private final Logger log = LoggerFactory.getLogger(ConsumerManager.class);
    private final PartyService partyService;
    private long lastCommitTime = System.currentTimeMillis();
    private Set<String> topic;
    private ObjectMapper objectMapper;


    public ConsumerManager(Set<String> topic, PartyService partyService, Consumer<String, String> consumer, ObjectMapper objectMapper) {
        this.topic = topic;
        this.partyService = partyService;
        this.consumer = consumer;
        this.objectMapper = objectMapper;
    }

    public void run() {
        try {
            consumer.subscribe(topic, this);
            while (!stopped.get()) {
                ConsumerRecords<String, String> records = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
                handleFetchedRecords(records);
                checkActiveTasks();
                commitOffsets();
            }
        } catch (WakeupException we) {
            if (!stopped.get()) {
                log.error("Error Occurred while performing kafka consume poll operation for [topic: {}]", topic, we);
                throw new KafkaException("Error Occurred while performing kafka consume poll operation", we);
            }
        } finally {
            consumer.close();
        }
    }


    private void handleFetchedRecords(ConsumerRecords<String, String> consumerRecords) {
        if (consumerRecords.count() > 0) {
            List<TopicPartition> partitionsToPause = new ArrayList<>();
            consumerRecords.partitions().forEach(partition -> {
                List<ConsumerRecord<String, String>> recs = consumerRecords.records(partition);
                Task task = new Task(recs, partyService, objectMapper);
                partitionsToPause.add(partition);
                executor.submit(task);
                activeTasks.put(partition, task);
            });
            consumer.pause(partitionsToPause);
        }
    }

    private void commitOffsets() {
        try {
            long currentTimeMillis = System.currentTimeMillis();
            if (currentTimeMillis - lastCommitTime > 5000) {
                if (!offsetsToCommit.isEmpty()) {
                    consumer.commitSync(offsetsToCommit);
                    offsetsToCommit.clear();
                }
                lastCommitTime = currentTimeMillis;
            }
        } catch (Exception e) {
            throw new KafkaException("Error Occurred while committing offset", e);
        }
    }


    private void checkActiveTasks() {
        List<TopicPartition> finishedTasksPartitions = new ArrayList<>();
        activeTasks.forEach((partition, task) -> {
            if (task.isFinished()) finishedTasksPartitions.add(partition);
            long offset = task.getCurrentOffset();
            if (offset > 0) offsetsToCommit.put(partition, new OffsetAndMetadata(offset));
        });
        finishedTasksPartitions.forEach(activeTasks::remove);
        consumer.resume(finishedTasksPartitions);
    }


    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {

        // 1. Stop all tasks handling records from revoked partitions
        Map<TopicPartition, Task> stoppedTask = new HashMap<>();
        for (TopicPartition partition : partitions) {
            Task task = activeTasks.remove(partition);
            if (task != null) {
                task.stop();
                stoppedTask.put(partition, task);
            }
        }

        // 2. Wait for stopped tasks to complete processing of current record
        stoppedTask.forEach((partition, task) -> {
            long offset = (long) task.waitForCompletion();
            if (offset > 0) offsetsToCommit.put(partition, new OffsetAndMetadata(offset));
        });


        // 3. collect offsets for revoked partitions
        Map<TopicPartition, OffsetAndMetadata> revokedPartitionOffsets = new HashMap<>();
        partitions.forEach(partition -> {
            OffsetAndMetadata offset = offsetsToCommit.remove(partition);
            if (offset != null) revokedPartitionOffsets.put(partition, offset);
        });

        // 4. commit offsets for revoked partitions
        try {
            consumer.commitSync(revokedPartitionOffsets);
        } catch (Exception e) {
            throw new KafkaException("Failed to commit offsets for revoked partitions!", e);
        }
    }

    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        consumer.resume(partitions);
    }


    public void stopConsuming() {
        stopped.set(true);
        consumer.wakeup();
    }

}
