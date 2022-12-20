package com.cif.stories.cifservice;

import com.cif.cifservice.api.UpdateAddressCmd;
import com.cif.cifservice.api.UpdateFatcaDetailsCmd;
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

public class UpdateFatcaSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();


    UpdateFatcaDetailsCmd updateFatcaDetailsCmd;
    String partyIdentifier;
    Long partyFatcaDetailsId;

    public UpdateFatcaSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User will send a valid value request")
    public void setupMockDataForUpdatePartyFatca() throws IOException {
        this.partyIdentifier = "12345678912";
        this.partyFatcaDetailsId = 1l;
        this.updateFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateFatca.json"), UpdateFatcaDetailsCmd.class);

    }

    @When("Validation process started by making call by PUT method to party Fatca")
    public void makeApiCallForUpdatePartyFatca() {
        serviceCall();
    }

    @Then("Party Fatca Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdatePartyFatca() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User would send an invalid value request")
    public void setupMockDataForUpdatePartyFatcaValidation() {
        this.partyIdentifier = "1234567891245";
    }

    @When("Validate the request parameters by making PUT call to party Fatca")
    public void makeApiCallForUpdatePartyFatcaValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of party Fatca Details exist")
    public void verifyStatusForUpdatePartyFatcaValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/fatca/" + partyFatcaDetailsId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(updateFatcaDetailsCmd));
    }
}
