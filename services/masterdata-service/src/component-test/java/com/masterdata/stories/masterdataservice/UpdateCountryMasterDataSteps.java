package com.masterdata.stories.masterdataservice;

import com.masterdata.masterdataservice.api.UpdateCountryMasterCmd;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UpdateCountryMasterDataSteps extends Steps {

    private final Client client;
    private final int localPort;

    Long countryMasterDataId;

    UpdateCountryMasterCmd updateCountryMasterCmd;
    private Response response;

    public UpdateCountryMasterDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid country master data in the request")
    public void setupMockDataForUpdateCountryMasterData() {
        this.countryMasterDataId = 1L;
        this.updateCountryMasterCmd = UpdateCountryMasterCmd.builder().code("01").isdCode("+84").build();
    }

    @When("User started the update country master data process by making PUT call to country master data api")
    public void makeApiCallForUpdateCountryMasterData() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster/update/" + countryMasterDataId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(this.updateCountryMasterCmd));
    }

    @Then("Country master data updated successfully and returns 200 status code")
    public void verifyStatusAndResponseDataForCountryMasterData() {
        assert response.getStatus() == HttpStatus.OK_200;

    }

    @Given("User sends the invalid country master data id in the request")
    public void setupMockInvalidDataForMasterDataApi() {
        this.countryMasterDataId = 0L;
        this.updateCountryMasterCmd = UpdateCountryMasterCmd.builder().build();
    }

    @When("User started the update process by making PUT call to country master data api")
    public void makeApiCallForCountryMasterDataApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster/update/" + countryMasterDataId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(updateCountryMasterCmd));
    }

    @Then("Country master data updated failed and returns 400 status code")
    public void verifyStatus() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }
}
