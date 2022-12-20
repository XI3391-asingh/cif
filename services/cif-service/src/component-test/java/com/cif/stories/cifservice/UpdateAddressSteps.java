package com.cif.stories.cifservice;

import com.cif.cifservice.api.UpdateAddressCmd;
import com.fasterxml.jackson.databind.ObjectMapper;
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


public class UpdateAddressSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();

    UpdateAddressCmd updateAddressCmd;

    String partyIdentifier;
    Long partyAddressId;

    public UpdateAddressSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid data is send by the user")
    public void setupMockDataForUpdatePartyAddress() throws IOException {
        partyIdentifier = "12345678912";
        this.partyAddressId = 1l;
        this.updateAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateAddress.json"), UpdateAddressCmd.class);
    }

    @When("Validation process started by making PUT call to party address")
    public void makeApiCallForUpdatePartyAddress() {
        serviceCall();
    }

    @Then("Party Address Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdatePartyAddress() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid data send by user in request parameter")
    public void setupMockDataForUpdatePartyAddressValidation() {
        this.partyAddressId = 0l;
    }

    @When("Validate the request parameters by making PUT call to party address")
    public void makeApiCallForUpdatePartyAddressValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of party address exist")
    public void verifyStatusForUpdatePartyAddressValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/address/" + partyAddressId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(this.updateAddressCmd));
    }
}
