package com.cif.stories.cifservice;


import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DeleteAddressSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;


    String partyIdentifier;
    Long partyAddressId;

    public DeleteAddressSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid address input parameter is sent by user")
    public void setupMockDataForDeletePartyAddress() {
        partyIdentifier = "12345678912";
        partyAddressId = 1l;
    }

    @When("Validation process started by making DELETE call to party address")
    public void makeApiCallForDeletePartyAddress() {
        serviceCall();
    }

    @Then("Party Address Deleted Successfully for partyId and returns 200 success status code")
    public void verifyStatusForDeletePartyAddress() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid address input parameter sent by user")
    public void setupMockDataForDeletePartyAddressValidation() {
        partyAddressId = 0l;
    }

    @When("Validate the request parameters by making DELETE call to party address")
    public void makeApiCallForDeletePartyAddressValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of party address does not exist")
    public void verifyStatusForDeletePartyAddressValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/address/" + partyAddressId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
    }


}