package com.cif.stories.cifservice;

import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DeleteDocumentSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;


    String partyIdentifier;
    Long partyDocumentId;

    public DeleteDocumentSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User has sent valid value in request")
    public void setupMockDataForDeletePartyDocuments() {
        partyIdentifier = "12345678912";
        partyDocumentId = 1l;
    }

    @When("Validation process by making DELETE call to party document")
    public void makeApiCallForDeletePartyDocuments() {
        serviceCall();
    }

    @Then("Party Documents Delete Success for partyId and returns 200 status code")
    public void verifyStatusForDeletePartyDocuments() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User has sent invalid value in request")
    public void setupMockDataForDeletePartyDocumentsValidation() {
        partyDocumentId = 0l;
    }

    @When("Validate the request parameters by making DELETE call to party documents")
    public void makeApiCallForDeletePartyDocumentsValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of party document exist")
    public void verifyStatusForDeletePartyDocumentsValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/document/" + partyDocumentId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
    }

}

