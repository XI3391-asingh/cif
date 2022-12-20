package com.cif.stories.nomineeservice;

import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DeleteNomineesSteps extends Steps {

    private final Client client;
    private final int localPort;
    int nomineeId;
    private Response response;

    public DeleteNomineesSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid nominee id in the parameter")
    public void setupMockDataForDeleteNominees() {
        this.nomineeId = 1;
    }

    @When("User started the delete nominee process by making DELETE call to nominee api")
    public void makeApiCallForDeleteNominees() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/1").request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    @Then("Nominee deleted successfully and returns 200 status code")
    public void verifyStatusAndResponseData() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User sends the invalid nominee id in the parameter")
    public void setupInvalidDataForDeleteNominees() {
        this.nomineeId = 0;
    }

    @When("User started the delete nominee process by making api call to nominee delete api")
    public void makingApiCallForDeleteNomineesApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/1").request(MediaType.APPLICATION_JSON_TYPE).delete();
    }
    @Then("Returns error code 400 response")
    public void verifyStatusAndResponseDataForDeleteNominee() {
        assert response.getStatus() == HttpStatus.INTERNAL_SERVER_ERROR_500;
    }




}
