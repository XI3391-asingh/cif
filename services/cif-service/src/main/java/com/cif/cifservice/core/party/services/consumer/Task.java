package com.cif.cifservice.core.party.services.consumer;

import com.cif.cifservice.core.party.domain.ApiResponse;
import com.cif.cifservice.core.party.services.PartyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;

import static org.slf4j.LoggerFactory.getLogger;

public class Task implements Runnable {

    private Logger log = getLogger(Task.class);

    private final List<ConsumerRecord<String, String>> consumerRecords;
    private final CompletableFuture<Long> completion = new CompletableFuture<>();
    private final ReentrantLock startStopLock = new ReentrantLock();
    private final AtomicLong currentOffset = new AtomicLong(-1);
    private final PartyService partyService;
    private volatile boolean stopped = false;
    private volatile boolean started = false;
    private volatile boolean finished = false;
    private ObjectMapper objectMapper;

    public Task(List<ConsumerRecord<String, String>> consumerRecords, PartyService partyService, ObjectMapper objectMapper) {
        this.consumerRecords = consumerRecords;
        this.partyService = partyService;
        this.objectMapper = objectMapper;
    }

    public void run() {
        startStopLock.lock();
        if (stopped) {
            return;
        }
        started = true;
        startStopLock.unlock();
        for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
            if (stopped)
                break;
            try {
                ApiResponse apiResponse = objectMapper.readValue(consumerRecord.value(), ApiResponse.class);
                String key = consumerRecord.key();
                log.info("Received kafka record for [key {}] ", key);
                partyService.mapXrefIdWithPartyId(apiResponse);
            } catch (Exception e) {
                log.error("Error Occurred while processing consumerRecord. [consumerRecord: {}],[errorMessage: {}", consumerRecord, e.getMessage());
            }
            currentOffset.set(consumerRecord.offset() + 1);
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
            completion.complete(-1L);
        }
        startStopLock.unlock();
    }

    public Object waitForCompletion() {
        try {
            return completion.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            return -1;
        }
    }

    public boolean isFinished() {
        return finished;
    }
}