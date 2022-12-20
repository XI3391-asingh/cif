package com.cif.stories.cifservice;

import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ViewAddressSteps extends Steps {

    private final Client client;
    private final int localPort;
    private Response response;


    String partyIdentifier;
    Long partyAddressId;

    public ViewAddressSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid values are send by the user")
    public void setupMockDataForViewPartyAddress() {
        this.partyIdentifier = "12345678912";
        this.partyAddressId = 1l;

    }

    @When("Validation process started for getting the address by making GET call to party address")
    public void makeApiCallForViewPartyAddress() {
        serviceCall();
    }

    @Then("Success message and returns 200 status code")
    public void verifyStatusForViewPartyAddress() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid data are send by user in request parameter")
    public void setupMockDataForViewPartyAddressValidation() {
        this.partyIdentifier = "1234567";
    }

    @When("Validate the request parameters for getting the address by making GET call to party address")
    public void makeApiCallForViewPartyAddressValidation() {
        serviceCall();
    }

    @Then("No record found and returns 404 status code")
    public void verifyStatusForViewPartyAddressValidation() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + this.partyIdentifier + "/address/" + this.partyAddressId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
