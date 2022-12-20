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

public class ViewAllFatcaSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    String partyIdentifier;

    public ViewAllFatcaSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the party id to request for fatca")
    public void setupMockDataForViewAllPartyFatca() {
        this.partyIdentifier = "12345678912";
    }

    @When("Validation process started by making GET call to party Fatca")
    public void makeFetchFatcaApiCallBasedOnExistingPartyId() {
        serviceCall();
    }

    @Then("If fatca details exist for party id then api will return 200 with response")
    public void verifyStatusForViewAllPartyFatca() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User provides party id which does not exist")
    public void setupMockDataForViewAllPartyFatcaValidation() {
        this.partyIdentifier = "1234567891234";
    }

    @When("Validate the request parameters by making GET call to party fatca")
    public void makeFetchFatcaApiCallBasedOnNonExistingPartyId() {
        serviceCall();
    }

    @Then("If fatca details for party id is not present then return no data")
    public void verifyNoDataWhenFatcaIsNotPresent() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/fatca")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }

}
