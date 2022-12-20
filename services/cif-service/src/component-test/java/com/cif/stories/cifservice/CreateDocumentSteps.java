package com.cif.stories.cifservice;

import com.cif.cifservice.api.CreateDocumentCmd;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.Set;

public class CreateDocumentSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private Set<CreateDocumentCmd> createDocumentCmd;
    private CreateDocumentCmd documentCmd = CreateDocumentCmd.builder().build();
    private final ObjectMapper MAPPER = new ObjectMapper();
    String partyIdentifier;

    public CreateDocumentSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid value in request")
    public void setupMockDataForCreatePartyDocuments() throws IOException {
        partyIdentifier = "12345678912";
        this.documentCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateDocument.json"), CreateDocumentCmd.class);
        this.createDocumentCmd = Collections.singleton(documentCmd);
    }

    @When("User started the create party documents process by making POST call to party documents")
    public void makeApiCallForCreatePartyDocuments() {
        serviceCall();
    }

    @Then("Party documents Created Successfully and returns 201 status code")
    public void verifyStatusForCreatePartyDocuments() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User sends invalid data in request")
    public void setupMockDataForCreatePartyDocumentsValidation() {
        this.partyIdentifier = null;
        this.documentCmd.setDocumentNumber(null);
        this.createDocumentCmd = Collections.singleton(documentCmd);
    }

    @When("Validate the request parameters by making POST call to party documents")
    public void makeApiCallForCreatePartyDocumentsValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code and Failed to create Party Documents")
    public void verifyStatusForCreatePartyDocumentsValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400 || response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + this.partyIdentifier + "/documents")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(this.createDocumentCmd));
    }
}
