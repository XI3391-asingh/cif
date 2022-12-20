package com.cif.cifservice;

import com.cif.cifservice.core.party.services.consumer.ConsumerBuildFactory;
import com.cif.cifservice.core.party.services.producer.KafkaProducerFactory;
import com.cif.cifservice.core.party.util.BulkImportConfig;
import com.cif.cifservice.core.party.util.ElasticSearchConfig;
import com.cif.cifservice.core.party.util.EncryptionConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

public class PartyServiceConfiguration extends Configuration {

    @Valid
    @NotNull
    private DataSourceFactory database = new DataSourceFactory();

    @Valid
    @NotNull
    private ElasticSearchConfig elasticSearchConfig = new ElasticSearchConfig();

    @Valid
    @NotNull
    private KafkaProducerFactory<String, Object> kafkaProducerFactory = new KafkaProducerFactory<>();

    @Valid
    @NotNull
    private ConsumerBuildFactory kafkaConsumerFactory = new ConsumerBuildFactory();

    @NotNull
    private SwaggerBundleConfiguration swaggerConfiguration;


    @JsonProperty("universalSearchFields")
    private Map<String, List<String>> universalSearchFieldDetails;

    @Valid
    @NotNull
    private int kafkaThreadPoolSize;

    @Valid
    @NotNull
    @JsonProperty("eventPosting")
    private Map<String, String> eventPosting;

    @Valid
    @NotNull
    private EncryptionConfig encryption = new EncryptionConfig();

    @Valid
    @NotNull
    private BulkImportConfig bulkImportConfig = new BulkImportConfig();

    @JsonProperty("database")
    public DataSourceFactory getDataSourceFactory() {
        return database;
    }

    @JsonProperty("database")
    public void setDatasourceFactory(DataSourceFactory database) {
        this.database = database;
    }

    @JsonProperty("producer")
    public KafkaProducerFactory<String, Object> getKafkaProducerFactory() {
        return kafkaProducerFactory;
    }


    @JsonProperty("producer")
    public void setKafkaProducerFactory(KafkaProducerFactory<String, Object> kafkaProducerFactory) {
        this.kafkaProducerFactory = kafkaProducerFactory;
    }

    @JsonProperty("consumer")
    public ConsumerBuildFactory getKafkaConsumerFactory() {
        return kafkaConsumerFactory;
    }

    @JsonProperty("consumer")
    public void setKafkaConsumerFactory(ConsumerBuildFactory kafkaConsumerFactory) {
        this.kafkaConsumerFactory = kafkaConsumerFactory;
    }

    @JsonProperty("elasticsearch")
    public ElasticSearchConfig getElasticSearchConfig() {
        return elasticSearchConfig;
    }

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration getSwaggerConfiguration() {
        return swaggerConfiguration;
    }

    @JsonProperty("swagger")
    public void setSwaggerConfiguration(SwaggerBundleConfiguration swaggerConfiguration) {
        this.swaggerConfiguration = swaggerConfiguration;
    }

    public Map<String, List<String>> getUniversalSearchFieldDetails() {
        return universalSearchFieldDetails;
    }

    public int getKafkaThreadPoolSize() {
        return kafkaThreadPoolSize;
    }

    @JsonProperty("encryption")
    public EncryptionConfig getEncryption() {
        return encryption;
    }

    public Map<String, String> getEventPosting() {
        return eventPosting;
    }

    @JsonProperty("bulkImportConfig")
    public BulkImportConfig getBulkImportConfig() {
        return bulkImportConfig;
    }
}
