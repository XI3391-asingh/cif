package com.finx.stories.customerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.customerservice.api.PartyCmd;
import com.finx.customerservice.api.PartyRequestCmd;
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

public class CreatePartySteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();
    private PartyRequestCmd partyRequestCmd;

    public CreatePartySteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User send request parameters with blank or invalid values")
    public void setupMockDataForPartyValidation() throws IOException {
        this.partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class)
                .party(PartyCmd.builder().primaryMobileNumber("").build());
    }

    @When("Validate the request parameters by making POST call to /party")
    public void makeApiCallForPartyValidation() {
        serviceCall();
    }

    @Then("Service returns 422 status code input validation failed")
    public void verifyStatusForPartyValidation() {
        assert response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422;
    }

    @Given("User send the valid data in request")
    public void setupMockDataForCreateParty() throws IOException {
        this.partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
    }

    @When("I saving party record in the database by making POST call to /party")
    public void makeApiCallForCreateParty() {
        serviceCall();
    }

    @Then("Party Created Successfully and returns 202 status code")
    public void verifyStatusForCreateParty() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(partyRequestCmd));
    }

}
