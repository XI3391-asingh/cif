package com.cif.stories.cifservice;

import com.cif.cifservice.api.PartyDocumentCmd;
import com.cif.cifservice.api.PartyRequestCmd;
import com.cif.cifservice.api.UpdateAddressCmd;
import com.cif.cifservice.api.UpdateDocumentsCmd;
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

public class UpdateDocumentSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();
    UpdateDocumentsCmd updateDocumentsCmd;

    String partyIdentifier;
    Long partyDocumentId;

    public UpdateDocumentSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends valid value in request")
    public void setupMockDataForUpdatePartyDocuments() throws IOException {
        partyIdentifier = "12345678912";
        ;
        this.partyDocumentId = 1l;
        this.updateDocumentsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateDocument.json"), UpdateDocumentsCmd.class);

    }

    @When("Validation process started by making PUT call to party documents")
    public void makeApiCallForUpdatePartyDocuments() {
        serviceCall();
    }

    @Then("Party Documents Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdatePartyDocuments() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User sends invalid value in request")
    public void setupMockDataForUpdatePartyDocumentsValidation() {
        partyIdentifier = "12345678912";
        partyDocumentId = 0l;
    }

    @When("Validate the request parameters by making PUT call to party documents")
    public void makeApiCallForUpdatePartyDocumentsValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of party documents exist")
    public void verifyStatusForUpdatePartyDocumentsValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/documents/" + partyDocumentId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(this.updateDocumentsCmd));
    }

}
