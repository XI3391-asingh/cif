package com.masterdata.stories.masterdataservice;


import com.masterdata.masterdataservice.api.UpdateMasterDataRequestCmd;
import org.eclipse.jetty.http.HttpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class UpdateMasterDataSteps extends Steps {

    private final Client client;
    private final int localPort;

    Long masterDataId;

    UpdateMasterDataRequestCmd updateMasterDataRequestCmd;
    private Response response;

    public UpdateMasterDataSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("User sends the valid master data in the request")
    public void setupMockDataForUpdateMasterData() {
        this.masterDataId = 1l;
        this.updateMasterDataRequestCmd = UpdateMasterDataRequestCmd.builder().type("ADDRESS_TYPE").code("01").dtType(UpdateMasterDataRequestCmd.DtTypeEnum.valueOf("LOOKUP_MASTER")).build();
    }

    @When("User started the update master data process by making PUT call to master data api")
    public void makeApiCallForUpdateMasterData() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/update/" + masterDataId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(updateMasterDataRequestCmd));
    }

    @Then("Master data updated successfully and returns 200 status code")
    public void verifyStatusAndResponseDataForMasterData() {
        assert response.getStatus() == HttpStatus.OK_200;
    }

    @Given("User sends the invalid master data id in the request")
    public void setupMockInvalidDataForMasterDataApi() {
        this.updateMasterDataRequestCmd = UpdateMasterDataRequestCmd.builder().type("ADDRESS_TYPE").code("01").dtType(UpdateMasterDataRequestCmd.DtTypeEnum.valueOf("LOOKUP_MASTER")).build();
        this.masterDataId = 0L;
    }

    @When("User started the update process by making PUT call to master data api")
    public void makeApiCallForMasterDataApi() {
        this.response = this.client.target("http://localhost:" + this.localPort + "/masterdata/update/" + masterDataId).request(MediaType.APPLICATION_JSON_TYPE).put(Entity.json(updateMasterDataRequestCmd));
    }

    @Then("Master data updated failed and returns 400 status code")
    public void verifyStatus() {
        assert response.getStatus() == HttpStatus.BAD_REQUEST_400;
    }
}
