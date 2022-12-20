package com.cif.stories.cifservice;

import com.cif.cifservice.api.PartyStatusUpdateCmd;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


public class UpdatePartyStatusSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private PartyStatusUpdateCmd partyStatusUpdateCmd;

    public UpdatePartyStatusSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User send valid id value in request")
    public void setupMockDataForUpdatePartyStatus() {
        this.partyStatusUpdateCmd = PartyStatusUpdateCmd.builder().partyIdentifier("12345678912").status(PartyStatusUpdateCmd.StatusEnum.ACTIVE).build();
    }

    @When("Validation process started by making update party status api call")
    public void makeApiCallForUpdatePartyStatus() {
        serviceCall();
    }

    @Then("Party Status Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdatePartyStatus() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User send request parameters with blank or invalid id value")
    public void setupMockDataForUpdatePartyStatusValidation() {
        this.partyStatusUpdateCmd = PartyStatusUpdateCmd.builder().partyIdentifier(null).status(PartyStatusUpdateCmd.StatusEnum.ACTIVE).build();

    }

    @When("Validate the request by making update party status api call")
    public void makeApiCallForUpdatePartyStatusValidation() {
        serviceCall();
    }

    @Then("Service returns Party Status Update Failed for partyId and return status code 400")
    public void verifyStatusForUpdatePartyStatusValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/update/status")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(partyStatusUpdateCmd));
    }

}
