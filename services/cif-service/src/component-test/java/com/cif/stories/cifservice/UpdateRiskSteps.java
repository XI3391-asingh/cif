package com.cif.stories.cifservice;

import com.cif.cifservice.api.UpdateRisksCmd;
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

public class UpdateRiskSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();

    UpdateRisksCmd updateRisksCmd;
    String partyIdentifier;
    Long partyRiskId;

    public UpdateRiskSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid risk data is send by the user")
    public void setupMockDataForUpdatePartyRisk() throws IOException {
        this.partyIdentifier = "12345678912";
        this.partyRiskId = 1l;
        this.updateRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateRisk.json"), UpdateRisksCmd.class);

    }

    @When("Validation process started by making PUT call to party risk")
    public void makeApiCallForUpdatePartyRisk() {
        serviceCall();
    }

    @Then("Party risk Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdatePartyRisk() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid risk data send by user in request parameter")
    public void setupMockDataForUpdatePartyRiskValidation() {
        this.partyIdentifier = "1234567";
    }

    @When("Validate the request parameters by making PUT call to party risk")
    public void makeApiCallForUpdatePartyRiskValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of party risk exist")
    public void verifyStatusForUpdatePartyRiskValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/risks/" + partyRiskId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(this.updateRisksCmd));
    }
}
