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

public class ViewAllRiskSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    String partyIdentifier;


    public ViewAllRiskSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the required party risks data")
    public void setupMockDataForViewAllPartyRisks() {
        this.partyIdentifier = "12345678912";
    }

    @When("Validation process started by making GET call to risks")
    public void makeFetchAllPartyRisksApiCallBasedOnExistingPartyId() {
        serviceCall();
    }

    @Then("If party risks exist for party Id api will return 200 with response")
    public void verifyStatusForViewAllPartyRisks() {
        assert response.getStatus() == HttpStatus.OK_200;

    }

    @Given("User provided party risks data which not exist in database")
    public void setupMockDataForViewAllPartyRisksValidation() {
        this.partyIdentifier = "12345678912789";
    }

    @When("Validate the request parameters by making GET call to risks")
    public void makeFetchAllPartyRisksApiCallBasedOnNonExistingPartyId() {
        serviceCall();
    }

    @Then("If party risks for party id is not present then return no data")
    public void verifyNoDataWhenAllPartyRisksIsPresent() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;

    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/risks/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
