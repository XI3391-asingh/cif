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

public class CreateMemoSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();


    public CreateMemoSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    private Set<CreateMemosCmd> createMemosCmds;
    private CreateMemosCmd memosCmd = CreateMemosCmd.builder().build();

    String partyIdentifier;

    @Given("User sends the required value in request")
    public void setupMockDataForCreatePartyMemos() throws IOException {
        partyIdentifier = "12345678912";
        this.memosCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateMemo.json"), CreateMemosCmd.class);
        this.createMemosCmds = Collections.singleton(memosCmd);

    }

    @When("User started the create party memos process by making POST call to party memos")
    public void makeApiCallForCreatePartyMemos() {
        serviceCall();
    }

    @Then("Party memos Created Successfully and returns 201 status code")
    public void verifyStatusForCreatePartyMemos() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User did not send the required value in request")
    public void setupMockDataForCreatePartyMemosValidation() {
        partyIdentifier = null;
        memosCmd.setMemoTypeCode(null);
        this.createMemosCmds = Collections.singleton(memosCmd);
    }

    @When("Validate the request parameters by making POST call to party memos")
    public void makeApiCallForCreatePartyMemosValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code and Failed to create Party Memos")
    public void verifyStatusForCreatePartyMemosValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400 || response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + this.partyIdentifier + "/memos")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(createMemosCmds));
    }

}
