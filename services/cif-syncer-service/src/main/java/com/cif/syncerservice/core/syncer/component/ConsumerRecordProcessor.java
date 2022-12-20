package com.cif.syncerservice.core.syncer.component;

import com.cif.syncerservice.core.syncer.domain.AdapterConfig;
import com.cif.syncerservice.core.syncer.domain.ChangeConfig;
import com.cif.syncerservice.core.syncer.services.SyncerService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;


public class ConsumerRecordProcessor implements Runnable {

    private final List<ConsumerRecord<String, Object>> records;
    private final CompletableFuture<Long> completion = new CompletableFuture<>();
    private final ReentrantLock startStopLock = new ReentrantLock();
    private final AtomicLong currentOffset = new AtomicLong();
    private volatile boolean stopped = false;
    private volatile boolean started = false;
    private volatile boolean finished = false;
    private List<AdapterConfig> adapterConfigList;

    private List<ChangeConfig> changeConfigs;

    private SyncerService syncerService;
    private Logger log = LoggerFactory.getLogger(ConsumerRecordProcessor.class);

    public ConsumerRecordProcessor(List<ConsumerRecord<String, Object>> records, List<AdapterConfig> adapterConfigList, SyncerService syncerService, List<ChangeConfig> changeConfigs) {
        this.records = records;
        this.adapterConfigList = adapterConfigList;
        this.syncerService = syncerService;
        this.changeConfigs = changeConfigs;
    }

    public void run() {
        startStopLock.lock();
        if (stopped) {
            return;
        }
        started = true;
        startStopLock.unlock();

        for (ConsumerRecord<String, Object> record : records) {
            if (stopped)
                break;
            try {
                syncerService.processRecord(record, adapterConfigList, changeConfigs);
            } catch (Exception e) {
                log.error("Error occurred while processing {} record . Error Message {}", record.key(), e.getMessage());
            }
            currentOffset.set(record.offset() + 1);
        }
        finished = true;
        completion.complete(currentOffset.get());
    }

    public long getCurrentOffset() {
        return currentOffset.get();
    }

    public void stop() {
        startStopLock.lock();
        this.stopped = true;
        if (!started) {
            finished = true;
            completion.complete(currentOffset.get());
        }
        startStopLock.unlock();
    }

    public long waitForCompletion() {
        try {
            return completion.get();
        } catch (InterruptedException | ExecutionException e) {
            return -1;
        }
    }

    public boolean isFinished() {
        return finished;
    }

}