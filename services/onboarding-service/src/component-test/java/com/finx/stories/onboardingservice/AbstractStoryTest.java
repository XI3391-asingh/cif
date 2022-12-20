package com.finx.stories.onboardingservice;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.CONSOLE;

import com.finx.onboardingservice.OnboardingServiceApplication;
import com.finx.onboardingservice.OnboardingServiceConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import java.util.List;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.client.Client;
import org.glassfish.jersey.client.ClientProperties;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnitStory;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractStoryTest extends JUnitStory {

    private static final String CONFIG = "component-test-config.yml";
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;
    public static final DropwizardTestSupport<OnboardingServiceConfiguration> SUPPORT;
    public static final Client CLIENT;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"));
        POSTGRES_CONTAINER.start();
        SUPPORT =
                new DropwizardTestSupport<>(OnboardingServiceApplication.class,
                        ResourceHelpers.resourceFilePath(CONFIG),
                        ConfigOverride.config("server.applicationConnectors[0].port", "0"),
                        ConfigOverride.config("database.url", POSTGRES_CONTAINER::getJdbcUrl),
                        ConfigOverride.config("database.user", POSTGRES_CONTAINER::getUsername),
                        ConfigOverride.config("database.password", POSTGRES_CONTAINER::getPassword),
                        ConfigOverride.config("database.properties.enabledTLSProtocols", "TLSv1.1,TLSv1.2,TLSv1.3")
                );

        try {
            SUPPORT.before();
            SUPPORT.getApplication().run("db", "migrate", ResourceHelpers.resourceFilePath(CONFIG));
            CLIENT = new JerseyClientBuilder(SUPPORT.getEnvironment())
                .withProperty(ClientProperties.READ_TIMEOUT, 1000)
                .build("test client");
        } catch (Exception e) {
            throw new InternalServerErrorException("Failed to start", e);
        }
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(this.getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withCodeLocation(codeLocationFromClass(this.getClass()))
                        .withFormats(CONSOLE));
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), getStepClasses());
    }

    protected Client getClient() {
        return CLIENT;
    }

    protected int getLocalPort() {
        return SUPPORT.getLocalPort();
    }

    protected abstract List<?> getStepClasses();
}