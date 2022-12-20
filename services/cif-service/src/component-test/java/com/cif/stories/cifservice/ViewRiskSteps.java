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

public class ViewRiskSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;

    String partyIdentifier;
    Long partyRiskId;

    public ViewRiskSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid party risk values are send by the user")
    public void setupMockDataForViewPartyRisk() {
        partyIdentifier = "12345678912";
        partyRiskId = 1l;

    }

    @When("Validation process started for getting the party risk by making GET call to party risk")
    public void makeApiCallForViewPartyRisk() {
        serviceCall();
    }

    @Then("Success message for party risk and returns 200 status code")
    public void verifyStatusForViewPartyRisk() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid party risk data are send by user in request parameter")
    public void setupMockDataForViewPartyRiskValidation() {
        partyIdentifier = "1234567891287";
    }

    @When("Validate the request parameters for getting the party risk by making GET call to party risk")
    public void makeApiCallForViewPartyRiskValidation() {
        serviceCall();
    }

    @Then("If party id not present in the database return no data for party risk")
    public void verifyStatusForViewPartyRiskValidation() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/risks/" + this.partyRiskId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
