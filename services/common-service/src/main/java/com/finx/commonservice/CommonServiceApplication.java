package com.finx.commonservice;

import com.finx.commonservice.core.otp.services.OtpService;
import com.finx.commonservice.db.OtpDetailRepository;
import com.finx.commonservice.resources.GitInfoResource;
import com.finx.commonservice.resources.OtpResource;
import com.finx.commonservice.resources.exceptionmappers.FailedToCreateOtpExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class CommonServiceApplication extends Application<CommonServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new CommonServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "common-service";
    }

    @Override
    public void initialize(final Bootstrap<CommonServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(CommonServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final CommonServiceConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        environment.jersey().register(new FailedToCreateOtpExceptionMapper(environment.metrics()));
        OtpService otpService = new OtpService(jdbi.onDemand(OtpDetailRepository.class), configuration);
        OtpResource otpResource = new OtpResource(otpService);
        environment.jersey().register(otpResource);
        environment.jersey().register(new GitInfoResource());
    }

}
