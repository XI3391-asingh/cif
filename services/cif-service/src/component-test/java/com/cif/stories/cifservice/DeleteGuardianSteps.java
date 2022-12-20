package com.cif.stories.cifservice;

import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DeleteGuardianSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    String partyIdentifier;
    Long partyGuardianId;

    public DeleteGuardianSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User has sent valid guardian value in request")
    public void setupMockDataForDeletePartyGuardian() {
        partyIdentifier = "12345678912";
        partyGuardianId = 1l;
    }

    @When("Validation process by making DELETE call to party guardian")
    public void makeApiCallForDeletePartyGuardian() {
        serviceCall();
    }

    @Then("Party guardian Delete Success for partyId and returns 200 status code")
    public void verifyStatusForDeletePartyGuardian() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User has sent invalid guardian value in request")
    public void setupMockDataForDeletePartyGuardianValidation() {
        partyGuardianId = 0l;
    }

    @When("Validate the request parameters by making DELETE call to party guardian")
    public void makeApiCallForDeletePartyGuardianValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of party guardian exist")
    public void verifyStatusForDeletePartyGuardianValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/guardian/" + partyGuardianId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .delete();
    }

}
