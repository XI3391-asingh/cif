package com.cif.stories.cifservice;

import com.cif.cifservice.api.PartyResponseCmd;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;


public class FetchPartyByCifIdSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;
    private String partyIdentifiers;

    public FetchPartyByCifIdSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User with cif id which not exist")
    public void setupMockCifIdForPartyFetch() {
        this.partyIdentifiers = "1234567891245";
    }

    @When("User fetch all party record by making GET call to /party/")
    public void makeApiCallForPartyFetch() {
        serviceCall();
    }

    @Then("Fetch service should return 404 with no record")
    public void verifyResponseForPartyFetchApi() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    @Given("User with cif id which exist")
    public void setupValidMockCifIdForPartyFetch() {
        this.partyIdentifiers = "12345678912";
    }

    @When("User fetch all party information by making GET call to /party/")
    public void makeApiCallForPartyFetchInformation() {
        serviceCall();
    }

    @Then("Fetch service should return 200 with record")
    public void verifyResponseForPartyRecordFetchApi() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party")
                .queryParam("id", partyIdentifiers)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
