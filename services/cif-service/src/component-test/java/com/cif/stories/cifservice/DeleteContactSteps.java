package com.cif.stories.cifservice;


import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DeleteContactSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;


    String partyIdentifier;
    Long partyContactId;


    public DeleteContactSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User send valid data parameter in request for deletion")
    public void setupMockDataForDeleteContact() {
        partyIdentifier = "12345678912";
        partyContactId = 1l;

    }

    @When("Validation process started by making DELETE call to party contact")
    public void makeApiCallForDeleteContact() {
        serviceCall();
    }

    @Then("Party Contact deletion Success for partyId and returns 200 status code")
    public void verifyStatusForDeleteContact() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User send request parameters which is invalid data")
    public void setupMockDataForDeleteContactValidation() {
        partyContactId = 0l;
    }

    @When("Validate the request parameters by making DELETE call to party contact")
    public void makeApiCallForDeleteContactValidation() {
        serviceCall();
    }

    @Then("Service returns Party Contact Delete Failed and return status code 400")
    public void verifyStatusForDeleteContactValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/contact/" + partyContactId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
    }


}