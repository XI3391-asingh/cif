package com.cif.stories.cifservice;

import com.cif.cifservice.api.PartyEmailUpdateCmd;
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


public class UpdatePartyEmailSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private PartyEmailUpdateCmd partyEmailUpdateCmd;

    public UpdatePartyEmailSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends valid data in request")
    public void setupMockDataForUpdatePartyEmail() {
        this.partyEmailUpdateCmd = PartyEmailUpdateCmd.builder().partyIdentifier("12345678912").primaryEmail("test@gmal.com").build();

    }

    @When("User validation process started by making PUT call to update emailId")
    public void makeApiCallForUpdatePartyEmail() {
        serviceCall();
    }

    @Then("Party EmailId Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdatePartyEmail() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User sends request parameters with blank or invalid values")
    public void setupMockDataForUpdatePartyEmailValidation() {
        this.partyEmailUpdateCmd = PartyEmailUpdateCmd.builder().partyIdentifier("123456789012").primaryEmail(null).build();

    }

    @When("Validate the request parameters by making PUT call to update emailId")
    public void makeApiCallForUpdatePartyEmailValidation() {
        serviceCall();
    }

    @Then("Service returns Party EmailId Update Failed for partyId and return status code 400")
    public void verifyStatusForUpdatePartyEmailValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/update/email")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(this.partyEmailUpdateCmd));
    }
}
