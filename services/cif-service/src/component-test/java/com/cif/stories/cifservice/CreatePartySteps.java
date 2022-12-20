package com.cif.stories.cifservice;

import com.cif.cifservice.api.CreatePartyCmd;
import com.cif.cifservice.api.PartyCmd;
import com.cif.cifservice.api.PartyRequestCmd;
import com.cif.cifservice.core.party.domain.Config;
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
                .party(CreatePartyCmd.builder().primaryMobileNumber("").build());
    }

    @When("Validate the request parameters by making POST call to /party")
    public void makeApiCallForPartyValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code input validation failed")
    public void verifyStatusForPartyValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400);
    }

    @Given("User send the valid data in request")
    public void setupMockDataForCreateParty() throws IOException {
        this.partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        this.partyRequestCmd.getParty().setPartyIdentifier("12345678912");
    }

    @When("I saving party record in the database by making POST call to /party")
    public void makeApiCallForCreateParty() throws IOException {
        createAdminData();
        serviceCall();
    }

    @Then("Party Created Successfully and returns 201 status code")
    public void verifyStatusForCreateParty() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(partyRequestCmd));
    }

    private void createAdminData() throws IOException {
        Config configurationData = MAPPER.readValue(getClass().getResource("/fixtures/CreateAdminEncryption.json"), Config.class);
        this.client.target("http://localhost:" + this.localPort + "/admin/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(configurationData));
    }

}
