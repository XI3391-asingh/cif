package com.cif.

        nomineeservice;

import com.cif.nomineeservice.core.nominee.services.NomineeService;
import com.cif.nomineeservice.db.NomineeRepository;
import com.cif.nomineeservice.resources.GitInfoResource;
import com.cif.nomineeservice.resources.NomineeResource;
import com.cif.nomineeservice.resources.exceptionmappers.GenericExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class NomineeServiceApplication extends Application<NomineeServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new NomineeServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "nominee-service";
    }

    @Override
    public void initialize(final Bootstrap<NomineeServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(NomineeServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final NomineeServiceConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        environment.jersey().register(new GenericExceptionMapper(environment.metrics()));
        NomineeService nomineeService = new NomineeService(jdbi.onDemand(NomineeRepository.class));
        NomineeResource nomineeResource = new NomineeResource(nomineeService);
        environment.jersey().register(nomineeResource);
        environment.jersey().register(new GitInfoResource());
    }

}
