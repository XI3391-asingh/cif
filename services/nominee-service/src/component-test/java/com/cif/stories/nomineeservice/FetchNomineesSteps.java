package com.cif.stories.nomineeservice;

import com.cif.nomineeservice.api.FetchNomineeRequest;
import com.cif.nomineeservice.api.SuccessResponseApi;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class FetchNomineesSteps extends Steps {

    private final Client client;
    private final int localPort;
    FetchNomineeRequest nomineeRequest;
    private Response response;

    public FetchNomineesSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid nominee id in the request")
    public void setupMockDataForFetchNominees() {
        this.nomineeRequest = FetchNomineeRequest.builder().nomineeId(1).build();
    }

    @When("User started the fetch nominee process by making POST call to nominee fetch api")
    public void makeApiCallForFetchNominees() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/fetch").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(nomineeRequest));
    }

    @Then("Nominee fetch successfully and returns 200 status code with response data")
    public void verifyStatusAndResponseData() {
        assert response.getStatus() == HttpStatus.OK_200;
        assert response.readEntity(SuccessResponseApi.class).getData() != null;
    }
    @Given("User sends the invalid nominee id in the request")
    public void setupMockDataForFetchNomineeApi() {
        this.nomineeRequest = FetchNomineeRequest.builder().nomineeId(0).build();
    }
    @When("User started the fetch nominee process by making api call to nominee fetch api")
    public void makeApiCallForFetchNomineeApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/fetch").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(nomineeRequest));
    }
    @Then("Nominee fetch successfully and returns 204 status code with response data")
    public void verifyStatusAndResponseDataForFetchNomineeApi() {
        assert response.getStatus() == HttpStatus.NO_CONTENT_204;
    }
}
