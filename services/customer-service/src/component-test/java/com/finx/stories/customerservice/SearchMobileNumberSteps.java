package com.finx.stories.customerservice;

import com.finx.customerservice.api.PartySearchCmd;
import com.finx.customerservice.resources.exceptions.PartyException;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class SearchMobileNumberSteps extends Steps {

    private final Client client;
    private final int localPort;
    private PartySearchCmd partySearchCmd;
    private Response response;

    public SearchMobileNumberSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("First time user with valid mobile number")
    public void getValidRequestData() {
        this.partySearchCmd = PartySearchCmd.builder().mobileNumber("1111111111").build();
    }

    @When("I start the validation process by making POST call to /party/searchbymobile")
    public void makeApiCall() {
        Entity<PartySearchCmd> searchCmdEntity = Entity.json(partySearchCmd);
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/searchbymobile")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(searchCmdEntity);
    }

    @Then("Service should return 200 with no record")
    public void validateMobileNumber() {
        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            throw new PartyException("Mobile Number Validation Failed", null);
        }
    }

}