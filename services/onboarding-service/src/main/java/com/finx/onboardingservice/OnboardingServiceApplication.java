package com.finx.onboardingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.finx.onboardingservice.client.CIFServiceClient;
import com.finx.onboardingservice.client.CommonServiceClient;
import com.finx.onboardingservice.core.onboarding.services.CustomerService;
import com.finx.onboardingservice.core.onboarding.services.KycService;
import com.finx.onboardingservice.db.KycDetailRepository;
import com.finx.onboardingservice.db.WorkflowOnboardingRepository;
import com.finx.onboardingservice.resources.CustomerResource;
import com.finx.onboardingservice.resources.EmailResource;
import com.finx.onboardingservice.resources.GitInfoResource;
import com.finx.onboardingservice.resources.KycResource;
import com.finx.onboardingservice.resources.exceptionmappers.FailedToStartWorkflowExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.jackson2.Jackson2Plugin;

public class OnboardingServiceApplication extends Application<OnboardingServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new OnboardingServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "onboarding-service";
    }

    @Override
    public void initialize(final Bootstrap<OnboardingServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(OnboardingServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final OnboardingServiceConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        jdbi.installPlugin(new Jackson2Plugin());
        environment.jersey().register(new FailedToStartWorkflowExceptionMapper(environment.metrics()));
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        //CLIENT
        var cifServiceClient = new CIFServiceClient(configuration.getUrlSearchMobile());
        var commonServiceClient = new CommonServiceClient(configuration.getUrlCheckBlockingPeriod());
        //REPOSITORIES
        var workflowOnboardingRepository = jdbi.onDemand(WorkflowOnboardingRepository.class);
        var kycDetailRepository = jdbi.onDemand(KycDetailRepository.class);
        //SERVICES
        var customerService = new CustomerService(cifServiceClient, workflowOnboardingRepository, commonServiceClient);
        var kycService = new KycService(kycDetailRepository, workflowOnboardingRepository, objectMapper);
        //RESOURCES
        var customerResource = new CustomerResource(customerService);
        var kycResource = new KycResource(kycService);
        var emailResource = new EmailResource();

        environment.jersey().register(emailResource);
        environment.jersey().register(kycResource);
        environment.jersey().register(customerResource);
        environment.jersey().register(new GitInfoResource());
    }

}
