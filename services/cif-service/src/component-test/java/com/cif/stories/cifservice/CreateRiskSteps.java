package com.cif.stories.cifservice;

import com.cif.cifservice.api.*;
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
import java.util.Collections;
import java.util.Set;

public class CreateRiskSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();

    String partyIdentifier;

    private Set<CreateRisksCmd> createRisksCmds;
    private CreateRisksCmd risksCmd = CreateRisksCmd.builder().build();

    public CreateRiskSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid party risks data in the request")
    public void setupMockDataForCreatePartyRisks() throws IOException {
        partyIdentifier = "12345678912";
        this.risksCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateRisk.json"), CreateRisksCmd.class);
        this.createRisksCmds = Collections.singleton(risksCmd);
    }

    @When("User started the create party risks process by making POST call to risks")
    public void makeApiCallForCreatePartyRisks() {
        serviceCall();
    }

    @Then("Party Risks Created Successfully and returns 201 status code")
    public void verifyStatusForCreatePartyRisks() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User send invalid party risks data in request")
    public void setupMockDataForCreatePartyRisksValidation() {
        partyIdentifier = null;
        this.createRisksCmds = Collections.singleton(risksCmd);
    }

    @When("Validate the request parameters by making POST call to risks")
    public void makeApiCallForCreatePartyRisksValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code and Party Risks Created Failed")
    public void verifyStatusForCreatePartyRisksValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400 || response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/risks")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(this.createRisksCmds));
    }


}
