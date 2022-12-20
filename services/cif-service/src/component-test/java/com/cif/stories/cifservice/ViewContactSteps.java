package com.cif.stories.cifservice;


import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ViewContactSteps extends Steps {
    private final Client client;
    private final int localPort;
    private Response response;


    String partyIdentifier;
    Long partyContactDetailsId;

    public ViewContactSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Valid contact values send by the user for fetch")
    public void setupMockDataForViewContact() {
        this.partyIdentifier = "12345678912";
        partyContactDetailsId = 1l;

    }

    @When("Validation process started for getting the contact by making GET call to contacts")
    public void makeApiCallForViewContact() {
        serviceCall();
    }

    @Then("Contact data and returns 200 status code")
    public void verifyStatusForViewContact() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Invalid contact data is send by user in request parameter for fetch")
    public void setupMockDataForViewContactValidation() {
        this.partyIdentifier = "1234567891234";
    }

    @When("Validate the request parameters for getting the contact by making GET call to contacts")
    public void makeApiCallForViewContactValidation() {
        serviceCall();
    }

    @Then("If party id not present in the database return no contact data found")
    public void verifyStatusForViewContactValidation() {
        assert response.getStatus() == HttpStatus.NOT_FOUND_404;
    }

    private void serviceCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/" + partyIdentifier + "/contacts/" + partyContactDetailsId)
                .request(MediaType.APPLICATION_JSON_TYPE)
                .get();
    }
}
