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

public class ViewAllContactSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    String partyIdentifier;


    public ViewAllContactSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the required contact data")
    public void setupMockDataForViewAllContacts() {
        this.partyIdentifier = "12345678912";
    }

    @When("Validation process started by making GET call to contacts")
    public void makeFetchContactsApiCallBasedOnExistingPartyId() {
        serviceCall();
    }

    @Then("If Contact exist for party Id api will return 200 with response")
    public void verifyStatusForViewAllContacts() {
        assert response.getStatus() == HttpStatus.OK_200;

    }

    @Given("User provided contact data which not exist in database")
    public void setupMockDataForViewAllContactsValidation() {
        this.partyIdentifier = "12345678912567";
    }

    @When("Validate the request parameters by making GET call to contacts")
    public void makeFetchContactsApiCallBasedOnNonExistingPartyId() {
        serviceCall();
    }

    @Then("If contact for party id is not present then return no data")
    public void verifyNoDataWhenContactsIsPresent() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;

    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/contacts/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
