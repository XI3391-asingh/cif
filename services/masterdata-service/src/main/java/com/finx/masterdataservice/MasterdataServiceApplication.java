package com.finx.masterdataservice;

import com.finx.masterdataservice.core.masterdata.helpers.IdentityFetcher;
import com.finx.masterdataservice.core.masterdata.helpers.JobPositionFetcher;
import com.finx.masterdataservice.core.masterdata.helpers.OccupationFetcher;
import com.finx.masterdataservice.core.masterdata.services.MasterDataService;
import com.finx.masterdataservice.db.IdentityRepositoryImpl;
import com.finx.masterdataservice.db.JobPositionRepositoryImpl;
import com.finx.masterdataservice.db.OccupationRepositoryImpl;
import com.finx.masterdataservice.resources.GitInfoResource;
import com.finx.masterdataservice.resources.MasterDataResource;
import com.finx.masterdataservice.resources.exceptionmappers.FailedToCreateMasterDataExceptionMapper;
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

public class MasterdataServiceApplication extends Application<MasterdataServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MasterdataServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "masterdata-service";
    }

    @Override
    public void initialize(final Bootstrap<MasterdataServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(MasterdataServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final MasterdataServiceConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        jdbi.installPlugin(new Jackson2Plugin());
        environment.jersey().register(new FailedToCreateMasterDataExceptionMapper(environment.metrics()));
        environment.jersey().register(new GitInfoResource());
        environment.jersey().register(
                new MasterDataResource(
                        new MasterDataService(
                                new IdentityFetcher(
                                        new IdentityRepositoryImpl()
                                ),
                                new OccupationFetcher(
                                        new OccupationRepositoryImpl()
                                ),
                                new JobPositionFetcher(
                                        new JobPositionRepositoryImpl()
                                )
                        )
                )
        );
    }

}
