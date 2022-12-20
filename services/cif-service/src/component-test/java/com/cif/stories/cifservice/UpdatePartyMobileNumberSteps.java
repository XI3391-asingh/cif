package com.cif.stories.cifservice;

import com.cif.cifservice.api.PartyMobileUpdateCmd;
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

public class UpdatePartyMobileNumberSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private PartyMobileUpdateCmd partyContactUpdateCmd;

    public UpdatePartyMobileNumberSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User send valid value in request")
    public void setupMockDataForUpdatePartyMobileNumber() {
        this.partyContactUpdateCmd = PartyMobileUpdateCmd.builder().partyIdentifier("12345678912").primaryMobileNumber("9990601721").build();
    }

    @When("User validation process started by making update mobile number api call")
    public void makeApiCallForUpdatePartyMobileNumber() {
        serviceCall();
    }

    @Then("Party Mobile Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdatePartyMobileNumber() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User send request with invalid mobile number")
    public void setupMockDataForUpdatePartyMobileNumberValidation() {
        this.partyContactUpdateCmd = PartyMobileUpdateCmd.builder().partyIdentifier("12345678912").primaryMobileNumber(null).build();
    }

    @When("Validate the request parameters by making update mobile number api call")
    public void makeApiCallForUpdatePartyMobileNumberValidation() {
        serviceCall();
    }

    @Then("Service returns Party Mobile Update Failed for partyId and return status code 400")
    public void verifyStatusForUpdatePartyMobileNumberValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/update/mobilenumber")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(partyContactUpdateCmd));
    }


}
