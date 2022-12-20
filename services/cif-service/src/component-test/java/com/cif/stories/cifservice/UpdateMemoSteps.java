package com.cif.stories.cifservice;

import com.cif.cifservice.api.PartyMemoCmd;
import com.cif.cifservice.api.PartyRequestCmd;
import com.cif.cifservice.api.UpdateMemosCmd;
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

public class UpdateMemoSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();

    UpdateMemosCmd updateMemosCmd;
    String partyIdentifier;
    Long partyMemoId;

    public UpdateMemoSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid value are send by the user in request parameter")
    public void setupMockDataForUpdatePartyMemos() throws IOException {
        this.partyIdentifier = "12345678912";
        this.partyMemoId = 1l;
        this.updateMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateMemo.json"), UpdateMemosCmd.class);

    }

    @When("Validation process started by making PUT call to party memos")
    public void makeApiCallForUpdatePartyMemos() {
        serviceCall();
    }

    @Then("Party Memos Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdatePartyMemos() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid value are send by the user in request parameter")
    public void setupMockDataForUpdatePartyMemosValidation() {
        this.partyIdentifier = "1234567891267";
    }

    @When("Validate the request parameters by making PUT call to party memos")
    public void makeApiCallForUpdatePartyMemosValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of party memos exist")
    public void verifyStatusForUpdatePartyMemosValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/memos/" + partyMemoId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(this.updateMemosCmd));
    }
}
