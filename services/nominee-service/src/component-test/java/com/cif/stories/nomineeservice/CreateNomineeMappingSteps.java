package com.cif.stories.nomineeservice;

import com.cif.nomineeservice.api.CreateNomineeRequest;
import com.cif.nomineeservice.api.NomineeMappingRequest;
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

public class CreateNomineeMappingSteps extends Steps {
    private final Client client;
    private final int localPort;
    NomineeMappingRequest nomineeMappingRequest;
    private Response response;

    public CreateNomineeMappingSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }
    @Given("User sends the valid data in the request")
    public void setupMockDataForCreateNomineeMapping() {
        this.nomineeMappingRequest = NomineeMappingRequest.builder().partyId(1).nomineeId(1).accountNumber("1").build();
    }
    @When("User started the create nominee mapping process by making POST call to nominee mapping api")
    public void makeApiCallForCreateNomineeMapping() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/mapping").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(nomineeMappingRequest));
    }
    @Then("Nominee mapping created successfully and returns 201 status code")
    public void verifyStatusAndResponseDataNomineeMapping() {
        assert response.getStatus() == HttpStatus.CREATED_201;
        assert response.readEntity(SuccessResponseApi.class).getData() != null;
    }
    @Given("User sends the invalid data in the request")
    public void setupMockInvalidDataForCreateNomineeMapping() {
        this.nomineeMappingRequest = NomineeMappingRequest.builder().build();
    }
    @When("User started the create nominee mapping process by making POST api call")
    public void makeApiCallForCreateNomineeMappingForException() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/mapping").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(nomineeMappingRequest));
    }
    @Then("Status code 400 will return")
    public void verifyStatusForNomineeMapping() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }
}
