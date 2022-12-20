package com.masterdata.masterdataservice;

import com.masterdata.masterdataservice.core.masterdata.services.MasterDataService;
import com.masterdata.masterdataservice.db.MasterDataRepository;
import com.masterdata.masterdataservice.resources.GitInfoResource;
import com.masterdata.masterdataservice.resources.MasterDataResource;
import com.masterdata.masterdataservice.resources.exceptionmappers.ConstraintViolationExceptionMapper;
import com.masterdata.masterdataservice.resources.exceptionmappers.GenericExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class MasterDataServiceApplication extends Application<MasterDataServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MasterDataServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "masterdata-service";
    }

    @Override
    public void initialize(final Bootstrap<MasterDataServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(MasterDataServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final MasterDataServiceConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        environment.jersey().register(new ConstraintViolationExceptionMapper(environment.metrics(), getName()));
        environment.jersey().register(new GenericExceptionMapper(environment.metrics(), getName()));
        MasterDataService masterdataService = new MasterDataService(jdbi.onDemand(MasterDataRepository.class));
        MasterDataResource masterdataResource = new MasterDataResource(masterdataService);
        environment.jersey().register(masterdataResource);
        environment.jersey().register(new GitInfoResource());
    }

}
