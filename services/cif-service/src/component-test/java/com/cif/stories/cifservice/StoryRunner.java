package com.cif.stories.cifservice;


import com.cif.cifservice.PartyServiceApplication;
import com.cif.cifservice.PartyServiceConfiguration;
import com.cif.cifservice.resources.exceptions.ServerSideException;
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
import org.jbehave.core.steps.StepFinder;
import org.junit.runner.RunWith;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.utility.DockerImageName;

import javax.ws.rs.client.Client;
import java.util.ArrayList;
import java.util.List;

import static org.jbehave.core.io.CodeLocations.codeLocationFromClass;
import static org.jbehave.core.reporters.Format.*;

@RunWith(JUnit4StoryRunner.class)
public class  StoryRunner extends JUnitStories {

    public static final DropwizardTestSupport<PartyServiceConfiguration> SUPPORT;
    public static final Client CLIENT;
    private static final String CONFIG = "component-test-config.yml";
    private static final PostgreSQLContainer<?> POSTGRES_CONTAINER;

    private static final KafkaContainer KAFKA_CONTAINER;
    private static final ElasticsearchContainer ELASTICSEARCH_CONTAINER;

    private static final DockerImageName KAFKA_IMAGE = DockerImageName.parse("confluentinc/cp-kafka:6.2.1");
    private static final DockerImageName ELASTIC_SEARCH_IMAGE = DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:6.5.1");


    static {
        POSTGRES_CONTAINER = new PostgreSQLContainer<>(DockerImageName.parse("postgres:13.3"));
        POSTGRES_CONTAINER.start();
        KAFKA_CONTAINER = new KafkaContainer(KAFKA_IMAGE);
        KAFKA_CONTAINER.start();
        ELASTICSEARCH_CONTAINER=new ElasticsearchContainer(ELASTIC_SEARCH_IMAGE);
        ELASTICSEARCH_CONTAINER.start();
        SUPPORT =
                new DropwizardTestSupport<>(PartyServiceApplication.class,
                        ResourceHelpers.resourceFilePath(CONFIG),
                        ConfigOverride.config("server.applicationConnectors[0].port", "0"),
                        ConfigOverride.config("database.url", POSTGRES_CONTAINER::getJdbcUrl),
                        ConfigOverride.config("database.user", POSTGRES_CONTAINER::getUsername),
                        ConfigOverride.config("database.password", POSTGRES_CONTAINER::getPassword),
                        ConfigOverride.config("database.properties.enabledTLSProtocols", "TLSv1.1,TLSv1.2,TLSv1.3"),
                        ConfigOverride.config("producer.bootstrapServers", KAFKA_CONTAINER.getBootstrapServers()),
                        ConfigOverride.config("elasticsearch.host", String.valueOf("http://"+ELASTICSEARCH_CONTAINER.getHost())),
                        ConfigOverride.config("elasticsearch.port", String.valueOf(ELASTICSEARCH_CONTAINER.getMappedPort(9200)))

                );

        try {
            SUPPORT.before();
            SUPPORT.getApplication().run("db", "migrate", ResourceHelpers.resourceFilePath(CONFIG));
            CLIENT = new JerseyClientBuilder(SUPPORT.getEnvironment())
                    .withProperty(ClientProperties.READ_TIMEOUT, 0)
                    .withProperty(ClientProperties.CONNECT_TIMEOUT, 0)
                    .build("test client");
        } catch (Exception e) {
            throw new ServerSideException("UNABLE_START_CONTAINER", e.getMessage(), e);
        }
    }

    public StoryRunner() {
        Embedder embedder = configuredEmbedder();
        embedder.embedderControls()
                .doVerboseFailures(true)
                .useStoryTimeouts("10000000");
    }

    @Override
    public Configuration configuration() {
        return new MostUsefulConfiguration()
                .useStoryLoader(new LoadFromClasspath(this.getClass()))
                .useStoryReporterBuilder(new StoryReporterBuilder()
                        .withDefaultFormats()
                        .withCodeLocation(codeLocationFromClass(this.getClass()))
                        .withFormats(CONSOLE, HTML, TXT))
                .useStepFinder(new StepFinder());


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
        var storyPaths = new ArrayList<String>();
        storyPaths.addAll(setStoryPathForParty());
        storyPaths.addAll(setStoryPathForAddress());
        storyPaths.addAll(setStoryPathForContacts());
        storyPaths.addAll(setStoryPathForDocuments());
        storyPaths.addAll(setStoryPathForFatca());
        storyPaths.addAll(setStoryPathForMemos());
        storyPaths.addAll(setStoryPathForRisk());
        storyPaths.addAll(setStoryPathForGuardian());
        storyPaths.addAll(setStoryPathForAdminData());
        return storyPaths;
    }

    private List<Object> setStepsFactory() {
        Client client = getClient();
        int port = getLocalPort();
        var storyFactorySteps = new ArrayList<>();
        storyFactorySteps.addAll(setStepsFactoryForParty(client, port));
        storyFactorySteps.addAll(setStepsFactoryForAddress(client, port));
        storyFactorySteps.addAll(setStepsFactoryForContacts(client, port));
        storyFactorySteps.addAll(setStepsFactoryForDocuments(client, port));
        storyFactorySteps.addAll(setStepsFactoryForFatca(client, port));
        storyFactorySteps.addAll(setStepsFactoryForMemos(client, port));
        storyFactorySteps.addAll(setStepsFactoryForRisk(client, port));
        storyFactorySteps.addAll(setStepsFactoryForGuardian(client, port));
        storyFactorySteps.addAll(setStepsFactoryForAdminData(client,port));
        return storyFactorySteps;
    }

    private List<String> setStoryPathForParty() {
        return List.of("stories/01/step1_create_party.story", "stories/01/step3_update_party.story", "stories/01/step4_update_party_email.story", "stories/01/step6_update_party_mobile_number.story",
                "stories/01/step7_update_party_status.story", "stories/01/step8_party_fetch_by_cif_ids.story", "stories/01/step5_update_party_for_soft_delete.story", "stories/01/step2_dedupe_verification.story");
    }

    private List<Object> setStepsFactoryForParty(Client client, int port) {
        return List.of(new CreatePartySteps(client, port), new UpdatePartySteps(client, port), new UpdatePartyEmailSteps(client, port), new UpdatePartyMobileNumberSteps(client, port),
                new UpdatePartyStatusSteps(client, port), new FetchPartyByCifIdSteps(client, port), new UpdatePartyForSoftDeleteSteps(client, port), new DedupeVerificationSteps(client, port));
    }

    private List<String> setStoryPathForAddress() {
        return List.of("stories/02/step1_create_address.story", "stories/02/step2_update_address.story", "stories/02/step3_view_all_address.story",
                "stories/02/step4_view_address.story", "stories/02/step5_delete_address.story");
    }

    private List<Object> setStepsFactoryForAddress(Client client, int port) {
        return List.of(new CreateAddressSteps(client, port), new UpdateAddressSteps(client, port), new ViewAddressSteps(client, port),
                new ViewAllAddressSteps(client, port), new DeleteAddressSteps(client, port));
    }

    private List<String> setStoryPathForContacts() {
        return List.of("stories/03/step1_create_contacts.story", "stories/03/step2_update_contacts.story", "stories/03/step3_view_all_contacts.story",
                "stories/03/step4_view_contacts.story", "stories/03/step5_delete_contacts.story");
    }

    private List<Object> setStepsFactoryForContacts(Client client, int port) {
        return List.of(new CreateContactSteps(client, port), new UpdateContactSteps(client, port), new ViewAllContactSteps(client, port),
                new ViewContactSteps(client, port), new DeleteContactSteps(client, port));
    }

    private List<String> setStoryPathForDocuments() {
        return List.of("stories/04/step1_create_document.story", "stories/04/step2_update_document.story", "stories/04/step3_view_all_document.story",
                "stories/04/step4_view_document.story", "stories/04/step5_delete_document.story");
    }

    private List<Object> setStepsFactoryForDocuments(Client client, int port) {
        return List.of(new CreateDocumentSteps(client, port), new UpdateDocumentSteps(client, port), new ViewAllDocumentSteps(client, port),
                new ViewDocumentSteps(client, port), new DeleteDocumentSteps(client, port));
    }

    private List<String> setStoryPathForFatca() {
        return List.of("stories/05/step1_create_fatca.story", "stories/05/step2_update_fatca.story", "stories/05/step3_view_all_fatca.story",
                "stories/05/step4_view_fatca.story");
    }

    private List<Object> setStepsFactoryForFatca(Client client, int port) {
        return List.of(new CreateFatcaSteps(client, port), new CreateFatcaSteps(client, port), new ViewAllFatcaSteps(client, port),
                new ViewFatcaSteps(client, port));
    }

    private List<String> setStoryPathForMemos() {
        return List.of("stories/06/step1_create_memos.story", "stories/06/step2_update_memos.story", "stories/06/step3_view_all_memos.story",
                "stories/06/step4_view_memos.story");
    }

    private List<Object> setStepsFactoryForMemos(Client client, int port) {
        return List.of(new CreateMemoSteps(client, port), new CreateMemoSteps(client, port), new ViewAllMemoSteps(client, port),
                new ViewMemoSteps(client, port));
    }

    private List<String> setStoryPathForRisk() {
        return List.of("stories/07/step1_create_risk.story", "stories/07/step2_update_risk.story", "stories/07/step3_view_all_risk.story",
                "stories/07/step4_view_risk.story");
    }


    private List<Object> setStepsFactoryForRisk(Client client, int port) {
        return List.of(new CreateRiskSteps(client, port), new CreateRiskSteps(client, port), new ViewAllRiskSteps(client, port),
                new ViewRiskSteps(client, port));
    }

    private List<String> setStoryPathForXref() {
        return List.of("stories/08/step1_view_all_xref.story");
    }
    private List<Object> setStepsFactoryForXref(Client client, int port) {
        return List.of(new ViewAllXrefSteps(client, port));
    }

    private List<String> setStoryPathForGuardian() {
        return List.of("stories/08/step1_create_guardian.story", "stories/08/step2_delete_guardian.story");
    }

    private List<Object> setStepsFactoryForGuardian(Client client, int port) {
        return List.of(new CreateGuardianSteps(client, port), new DeleteGuardianSteps(client, port));
    }
    private List<String> setStoryPathForAdminData() {
        return List.of("stories/00/step1_create_admin_data.story","stories/00/step2_view_admin_data.story");
    }
    private List<Object> setStepsFactoryForAdminData(Client client, int port) {
        return List.of(new CreateAdminDataSteps(client, port), new ViewAdminDataSteps(client,port));
    }

}
