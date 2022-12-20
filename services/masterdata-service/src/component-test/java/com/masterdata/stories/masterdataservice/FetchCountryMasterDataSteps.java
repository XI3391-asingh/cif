package com.masterdata.stories.masterdataservice;

import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class FetchCountryMasterDataSteps extends Steps {

    private final Client client;
    private final int localPort;

    Long countryMasterDataId;

    private Response response;

    public FetchCountryMasterDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid GET request")
    public void setupMockDataForFetchCountryMasterDataApi() {
    }

    @When("User makes fetch api call for country master data api")
    public void makeApiCallForFetchCountryMasterDataApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster").request(MediaType.APPLICATION_JSON_TYPE).get();
    }

    @Then("Country master data fetch successfully and returns 200 status code with response data")
    public void verifyStatusAndResponseDataForCountryMasterDataApi() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User makes a valid request")
    public void setupMockDataForFetchMasterDataService() {
    }

    @When("Existing country master records to be deleted before fetching")
    public void deleteExistingRecord() {
        this.countryMasterDataId = 1l;
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster/" + countryMasterDataId).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    @When("User start api call by making fetch api call to country master data api")
    public void makeApiCallForFetchMasterDataService() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster").request(MediaType.APPLICATION_JSON_TYPE).get();
    }

    @Then("API returns 204 status code as no record present in the database")
    public void verifyStatusAndResponseDataForCountryMasterDataService() {
        assert response.getStatus() == HttpStatus.NO_CONTENT_204;
    }
}
