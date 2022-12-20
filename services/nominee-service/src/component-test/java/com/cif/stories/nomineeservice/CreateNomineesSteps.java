package com.cif.stories.nomineeservice;

import com.cif.nomineeservice.api.CreateNomineeRequest;
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

public class CreateNomineesSteps extends Steps {

    private final Client client;
    private final int localPort;
    CreateNomineeRequest nomineeRequest;
    private Response response;

    public CreateNomineesSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid nominee data in the request")
    public void setupMockDataForCreateNominees() {
        this.nomineeRequest = CreateNomineeRequest.builder().partyId(1).firstName("Test").lastName("Test").nationalId("01").relation(CreateNomineeRequest.RelationEnum.SPOUSE).salutation(CreateNomineeRequest.SalutationEnum.MR_).build();
    }

    @When("User started the create nominee process by making POST call to nominee api")
    public void makeApiCallForCreateNominees() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(nomineeRequest));
    }

    @Then("Nominee created successfully and returns 201 status code")
    public void verifyStatusAndResponseData() {
        assert response.getStatus() == HttpStatus.CREATED_201;
        assert response.readEntity(SuccessResponseApi.class).getData() != null;
    }

    @Given("User sends the invalid nominee data in the request")
    public void setupMockInvalidDataForCreateNominees() {
        this.nomineeRequest = CreateNomineeRequest.builder().build();
    }

    @When("User started the create nominee process by making POST api call")
    public void makeApiCallForCreateNomineesForException() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(nomineeRequest));
    }

    @Then("API will return error")
    public void verifyStatus() {
        assert response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422;
    }
}
