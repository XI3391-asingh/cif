package com.cif.cifservice.core.party.services.producer;

import com.cif.cifservice.core.party.services.producer.security.SecurityFactory;
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


public class KafkaProducerFactory<K, V> implements Discoverable {

    @JsonProperty
    protected String type;

    @JsonProperty
    @NotNull
    protected String topic;

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
    protected Optional<String> acks = Optional.empty();

    @JsonProperty
    protected Optional<String> retries = Optional.empty();

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
    protected Optional<String> transactionalId = Optional.empty();

    @Valid
    @JsonProperty
    protected Optional<SecurityFactory> security = Optional.empty();

    public KafkaProducer<K, V> buildKafkaProducer() {
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
        security.filter(securityFactory -> securityFactory.isEnabled())
                .ifPresent(securityFactory -> configs.putAll(securityFactory.build()));
        return new KafkaProducer<>(configs);
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public Optional<String> getAcks() {
        return acks;
    }

    public void setAcks(Optional<String> acks) {
        this.acks = acks;
    }

    public Optional<String> getRetries() {
        return retries;
    }

    public void setRetries(Optional<String> retries) {
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

    public Optional<String> getTransactionalId() {
        return transactionalId;
    }

    public void setTransactionalId(Optional<String> transactionalId) {
        this.transactionalId = transactionalId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }
}