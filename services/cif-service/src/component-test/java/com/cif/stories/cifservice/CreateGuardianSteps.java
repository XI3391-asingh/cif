package com.cif.stories.cifservice;

import com.cif.cifservice.api.CreateGuardianCmd;
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

public class CreateGuardianSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();
    private Set<CreateGuardianCmd> createGuardianCmds;
    private CreateGuardianCmd guardianCmd = CreateGuardianCmd.builder().build();
    String partyIdentifier;

    public CreateGuardianSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid guardian data in request")
    public void setupMockDataForCreatePartyGuardian() throws IOException {
        partyIdentifier = "12345678912";
        this.guardianCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateGuardian.json"), CreateGuardianCmd.class);
        this.createGuardianCmds = Collections.singleton(guardianCmd);
    }

    @When("User started the create party guardian process by making POST call to Party guardian")
    public void makeApiCallForCreatePartyGuardian() {
        serviceCall();
    }

    @Then("Party guardian Created Successfully and returns 201 status code")
    public void verifyStatusForCreatePartyGuardian() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User sends invalid guardian data request")
    public void setupMockDataForCreatePartyGuardianValidation() {
        this.partyIdentifier = "12345";
        this.createGuardianCmds = Collections.singleton(guardianCmd);
    }

    @When("Validate the request parameters by making POST call to Party guardian")
    public void makeApiCallForCreatePartyGuardianValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code and Failed to create Party guardian")
    public void verifyStatusForCreatePartyGuardianValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400 || response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + this.partyIdentifier + "/guardian")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(this.createGuardianCmds));
    }
}
