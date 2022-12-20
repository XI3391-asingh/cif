package com.cif.stories.cifservice;


import com.cif.cifservice.api.UpdateContactDetailsCmd;
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

public class UpdateContactSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();

    UpdateContactDetailsCmd updateContactDetailsCmd;

    String partyIdentifier;
    Long partyContactDetailsId;

    public UpdateContactSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid contact data is send by the user for updation")
    public void setupMockDataForUpdateContact() throws IOException {
        partyIdentifier = "12345678912";
        this.partyContactDetailsId = 1l;
        this.updateContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);
    }

    @When("Validation process started by making PUT call to contacts")
    public void makeApiCallForUpdateContact() {
        serviceCall();
    }

    @Then("Contacts Update Success for partyId and returns 200 status code")
    public void verifyStatusForUpdateContact() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid contact data send by user in request parameter")
    public void setupMockDataForUpdateContactValidation() {
        this.partyContactDetailsId = 0l;
    }

    @When("Validate the request parameters by making PUT call to contact")
    public void makeApiCallForUpdateContactValidation() {
        serviceCall();
    }

    @Then("Service returns status code 400 when no record of contact exist")
    public void verifyStatusForUpdateContactValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/contacts/" + partyContactDetailsId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(this.updateContactDetailsCmd));
    }
}
