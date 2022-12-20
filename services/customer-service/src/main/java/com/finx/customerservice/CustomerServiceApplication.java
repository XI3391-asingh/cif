package com.finx.customerservice;

import com.finx.customerservice.core.customer.services.PartyService;
import com.finx.customerservice.db.PartyRepository;
import com.finx.customerservice.db.PartyTransactionRepository;
import com.finx.customerservice.resources.GitInfoResource;
import com.finx.customerservice.resources.PartyResource;
import com.finx.customerservice.resources.exceptionmappers.DatabaseExceptionMapper;
import com.finx.customerservice.resources.exceptionmappers.PartyExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class CustomerServiceApplication extends Application<CustomerServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CustomerServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "customer-service";
    }

    @Override
    public void initialize(final Bootstrap<CustomerServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(CustomerServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final CustomerServiceConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        environment.jersey().register(new PartyExceptionMapper(environment.metrics()));
        environment.jersey().register(new DatabaseExceptionMapper(environment.metrics()));
        PartyService partyService = new PartyService(jdbi.onDemand(PartyRepository.class), jdbi.onDemand(PartyTransactionRepository.class));
        PartyResource partyResource = new PartyResource(partyService);
        environment.jersey().register(partyResource);
        environment.jersey().register(new GitInfoResource());
    }

}
