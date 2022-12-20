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

public class ViewAllAddressSteps extends Steps {

    private final Client client;
    private final int localPort;
    private Response response;
    String partyIdentifier;


    public ViewAllAddressSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the required data")
    public void setupMockDataForViewAllPartyAddress() {
        this.partyIdentifier = "12345678912";
    }

    @When("Validation process started by making GET call to party address")
    public void makeFetchAddressApiCallBasedOnExistingPartyId() {
        serviceCall();
    }

    @Then("If Address exist for party Id api will return 200 with response")
    public void verifyStatusForViewAllPartyAddress() {
        assert response.getStatus() == HttpStatus.OK_200;

    }

    @Given("User provided data which not exist in database")
    public void setupMockDataForViewAllPartyAddressValidation() {
        this.partyIdentifier = "12345678912123";
    }

    @When("Validate the request parameters by making GET call to party address")
    public void makeFetchAddressApiCallBasedOnNonExistingPartyId() {
        serviceCall();
    }

    @Then("If address for party id is not present then return no data")
    public void verifyNoDataWhenAddressIsNotPresent() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;

    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/address/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
