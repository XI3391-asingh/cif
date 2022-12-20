package com.cif.stories.cifservice;

import com.cif.cifservice.api.UpdatePartyRecordForSoftDeleteRequest;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


public class UpdatePartyForSoftDeleteSteps extends Steps {

    private final Client client;
    private final int localPort;
    private Response response;
    UpdatePartyRecordForSoftDeleteRequest softDeleteRequest;

    String partyIdentifier="12345678912";

    public UpdatePartyForSoftDeleteSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("Provide party id for soft delete")
    public void setupMockDataForViewPartyRisk() {
        softDeleteRequest = new UpdatePartyRecordForSoftDeleteRequest();
        softDeleteRequest.setPartyIdentifier(partyIdentifier);
    }

    @When("Call soft-delete api for party record flag update")
    public void makeApiCallForViewPartyRisk() {
        updatePartyRecordForSoftDeleteByPartyIdApiCall();
    }

    @Then("Api will return success response when flag update completed")
    public void verifyStatusForViewPartyRisk() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("Provide party record for soft delete")
    public void setupMockDataForViewPartyRiskValidation() {
        softDeleteRequest.setPartyIdentifier("123456");
    }

    @When("Call /soft-delete api for party record flag update")
    public void makeApiCallForViewPartyRiskValidation() {
        updatePartyRecordForSoftDeleteByPartyIdApiCall();
    }

    @Then("Api will return no content response when record not exist in database")
    public void verifyStatusForViewPartyAddressValidation() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

    private void updatePartyRecordForSoftDeleteByPartyIdApiCall() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/party/soft-delete")
                .request(MediaType.APPLICATION_JSON_TYPE)
                .put(Entity.json(softDeleteRequest));
    }
}
