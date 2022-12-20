package com.cif.stories.cifservice;


import com.cif.cifservice.api.CreateFatcaDetailsCmd;
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

public class CreateFatcaSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();

    public CreateFatcaSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    private Set<CreateFatcaDetailsCmd> createFatcaDetailsCmds;
    private CreateFatcaDetailsCmd fatcaDetailsCmd = CreateFatcaDetailsCmd.builder().build();
    String partyIdentifier;

    @Given("User sends the valid data in request")
    public void setupMockDataForCreatePartyFatca() throws IOException {
        partyIdentifier = "12345678912";
        this.fatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateFatca.json"), CreateFatcaDetailsCmd.class);
        this.createFatcaDetailsCmds = Collections.singleton(fatcaDetailsCmd);
    }

    @When("User started the create party fatca process by making POST call to Party Fatca")
    public void makeApiCallCreatePartyFatca() {
        serviceCall();
    }

    @Then("Party Fatca Created Successfully and returns 201 status code")
    public void verifyStatusForCreatePartyFatca() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User sends invalid data request")
    public void setupMockDataForCreatePartyFatcaValidation() {
        partyIdentifier = null;
        fatcaDetailsCmd.setFatcaTaxId(null);
        this.createFatcaDetailsCmds = Collections.singleton(fatcaDetailsCmd);
    }

    @When("Validate the request parameters by making POST call to Party Fatca")
    public void makeApiCallForCreatePartyFatcaValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code and Failed to create Party Fatca")
    public void verifyStatusForCreatePartyFatcaValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400 || response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + this.partyIdentifier + "/fatca")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(this.createFatcaDetailsCmds));
    }

}
