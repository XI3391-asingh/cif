package com.cif.stories.cifservice;

import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;

public class ViewAllMemoSteps {
    private final Client client;
    private final int localPort;
    private Response response;

    String partyIdentifier;

    public ViewAllMemoSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User send the valid party id in parameter")
    public void setupMockDataForViewAllPartyMemos() {
        this.partyIdentifier = "12345678912";
    }

    @When("Validation process started by making GET call to party memos")
    public void makeFetchMemosApiCallBasedOnExistingPartyId() {
        serviceCall();
    }

    @Then("If memos exist for party id then api will return 200 status code with response")
    public void verifyStatusForViewAllPartyMemos() {
        assert response.getStatus() == HttpStatus.OK_200;

    }

    @Given("User invalid party id in request parameter")
    public void setupMockDataForViewAllPartyMemosValidation() {
        this.partyIdentifier = "1234567891278";
    }

    @When("Validate the request parameters by making GET call to party memos")
    public void makeFetchMemosApiCallBasedOnNonExistingPartyId() {
        serviceCall();
    }

    @Then("If memos for party id is not present then return no data")
    public void verifyNoDataWhenMemosIsNotPresent() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;

    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/memos/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
