package com.cif.stories.cifservice;

import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ViewAdminDataSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;


    String type;

    public ViewAdminDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid type")
    public void setupMockDataForViewAdminAPI() {
        this.type = "RATELIMITER";

    }

    @When("Validation process started by making GET call to admin")
    public void makeApiCallForViewAdminAPI() {
        serviceCall();
    }

    @Then("If admin data exist for the type provided then api will return 200 with response")
    public void verifyStatusForViewAdminAPI() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User sends the invalid type")
    public void setupMockDataForViewAdminValidation() {
        this.type = "TEST";

    }

    @When("Validate the request parameters by making GET call to admin")
    public void makeApiCallForViewAdminValidation() {
        serviceCall();
    }

    @Then("Error message will be shown with status code 500")
    public void verifyStatusForViewAdminValidation() {
        assert response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR_500;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/admin/" + this.type)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
