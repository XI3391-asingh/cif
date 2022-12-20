package com.masterdata.stories.masterdataservice;

import com.masterdata.masterdataservice.api.CreateCountryMasterCmd;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;

public class DeleteCountryMasterDataSteps extends Steps {


    private final Client client;
    private final int localPort;

    Long countryMasterDataId;
    CreateCountryMasterCmd createCountryMasterCmd;
    List<CreateCountryMasterCmd> createCountryMasterCmdList;

    private Response response;

    public DeleteCountryMasterDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid country master data id in the parameter")
    public void setupMockDataForDeleteCountryMasterDataApi() {
        countryMasterDataId = 2L;
    }

    @When("Create a valid country master data record")
    public void createCountryMasterRecord() {
        this.createCountryMasterCmd = CreateCountryMasterCmd.builder().code("01").description("Vietnam").isdCode("+84").build();
        this.createCountryMasterCmdList = Collections.singletonList(createCountryMasterCmd);
        this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster/").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(this.createCountryMasterCmdList));
    }

    @When("User started the delete country master data process by making DELETE call to country master data api")
    public void makeApiCallForDeleteCountryMasterDataApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster/" + countryMasterDataId).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    @Then("Country Master data deleted successfully and returns 200 status code")
    public void verifyStatusAndResponseData() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User sends the invalid country master data id in the parameter")
    public void setupMockDataForDeleteCountryMasterDataService() {
        countryMasterDataId = 0L;
    }

    @When("User started the delete process by making DELETE call to country master data api")
    public void makeApiCallForDeleteCountryMasterDataService() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster/" + countryMasterDataId).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    @Then("Error message with status code 400 return")
    public void verifyStatusAndResponseDataForCountryMasterDataApi() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }
}
