package com.cif.stories.cifservice;

import com.cif.cifservice.api.DedupeFieldRequestCmd;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DedupeVerificationSteps extends Steps{
    private final Client client;
    private final  int localPort;
    private Response response;


    DedupeFieldRequestCmd dedupeFieldRequestCmd ;

    public DedupeVerificationSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }
    @Given("User sends the valid requested data in the request parameter")
    public void setupMockDataForDedupeVerification() {
        this.dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().countryCode("1").mobileNumber("9990601996").build();
    }
    @When("User started the dedupe process by making POST call to dedupe")
    public void makeApiCallForDedupeVerification(){
        serviceCall();
    }
    @Then("200 status code return with total dedupe count")
    public void verifyStatusForDedupeVerification(){
        assert response.getStatus() == HttpStatus.OK_200;
    }
    @Given("User sends the required value")
    public void setupMockDataForDedupeCheck(){
        this.dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().countryCode(null).build();
    }
    @When("Dedupe process started by making POST call to dedupe by the user")
    public void makeApiCallForDedupeCheck(){
        serviceCall();
    }
    @Then("Status code 400 return with the error message")
    public void verifyStatusForDedupeVerifcation(){
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/dedupe/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(Entity.json(this.dedupeFieldRequestCmd));
    }

}