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

public class DeleteMasterDataSteps extends Steps {

    private final Client client;
    private final int localPort;

    Long masterDataId;
    CreateMasterCmd createMasterCmd;
    List<CreateMasterCmd> createMasterCmdList;

    String masterDataType;
    private Response response;

    public DeleteMasterDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid master data id in the parameter")
    public void setupMockDataForDeleteMasterDataApi() {
        this.masterDataId = 2L;
        this.masterDataType = "LOOKUP_MASTER";
    }

    @When("Create a valid master data record")
    public void makeApiCallForCreateMasterDataApi() {
        this.createMasterCmd = CreateMasterCmd.builder().code("01").type("ADDRESS_TYPE").dtType(CreateMasterCmd.DtTypeEnum.LOOKUP_MASTER).createdBy("User1").description("Vietnam").build();
        this.createMasterCmdList = Collections.singletonList(createMasterCmd);
        this.client.target("http://localhost:" + this.localPort + "/masterdata/").request(MediaType.APPLICATION_JSON_TYPE).post(Entity.json(this.createMasterCmdList));
    }

    @When("User started the delete master data process by making DELETE call to master data api")
    public void makeApiCallForDeleteMasterDataApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/" + masterDataId + "/" + masterDataType).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    @Then("Master data deleted successfully and returns 200 status code")
    public void verifyStatusAndResponseData() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User sends the invalid master data id in the parameter")
    public void setupMockDataForDeleteMasterDataService() {
        this.masterDataId = 0L;
        this.masterDataType = "Test";
    }

    @When("User started the delete process by making DELETE call to master data api")
    public void makeApiCallForDeleteMasterDataService() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/" + masterDataId + "/" + masterDataType).request(MediaType.APPLICATION_JSON_TYPE).delete();
    }

    @Then("API returns 400 status code")
    public void verifyStatusAndResponseDataForMasterDataApi() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }
}
