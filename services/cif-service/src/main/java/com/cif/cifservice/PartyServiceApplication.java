package com.cif.cifservice;

import com.cif.cifservice.core.party.event.EventListener;
import com.cif.cifservice.core.party.services.AdminService;
import com.cif.cifservice.core.party.services.PartyService;
import com.cif.cifservice.core.party.services.consumer.ConsumerManager;
import com.cif.cifservice.core.party.services.producer.ProducerService;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.db.PartyRepository;
import com.cif.cifservice.db.PartyTransactionRepository;
import com.cif.cifservice.db.elasticsearch.ElasticsearchAdminRepository;
import com.cif.cifservice.db.elasticsearch.ElasticsearchClientRepository;
import com.cif.cifservice.filters.RequestIdMdcBundle;
import com.cif.cifservice.filters.ResponseFilter;
import com.cif.cifservice.filters.RateLimiterFilter;
import com.cif.cifservice.resources.AdminResource;
import com.cif.cifservice.resources.GitInfoResource;
import com.cif.cifservice.resources.PartyResource;
import com.cif.cifservice.resources.exceptionmappers.ConstraintViolationExceptionMapper;
import com.cif.cifservice.resources.exceptionmappers.GenericExceptionMapper;
import com.cif.cifservice.resources.exceptionmappers.JsonCustomExceptionMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.eventbus.EventBus;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.forms.MultiPartBundle;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.jdbi.v3.core.Jdbi;

import java.net.http.HttpClient;

public class PartyServiceApplication extends Application<PartyServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new PartyServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "cif-service";
    }

    @Override
    public void initialize(final Bootstrap<PartyServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(PartyServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });

        bootstrap.addBundle(new SwaggerBundle<>() {
            @Override
            protected SwaggerBundleConfiguration
            getSwaggerBundleConfiguration(PartyServiceConfiguration configuration) {
                return configuration.getSwaggerConfiguration();
            }
        });

        bootstrap.addBundle(new RequestIdMdcBundle());
        bootstrap.addBundle(new MultiPartBundle());
    }

    @Override
    public void run(final PartyServiceConfiguration configuration, final Environment environment) {
        ObjectMapper objectMapper = new ObjectMapper();
        ((DefaultServerFactory) configuration.getServerFactory()).setRegisterDefaultExceptionMappers(false);
        environment.jersey().register(new ConstraintViolationExceptionMapper(environment.metrics(), getName()));
        environment.jersey().register(new GenericExceptionMapper(environment.metrics(), getName()));
        environment.jersey().register(new JsonCustomExceptionMapper(environment.metrics(), getName()));

        final JdbiFactory factory = new JdbiFactory();
        HttpClient client = HttpClient.newHttpClient();
        ElasticsearchAdminRepository adminRepository = new ElasticsearchAdminRepository(configuration.getElasticSearchConfig(), client, objectMapper);
        CryptoUtil cryptoUtil = new CryptoUtil(configuration.getEncryption(), adminRepository);
        ElasticsearchClientRepository clientRepository = new ElasticsearchClientRepository(configuration.getElasticSearchConfig(), client, cryptoUtil);
        Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        ProducerService producerService = null;
        if (configuration.getEventPosting().get("allow").equalsIgnoreCase("true")) {
            producerService = new ProducerService(configuration.getKafkaProducerFactory().buildKafkaProducer(), configuration.getKafkaProducerFactory().getTopic());
        }
        PartyRepository partyRepository = jdbi.onDemand(PartyRepository.class);
        final EventBus eventBus = new EventBus();
        final PartyService partyService = new PartyService(partyRepository, jdbi.onDemand(PartyTransactionRepository.class), eventBus, clientRepository, configuration.getUniversalSearchFieldDetails(), cryptoUtil);
        final AdminService adminService = new AdminService(adminRepository, cryptoUtil, partyRepository, configuration.getBulkImportConfig());
        final PartyResource partyResource = new PartyResource(partyService);
        final AdminResource adminResource = new AdminResource(adminService);
        environment.jersey().register(adminResource);
        environment.jersey().register(partyResource);
        environment.jersey().register(new GitInfoResource());
        environment.jersey().register(new ResponseFilter(clientRepository));
        HazelcastInstance hazelcast = Hazelcast.newHazelcastInstance();
        environment.jersey().register(new RateLimiterFilter(hazelcast, adminRepository));

        eventBus.register(new EventListener(producerService, partyService, eventBus, clientRepository, cryptoUtil));
        if (configuration.getEventPosting().get("allow").equalsIgnoreCase("true")) {
            ConsumerManager consumerManager = new ConsumerManager(configuration.getKafkaConsumerFactory().getTopics(), partyService, configuration.getKafkaConsumerFactory().buildConsumer(), objectMapper);
            Thread consumerThread = new Thread(consumerManager);
            consumerThread.start();
        }
    }

}
