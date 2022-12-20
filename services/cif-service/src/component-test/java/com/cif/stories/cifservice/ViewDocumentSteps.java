package com.cif.stories.cifservice;


import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ViewDocumentSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;


    String partyIdentifier;
    Long partyDocumentId;

    public ViewDocumentSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid values are send by user")
    public void setupMockDataForViewPartyDocuments() {
        this.partyIdentifier = "12345678912";
        this.partyDocumentId = 1l;
    }

    @When("Validation process started for getting the document by making GET call to party documents")
    public void makeApiCallForViewPartyDocuments() {
        serviceCall();
    }

    @Then("Success message with status code 200 return")
    public void verifyStatusForViewPartyDocuments() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid values are send by the user in request parameter")
    public void setupMockDataForViewPartyDocumentsValidation() {
        this.partyIdentifier = "1234567891223";
    }

    @When("Validate the request parameters for getting the document by making GET call to party documents")
    public void makeApiCallForViewPartyDocumentsValidation() {
        serviceCall();
    }

    @Then("If party id is not present in the database return no data")
    public void verifyStatusForViewPartyDocumentsValidation() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/documents/" + this.partyDocumentId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
