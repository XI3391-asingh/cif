package com.cif.stories.nomineeservice;

import com.cif.nomineeservice.NomineeServiceApplication;
import com.cif.nomineeservice.NomineeServiceConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.testing.ConfigOverride;
import io.dropwizard.testing.DropwizardTestSupport;
import io.dropwizard.testing.ResourceHelpers;
import org.glassfish.jersey.client.ClientProperties;
import org.jbehave.core.configuration.Configuration;
import org.jbehave.core.configuration.MostUsefulConfiguration;
import org.jbehave.core.embedder.Embedder;
import org.jbehave.core.io.LoadFromClasspath;
import org.jbehave.core.junit.JUnit4StoryRunner;
import org.jbehave.core.junit.JUnitStories;
import org.jbehave.core.reporters.StoryReporterBuilder;
import org.jbehave.core.steps.InjectableStepsFactory;
import org.jbehave.core.steps.InstanceStepsFactory;
import org.junit.runner.RunWith;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import javax.ws.rs.client.Client;
import java.util.List;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.*;

@RunWith(JUnit4StoryRunner.class)
public class StoryRunner extends JUnitStories {

    public static final DropwizardTestSupport<NomineeServiceConfiguration> SUPPORT;
    public static final Client CLIENT;
    private static final String CONFIG = "component-test-config.yml";
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"));
        POSTGRES_CONTAINER.start();
        SUPPORT =
                new DropwizardTestSupport<>(NomineeServiceApplication.class,
                        ResourceHelpers.resourceFilePath(CONFIG),
                        ConfigOverride.config("server.applicationConnectors[0].port", "0"),
                        ConfigOverride.config("database.url", POSTGRES_CONTAINER::getJdbcUrl),
                        ConfigOverride.config("database.user", POSTGRES_CONTAINER::getUsername),
                        ConfigOverride.config("database.password", POSTGRES_CONTAINER::getPassword),
                        ConfigOverride.config("database.properties.enabledTLSProtocols", "TLSv1.1,TLSv1.2,TLSv1.3"));

        try {
            SUPPORT.before();
            SUPPORT.getApplication().run("db", "migrate", ResourceHelpers.resourceFilePath(CONFIG));
            CLIENT = new JerseyClientBuilder(SUPPORT.getEnvironment())
                    .withProperty(ClientProperties.READ_TIMEOUT, 0)
                    .build("test client");
        } catch (Exception e) {
            throw new RuntimeException("Failed to start", e);
        }
    }

    public StoryRunner() {
        Embedder embedder = configuredEmbedder();
        embedder.embedderControls()
                .doVerboseFailures(true)
                .useStoryTimeouts("10000");
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(this.getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withCodeLocation(codeLocationFromClass(this.getClass()))
                        .withFormats(CONSOLE, HTML, TXT));
    }

    protected Client getClient() {
        return CLIENT;
    }

    protected int getLocalPort() {
        return SUPPORT.getLocalPort();
    }

    @Override
    public InjectableStepsFactory stepsFactory() {
        return new InstanceStepsFactory(configuration(), setStepsFactory());
    }

    @Override
    public List<String> storyPaths() {
        return setStoryPath();
    }

    private List<String> setStoryPath() {
        return List.of("stories/create_nominee.story", "stories/update_nominee.story", "stories/delete_nominee.story", "stories/create_nominee_mapping.story", "stories/delete_nominee_mapping.story");
    }

    private List<Object> setStepsFactory() {
        return List.of(new CreateNomineesSteps(getClient(), getLocalPort()), new UpdateNomineesSteps(getClient(), getLocalPort()),
                new DeleteNomineesSteps(getClient(), getLocalPort()), new CreateNomineeMappingSteps(getClient(), getLocalPort()), new DeleteNomineeMappingSteps(getClient(), getLocalPort()));
    }
}
