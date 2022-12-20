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

public class UpdateNomineesSteps extends Steps {

    private final Client client;
    private final int localPort;
    CreateNomineeRequest nomineeRequest;
    private Response response;

    public UpdateNomineesSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid nominee data in the request")
    public void setupMockDataForCreateNominees() {
        this.nomineeRequest = CreateNomineeRequest.builder().partyId(1).firstName("Test").lastName("Test").nationalId("01").relation(CreateNomineeRequest.RelationEnum.SPOUSE).salutation(CreateNomineeRequest.SalutationEnum.DR_).build();
    }

    @When("User started the update nominee process by making PUT call to nominee api")
    public void makeApiCallForCreateNominees() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(nomineeRequest));
    }

    @Then("Nominee updated successfully and returns 200 status code")
    public void verifyStatusAndResponseData() {
        assert response.getStatus() == HttpStatus.OK_200;
        assert response.readEntity(SuccessResponseApi.class).getData() != null;
    }

    @Given("User sends the invalid nominee data in the request")
    public void setupMockInvalidDataForCreateNominees() {
        this.nomineeRequest = CreateNomineeRequest.builder().build();
    }

    @When("User started the update nominee process by making PUT api call")
    public void makeApiCallForCreateNomineesForException() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/").request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(nomineeRequest));
    }

    @Then("API will return error")
    public void verifyStatus() {
        assert response.getStatus() == HttpStatus.UNPROCESSABLE_ENTITY_422;
    }
}
