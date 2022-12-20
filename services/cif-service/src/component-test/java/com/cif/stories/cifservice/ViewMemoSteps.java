package com.cif.stories.cifservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class ViewMemoSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;

    String partyIdentifier;
    Long partyMemoId;

    public ViewMemoSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid values are send in the request parameter")
    public void setupMockDataForViewPartyMemos() {
        partyIdentifier = "12345678912";
        partyMemoId = 1l;

    }

    @When("Validation process started for getting the memos by making GET call to party memos")
    public void makeApiCallForViewPartyMemos() {
        serviceCall();
    }

    @Then("Success message and 200 status code return")
    public void verifyStatusForViewPartyMemos() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User did not send the required values in request parameter")
    public void setupMockDataForViewPartyMemosValidation() {
        this.partyIdentifier = "12345678912876";
    }

    @When("Validate the request parameters for getting the memos by making GET call to party memos")
    public void makeApiCallForViewPartyMemosValidation() {
        serviceCall();
    }

    @Then("If no party is present with that party id then no data return")
    public void verifyStatusForViewPartyMemosValidation() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/memos/" + this.partyMemoId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
