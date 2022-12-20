package com.masterdata.stories.masterdataservice;


import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class FetchMasterDataSteps extends Steps {

    private final Client client;
    private final int localPort;
    String masterDataType;
    Long masterDataId;
    private Response response;

    public FetchMasterDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid master type in the request")
    public void setupMockDataForFetchMasterDataApi() {
        this.masterDataType = "LOOKUP_MASTER";
    }

    @When("User makes fetch api call for master data api")
    public void makeApiCallForFetchMasterDataApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/" + masterDataType).request(MediaType.APPLICATION_JSON_TYPE).get();
    }

    @Then("Master data fetch successfully and returns 200 status code with response data")
    public void verifyStatusAndResponseDataForMasterDataApi() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User send the valid master type in the request")
    public void setupMockDataForFetchMasterDataService() {
        this.masterDataType = "LOOKUP_MASTER";
    }

    @When("Existing records to be deleted before fetching")
    public void makeApiCallForDeleteMaterService() {
        this.masterDataId = 1L;
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/" + masterDataId + "/" + masterDataType).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    @When("User start api call by making fetch api call to master data api")
    public void makeApiCallForFetchMasterDataService() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/" + masterDataType).request(MediaType.APPLICATION_JSON_TYPE).get();
    }

    @Then("API returns 204 status code as no record present in database")
    public void verifyStatusAndResponseDataForMasterDataService() {
        assert response.getStatus() == HttpStatus.NO_CONTENT_204;
    }
}
