package com.cif.stories.cifservice;

import com.cif.cifservice.api.CreateAddressCmd;
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

public class CreateAddressSteps extends Steps {

    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();

    private Set<CreateAddressCmd> createAddressCmds;
    private CreateAddressCmd addressCmd = CreateAddressCmd.builder().build();

    String partyIdentifier;

    public CreateAddressSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid value in the request")
    public void setupMockDataForCreatePartyAddress() throws IOException {
        partyIdentifier = "12345678912";
        this.addressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), CreateAddressCmd.class);
        this.createAddressCmds = Collections.singleton(addressCmd);
    }

    @When("User started the create party address process by making POST call to party address")
    public void makeApiCallForCreatePartyAddress() {
        serviceCall();
    }

    @Then("Party Address Created Successfully and returns 201 status code")
    public void verifyStatusForCreatePartyAddress() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User send invalid data in request")
    public void setupMockDataForCreatePartyAddressValidation() {
        addressCmd.setAddressLine1(null);
        this.createAddressCmds = Collections.singleton(addressCmd);
    }

    @When("Validate the request parameters by making POST call to party address")
    public void makeApiCallForCreatePartyAddressValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code and Party Address Created Failed")
    public void verifyStatusForCreatePartyAddressValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400 || response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + this.partyIdentifier + "/address")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(this.createAddressCmds));
    }
}
