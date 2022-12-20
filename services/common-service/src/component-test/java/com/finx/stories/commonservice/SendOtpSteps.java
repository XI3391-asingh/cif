package com.finx.stories.commonservice;

import com.finx.commonservice.api.MobileNumberCmd;
import com.finx.commonservice.api.ResponseApi;
import com.finx.commonservice.core.otp.enums.OtpStatus;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class SendOtpSteps {

    private final Client client;
    private final int localPort;
    private MobileNumberCmd mobileNumberCmd;
    private Response response;

    public SendOtpSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("that the user enters mobile number for registration")
    public void newUserWithMobileNumber() {
        this.mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0908822911")
                .build();
    }

    @When("they make a post call to /otp")
    public void makeApiCall() {
        Entity<MobileNumberCmd> cmdEntity = Entity.json(this.mobileNumberCmd);
        this.response = client.target(String.format("http://localhost:%d/otp", localPort))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(cmdEntity);

    }

    @Then("service should return 200 response")
    public void verify() {
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @When("they make a post call to /otp 5 times")
    public void makeApiCallFiveTimes() {
        Entity<MobileNumberCmd> cmdEntity = Entity.json(this.mobileNumberCmd);
        for (int i = 0; i < 5; i++) {
            this.response = client.target(String.format("http://localhost:%d/otp", localPort))
                    .request(MediaType.APPLICATION_JSON_TYPE)
                    .post(cmdEntity);
        }
    }
    @Then("service should send failure message")
    public void verifyResponseAfterCallingApiFiveTimes() {
        assertThat(response.readEntity(ResponseApi.class).getMessage()).isEqualTo(OtpStatus.GENERATION_BLOCKED.name());
    }
}
