package com.cif.syncerservice;


import com.cif.syncerservice.core.syncer.component.ConsumerManager;
import com.cif.syncerservice.core.syncer.component.KafkaProducer;
import com.cif.syncerservice.core.syncer.dto.PartyRequest;
import com.cif.syncerservice.core.syncer.services.QuartzSchedulerCronTriggerService;
import com.cif.syncerservice.core.syncer.services.SyncerService;
import com.cif.syncerservice.db.SyncerRepository;
import com.cif.syncerservice.resources.SyncerResource;
import com.cif.syncerservice.resources.exceptionmappers.GenericExceptionMapper;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.jdbi.v3.core.Jdbi;

import java.net.http.HttpClient;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyncerServiceApplication extends Application<SyncerServiceApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new SyncerServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "cif-syncer-service";
    }

    @Override
    public void initialize(Bootstrap<SyncerServiceApplicationConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(new SubstitutingSourceProvider(bootstrap.getConfigurationSourceProvider(), new EnvironmentVariableSubstitutor(false)));
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(SyncerServiceApplicationConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(SyncerServiceApplicationConfiguration config, Environment environment) throws Exception {
        final ExecutorService executor = Executors.newFixedThreadPool(config.getKafkaThreadPoolSize());
        final JdbiFactory factory = new JdbiFactory();
        final HttpClient httpClient = HttpClient.newBuilder().build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Jdbi jdbi = factory.build(environment, config.getDataSourceFactory(), "postgresql");
        CircuitBreakerRegistry circuitBreakerRegistry = CircuitBreakerRegistry.of(config.getCircuitBreakerConfig());
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker("cif-syncer-service");
        SyncerRepository syncerRepository = jdbi.onDemand(SyncerRepository.class);
        KafkaProducer kafkaProducer = new KafkaProducer(config.getKafkaProducerFactory().getTopics(), config.getKafkaProducerFactory().buildKafkaProducer());
        SyncerService syncerService = new SyncerService(syncerRepository, objectMapper, kafkaProducer, httpClient, circuitBreaker, executor);
        SyncerResource syncerResource = new SyncerResource(syncerService);
        environment.jersey().register(syncerResource);
        environment.jersey().register(new GenericExceptionMapper(environment.metrics(), "syncer-service"));
        ConsumerManager<String, PartyRequest> consumerManager = new ConsumerManager(executor, config.getConsumerBuilderFactory().getTopics(), config.getConsumerBuilderFactory().buildConsumer(), syncerService, syncerRepository);
        consumerManager.startConsumers();
        QuartzSchedulerCronTriggerService cron = new QuartzSchedulerCronTriggerService(syncerRepository, httpClient, config.getQuartzConfiguration());
        cron.fireJob();

    }

}
