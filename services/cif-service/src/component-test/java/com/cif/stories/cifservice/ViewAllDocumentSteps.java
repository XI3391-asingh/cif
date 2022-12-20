package com.cif.stories.cifservice;


import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class ViewAllDocumentSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    String partyIdentifier;

    public ViewAllDocumentSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the required value in parameters")
    public void setupMockDataForViewAllPartyDocuments() {
        this.partyIdentifier = "12345678912";
    }

    @When("Validation process started by making GET call to party documents")
    public void makeFetchDocumentsApiCallBasedOnExistingPartyId() {
        serviceCall();
    }

    @Then("If documents exist for party id then api will return 200 with response")
    public void verifyStatusForViewAllPartyDocuments() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User provides data which does not exist in database")
    public void setupMockDataForViewAllPartyDocumentsValidation() {
        this.partyIdentifier = "12345678912567";
    }

    @When("Validate the request parameters by making GET call to party documents")
    public void makeFetchDocumentsApiCallBasedOnNonExistingPartyId() {
        serviceCall();
    }

    @Then("If documents for party id is not present then return no data")
    public void verifyNoDataWhenDocumentsIsNotPresent() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/documents/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
