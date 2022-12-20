package com.finx.stories.commonservice;

import com.finx.commonservice.api.MobileNumberCmd;
import com.finx.commonservice.api.VerifyOtpCmd;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.assertj.core.api.Assertions.assertThat;

public class VerifyOtpSteps {

    private final Client client;
    private final int localPort;
    private VerifyOtpCmd verifyOtpCmd;
    private MobileNumberCmd mobileNumberCmd;
    private Response response;

    public VerifyOtpSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("that the user enters mobile number and otp for verification")
    public void newUserWithMobileNumber() {
        this.mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0908822910")
                .build();

        this.verifyOtpCmd = VerifyOtpCmd.builder()
                .countryCode("+84")
                .number("0908822910")
                .otp(123456)
                .build();
    }

    @When("they make a post call to /otp/verify")
    public void makeApiCall() {
        Entity<MobileNumberCmd> mobileNumberCmdEntity = Entity.json(this.mobileNumberCmd);
        this.response = client.target(String.format("http://localhost:%d/otp", localPort))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(mobileNumberCmdEntity);

        Entity<VerifyOtpCmd> verifyOtpCmdEntity = Entity.json(this.verifyOtpCmd);
        this.response = client.target(String.format("http://localhost:%d/otp/verify", localPort))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(verifyOtpCmdEntity);

    }

    @Then("service should return 200 response")
    public void verify() {
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

}
