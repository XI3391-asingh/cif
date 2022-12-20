package com.cif.stories.cifservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class ViewFatcaSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;

    String partyIdentifier;
    Long partyFatcaId;

    public ViewFatcaSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid party fatca values sent by the user")
    public void setupMockDataForViewPartyFatca() {
        partyIdentifier = "12345678912";
        partyFatcaId = 1l;
    }

    @When("Validation process started for getting the party fatca details by making GET call to party Fatca")
    public void makeApiCallForViewPartyFatca() {
        serviceCall();
    }

    @Then("Success message for party fatca and returns 200 status code")
    public void verifyStatusForViewPartyFatca() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid party fatca data factors are sent by user in request")
    public void setupMockDataForViewPartyFatcaValidation() {
        partyIdentifier = "1234567891298";
    }

    @When("Validate the request for getting the party fatca factors by making GET call to party fatca")
    public void makeApiCallForViewPartyFatcaValidation() {
        serviceCall();
    }

    @Then("If data factors not present in the database return no data for party fatca")
    public void verifyStatusForViewPartyFatcaValidation() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/fatca/" + this.partyFatcaId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
