package com.finx.sampleservice;

import com.finx.sampleservice.core.task.services.TaskService;
import com.finx.sampleservice.db.TaskRepository;
import com.finx.sampleservice.resources.TaskResource;
import com.finx.sampleservice.resources.GitInfoResource;
import com.finx.sampleservice.resources.exceptionmappers.FailedToCreateTaskExceptionMapper;
import io.dropwizard.Application;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.PooledDataSourceFactory;
import io.dropwizard.jdbi3.JdbiFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.jdbi.v3.core.Jdbi;

public class SampleServiceApplication extends Application<SampleServiceConfiguration> {

    public static void main(final String[] args) throws Exception {
        new SampleServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "sample-service";
    }

    @Override
    public void initialize(final Bootstrap<SampleServiceConfiguration> bootstrap) {
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );
        bootstrap.addBundle(new MigrationsBundle<>() {
            @Override
            public PooledDataSourceFactory getDataSourceFactory(SampleServiceConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final SampleServiceConfiguration configuration,
                    final Environment environment) {
        final JdbiFactory factory = new JdbiFactory();
        Jdbi jdbi = factory.build(environment, configuration.getDataSourceFactory(), "postgresql");
        environment.jersey().register(new FailedToCreateTaskExceptionMapper(environment.metrics()));
        TaskService taskService = new TaskService(jdbi.onDemand(TaskRepository.class));
        TaskResource taskResource = new TaskResource(taskService);
        environment.jersey().register(taskResource);
        environment.jersey().register(new GitInfoResource());
    }

}
