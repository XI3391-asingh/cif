package com.masterdata.stories.masterdataservice;


import com.masterdata.masterdataservice.api.CreateMasterCmd;
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

public class CreateMasterDataSteps extends Steps {

    private final Client client;
    private final int localPort;

    CreateMasterCmd createMasterCmd;
    List<CreateMasterCmd> createMasterCmds;

    private Response response;


    public CreateMasterDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid data in the request")
    public void setMockDataForCreateMasterDataApi() {
        this.createMasterCmd = CreateMasterCmd.builder().code("01").type("ADDRESS_TYPE").dtType(CreateMasterCmd.DtTypeEnum.LOOKUP_MASTER).createdBy("User1").description("Vietnam").build();
        this.createMasterCmds = Collections.singletonList(createMasterCmd);
    }

    @When("User started the create master data process by making POST call to master data api")
    public void makeApiCallForCreateMasterDataApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(this.createMasterCmds));

    }

    @Then("Master data created successfully and returns 201 status code")
    public void verifyStatusAndResponseData() {
        assert response.getStatus() == HttpStatus.CREATED_201;
    }

    @Given("User sends the invalid master data in the request")
    public void setMockDataForCreateMasterDataService() {
        this.createMasterCmd = CreateMasterCmd.builder().build();
    }

    @When("User started the create master data process by making POST api call")
    public void makeApiCallForCreateMasterDataService() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(createMasterCmd));

    }

    @Then("API will return error with status code 400")
    public void verifyStatusAndResponseDataForCreateMasterData() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }

}
