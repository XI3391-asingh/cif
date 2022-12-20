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

public class CreateCountryMasterDataSteps extends Steps {

    private final Client client;
    private final int localPort;

    CreateCountryMasterCmd createCountryMasterCmd;
    List<CreateCountryMasterCmd> createCountryMasterCmdList;

    private Response response;


    public CreateCountryMasterDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid value in the request")
    public void setMockDataForCreateCountryMasterData() {
        this.createCountryMasterCmd = CreateCountryMasterCmd.builder().code("01").description("Vietnam").isdCode("+84").build();
        this.createCountryMasterCmdList = Collections.singletonList(createCountryMasterCmd);
    }

    @When("User started the create country master data process by making POST call to country master data api")
    public void makeApiCallForCreateCountryMasterDataApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster/").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(this.createCountryMasterCmdList));
    }

    @Then("Country master data created successfully and returns 201 status code")
    public void verifyStatusAndResponseData() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User sends the invalid country master data in the request")
    public void setMockDataForCreateCountryMasterDataService() {
        this.createCountryMasterCmd = CreateCountryMasterCmd.builder().build();
    }

    @When("User started the create country master data process by making POST api call")
    public void makeApiCallForCreateCountryMasterData() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/countrymaster").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(createCountryMasterCmd));
    }

    @Then("API will return error message with status code 400")
    public void verifyStatusAndResponseDataForCountryMasterDataService() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

}
