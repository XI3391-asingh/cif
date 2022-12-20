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

public class CreateContactSteps extends Steps {

    private final Client client;
    private final int localPort;
    private Response response;
    private final ObjectMapper MAPPER = new ObjectMapper();

    private Set<CreateContactDetailsCmd> createContactDetailsCmd;
    private CreateContactDetailsCmd contactDetailsCmd = CreateContactDetailsCmd.builder().build();

    String partyIdentifier;

    public CreateContactSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid contact data in the request")
    public void setupMockDataForCreateContact() throws IOException {
        partyIdentifier = "12345678912";
        this.contactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);
        this.createContactDetailsCmd = Collections.singleton(contactDetailsCmd);
    }

    @When("User started the create contact process by making POST call to contact")
    public void makeApiCallForCreateContact() {
        serviceCall();
    }

    @Then("Contact Created Successfully and returns 201 status code")
    public void verifyStatusForCreateContact() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User send invalid contact data in request")
    public void setupMockDataForCreateContactValidation() {
        partyIdentifier = "123456";
        contactDetailsCmd.contactValue(null);
        this.createContactDetailsCmd = Collections.singleton(contactDetailsCmd);
    }

    @When("Validate the request parameters by making POST call to contact")
    public void makeApiCallForCreateContactValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code and Contact Created Failed")
    public void verifyStatusForCreateContactValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400 || response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422);
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + this.partyIdentifier + "/contacts")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(this.createContactDetailsCmd));
    }





}