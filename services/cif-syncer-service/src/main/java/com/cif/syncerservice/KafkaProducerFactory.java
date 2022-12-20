package com.cif.syncerservice;

import com.cif.syncerservice.core.syncer.security.SecurityFactory;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.jackson.Discoverable;
import io.dropwizard.util.Duration;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.record.CompressionType;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class KafkaProducerFactory implements Discoverable {

    @JsonProperty
    protected String type;

    @JsonProperty
    protected Set<String> topics;

    @JsonProperty
    protected String name;
    @JsonProperty
    protected String bootstrapServers;

    @Valid
    @NotNull
    @JsonProperty
    protected String keySerializer;

    @Valid
    @NotNull
    @JsonProperty
    protected String valueSerializer;

    @JsonProperty
    protected Optional<Object> acks = Optional.empty();

    @JsonProperty
    protected Optional<Object> retries = Optional.empty();

    @JsonProperty
    protected Optional<Integer> maxInFlightRequestsPerConnection = Optional.empty();

    @JsonProperty
    protected Optional<Duration> maxPollBlockTime = Optional.empty();

    @NotEmpty
    @JsonProperty
    protected String compressionType = CompressionType.GZIP.name;

    @Min(-1)
    @JsonProperty
    protected int sendBufferBytes = -1;

    @Min(-1)
    @JsonProperty
    protected int receiveBufferBytes = -1;

    @Min(0L)
    @JsonProperty
    protected long bufferMemory = 32 * 1024 * 1024L;

    @Min(0)
    @JsonProperty
    protected int batchSize = 16384;

    @Min(0)
    @JsonProperty
    protected int linger = 0;

    @Min(0)
    @JsonProperty
    protected int requestTimeout = 30;

    @JsonProperty
    protected boolean enableIdempotence = false;

    @JsonProperty
    protected Optional<Object> transactionalId = Optional.empty();

    @Valid
    @JsonProperty
    protected Optional<SecurityFactory> security = Optional.empty();

    protected KafkaProducer<String, Object> buildKafkaProducer() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configs.put(ProducerConfig.ACKS_CONFIG, acks.get());
        configs.put(ProducerConfig.RETRIES_CONFIG, retries.get());
        configs.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        configs.put(ProducerConfig.LINGER_MS_CONFIG, linger);
        configs.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        configs.put(ProducerConfig.MAX_IN_FLIGHT_REQUESTS_PER_CONNECTION, maxInFlightRequestsPerConnection.get());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, keySerializer);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, valueSerializer);
        security.filter(SecurityFactory::isEnabled)
                .ifPresent(securityFactory -> configs.putAll(securityFactory.build()));

        return new KafkaProducer<>(configs);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getTopics() {
        return topics;
    }

    public void setTopics(Set<String> topics) {
        this.topics = topics;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBootstrapServers() {
        return bootstrapServers;
    }

    public void setBootstrapServers(String bootstrapServers) {
        this.bootstrapServers = bootstrapServers;
    }

    public String getKeySerializer() {
        return keySerializer;
    }

    public void setKeySerializer(String keySerializer) {
        this.keySerializer = keySerializer;
    }

    public String getValueSerializer() {
        return valueSerializer;
    }

    public void setValueSerializer(String valueSerializer) {
        this.valueSerializer = valueSerializer;
    }

    public Optional<Object> getAcks() {
        return acks;
    }

    public void setAcks(Optional<Object> acks) {
        this.acks = acks;
    }

    public Optional<Object> getRetries() {
        return retries;
    }

    public void setRetries(Optional<Object> retries) {
        this.retries = retries;
    }

    public Optional<Integer> getMaxInFlightRequestsPerConnection() {
        return maxInFlightRequestsPerConnection;
    }

    public void setMaxInFlightRequestsPerConnection(Optional<Integer> maxInFlightRequestsPerConnection) {
        this.maxInFlightRequestsPerConnection = maxInFlightRequestsPerConnection;
    }

    public Optional<Duration> getMaxPollBlockTime() {
        return maxPollBlockTime;
    }

    public void setMaxPollBlockTime(Optional<Duration> maxPollBlockTime) {
        this.maxPollBlockTime = maxPollBlockTime;
    }

    public String getCompressionType() {
        return compressionType;
    }

    public void setCompressionType(String compressionType) {
        this.compressionType = compressionType;
    }

    public int getSendBufferBytes() {
        return sendBufferBytes;
    }

    public void setSendBufferBytes(int sendBufferBytes) {
        this.sendBufferBytes = sendBufferBytes;
    }

    public int getReceiveBufferBytes() {
        return receiveBufferBytes;
    }

    public void setReceiveBufferBytes(int receiveBufferBytes) {
        this.receiveBufferBytes = receiveBufferBytes;
    }

    public long getBufferMemory() {
        return bufferMemory;
    }

    public void setBufferMemory(long bufferMemory) {
        this.bufferMemory = bufferMemory;
    }

    public int getBatchSize() {
        return batchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    public int getLinger() {
        return linger;
    }

    public void setLinger(int linger) {
        this.linger = linger;
    }

    public int getRequestTimeout() {
        return requestTimeout;
    }

    public void setRequestTimeout(int requestTimeout) {
        this.requestTimeout = requestTimeout;
    }

    public boolean isEnableIdempotence() {
        return enableIdempotence;
    }

    public void setEnableIdempotence(boolean enableIdempotence) {
        this.enableIdempotence = enableIdempotence;
    }

    public Optional<Object> getTransactionalId() {
        return transactionalId;
    }

    public void setTransactionalId(Optional<Object> transactionalId) {
        this.transactionalId = transactionalId;
    }
}
