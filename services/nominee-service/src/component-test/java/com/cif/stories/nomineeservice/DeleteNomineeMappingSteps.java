package com.cif.stories.nomineeservice;


import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class DeleteNomineeMappingSteps extends Steps {

    private final Client client;
    private final int localPort;
    Long nomineeMappingId;
    private Response response;

    public DeleteNomineeMappingSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }
    @Given("User send the valid nominee id in the  parameter")
    public void setupMockDataForDeleteNomineeMapping() {
        this.nomineeMappingId = 1l;
    }

    @When("User started the delete nominee mapping process by making DELETE call to nominee mapping api")
    public void makeApiCallForDeleteNomineeMapping() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/nominee/mapping/1").request(MediaType.APPLICATION_JSON_TYPE).delete();
    }
    @Then("Nominee mapping deleted successfully and returns 200 status code")
    public void verifyStatusAndResponseDataForNomineeMapping() {
        assert response.getStatus() == HttpStatus.OK_200;
    }
}
