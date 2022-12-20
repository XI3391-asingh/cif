package com.cif.syncerservice.core.syncer.component;

import com.cif.syncerservice.core.syncer.dto.PartyRequest;
import com.cif.syncerservice.core.syncer.services.SyncerService;
import com.cif.syncerservice.db.SyncerRepository;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class ConsumerManager<K, V extends PartyRequest> {
    private int parallelismCount = 1;
    private Collection<String> topics;

    private KafkaConsumer<String, Object> kafkaConsumer;
    private ExecutorService executor;
    private MultithreadedConsumer consumer;

    private SyncerService syncerService;

    private SyncerRepository syncerRepository;

    public ConsumerManager(ExecutorService executorService, Collection<String> topics, KafkaConsumer<String, Object> kafkaConsumer, SyncerService syncerService, SyncerRepository syncerRepository) {
        this.executor = executorService;
        this.topics = topics;
        this.kafkaConsumer = kafkaConsumer;
        this.syncerService = syncerService;
        this.syncerRepository = syncerRepository;
    }

    public int getParallelismCount() {
        return parallelismCount;
    }

    public void setParallelismCount(int parallelismCount) {
        this.parallelismCount = parallelismCount;
    }

    public Collection<String> getTopics() {
        return topics;
    }

    public void setTopics(Collection<String> topics) {
        this.topics = topics;
    }

    public void startConsumers() {
        consumer = new MultithreadedConsumer(topics, kafkaConsumer, syncerService, syncerRepository, executor);
        executor.submit(consumer);
    }

    public void stopConsumers() {
        consumer.stopConsuming();
        shutdownExecutor();
    }

    private void shutdownExecutor() {
        try {
            executor.shutdown();
            executor.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            System.err.println("Executor tasks interrupted");
        } finally {
            if (!executor.isTerminated()) {
                System.err.println("Canceling non-finished executor tasks");
            }
            executor.shutdownNow();
        }
    }
}
