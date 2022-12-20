package com.cif.stories.cifservice;

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

public class CreateAdminDataSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;

    private final ObjectMapper MAPPER = new ObjectMapper();

    private Config configData;

    public CreateAdminDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid admin data in request")
    public void setupMockDataForCreateAdminAPI() throws IOException {
        this.configData = MAPPER.readValue(getClass().getResource("/fixtures/CreateAdminRatelimiter.json"), Config.class);
    }

    @When("User started the create admin process by making POST call to admin API")
    public void makeApiCallForCreateAdminAPI() {
        serviceCall();
    }

    @Then("Admin Data Created Successfully and returns 201 status code")
    public void verifyStatusForCreateAdminAPI() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User sends invalid admin data request")
    public void setupMockDataForCreateAdminValidation() throws IOException {
        this.configData = null;

    }

    @When("Validate the request parameters by making POST call to admin API")
    public void makeApiCallForCreateAdminValidation() {
        serviceCall();
    }

    @Then("Service returns 400 status code and Failed to create admin data")
    public void verifyStatusForCreateAdminValidation() {
        assert (response.getStatus() == HttpStatus.BAD_REQUEST_400);
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/admin/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(this.configData));
    }

}
