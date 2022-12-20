package com.cif.stories.cifservice;



import com.cif.cifservice.api.UpdatePartyRequestCmd;
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


public class UpdatePartySteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();
    private UpdatePartyRequestCmd partyRequest;


    public UpdatePartySteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User send valid data in request")
    public void setupMockDataForUpdateParty() throws IOException {
        this.partyRequest = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
    }

    @When("User started the update party process by making PUT call to party/update")
    public void makeApiCallForUpdateParty() {
        serviceCall();
    }

    @Then("Party Update Success for partyIdentifier and returns 200 status code")
    public void verifyStatusForUpdateParty() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User send request parameters with invalid or null values")
    public void setupMockDataForPartyValidation() throws IOException {
        this.partyRequest = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        this.partyRequest.getParty().setPartyIdentifier(null);
    }

    @When("Validate the request parameters by making PUT call to party/update")
    public void makeApiCallForPartyValidation() {
        serviceCall();
    }

    @Then("Service returns Party Update Failed for partyId and return status code 400")
    public void verifyStatusForPartyValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }


    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/update/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(partyRequest));
    }

}