package com.cif.stories.cifservice;

import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ViewAllXrefSteps {
    private final Client client;
    private final int localPort;

    private Response response;

    String partyIdentifier;

    public ViewAllXrefSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }
    @Given("User sends the valid party identifier")
    public void setupMockDataForViewAllPartyXref() {
        this.partyIdentifier = "12345678912";
    }
    @When("Validation process started by making GET call to xref")
    public void makeFetchAllPartyXrefApiCallBasedOnExistingPartyId() {
        serviceCall();
    }

    @Then("If party xref exist for party identifier api will return 200 with response")
    public void verifyStatusForViewAllPartyXref() {
        assert response.getStatus() == HttpStatus.OK_200;

    }
    @Given("User sends the invalid party identifier")
    public void setupMockDataForViewAllPartyXrefValidation() {
        this.partyIdentifier = null;
    }

    @When("Validate the request parameters by making GET call to xref")
    public void makeFetchAllPartyXrefApiCallBasedOnNonExistingPartyId() {
        serviceCall();
    }

    @Then("Exception will be thrown and a exception message will be return")
    public void verifyNoDataWhenAllPartyXrefIsPresent() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;

    }
    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/xref/")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}