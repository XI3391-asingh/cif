package com.cif.syncerservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.util.Map;


public class SyncerServiceApplicationConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();
    @Valid
    @NotNull
    private ConsumerBuilderFactory consumerBuilderFactory = new ConsumerBuilderFactory();
    @Valid
    @NotNull
    private KafkaProducerFactory kafkaProducerFactory = new KafkaProducerFactory();
    @Valid
    @NotNull
    private int kafkaThreadPoolSize;
    private CircuitBreakerConfig circuitBreakerConfig = CircuitBreakerConfig
            .custom()
            .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
            .slidingWindowSize(10)
            .failureRateThreshold(70.0f)
            .slowCallRateThreshold(70.0f)
            .slowCallDurationThreshold(Duration.ofMillis(200))
            .build();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.database = dataSourceFactory;
    }

    @JsonProperty("consumer")
    public ConsumerBuilderFactory getConsumerBuilderFactory() {
        return consumerBuilderFactory;
    }

    @JsonProperty("consumer")
    public void setConsumerBuilderFactory(ConsumerBuilderFactory cifConsumerBuilderFactory) {
        this.consumerBuilderFactory = cifConsumerBuilderFactory;
    }

    @JsonProperty("producer")
    public KafkaProducerFactory getKafkaProducerFactory() {
        return kafkaProducerFactory;
    }

    @JsonProperty("producer")
    public void setKafkaProducerFactory(KafkaProducerFactory kafkaProducerFactory) {
        this.kafkaProducerFactory = kafkaProducerFactory;
    }

    @JsonProperty("quartz")
    private Map<String, String> quartz;

    public Map<String, String> getQuartzConfiguration() {
        return quartz;
    }

    public CircuitBreakerConfig getCircuitBreakerConfig() {
        return circuitBreakerConfig;
    }

    public int getKafkaThreadPoolSize() {
        return kafkaThreadPoolSize;
    }

    @JsonProperty("kafka")
    public void setKafkaProperties(Map<String, String> kafkaProperties) {
        this.kafkaThreadPoolSize = Integer.parseInt(kafkaProperties.get("threadPoolSize"));
    }

    public Map<String, String> getQuartz() {
        return quartz;
    }

    public void setQuartz(Map<String, String> quartz) {
        this.quartz = quartz;
    }
}
