package com.finx.stories.commonservice;

import com.finx.commonservice.api.MobileNumberCmd;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class BlockingPeriodExpiredSteps {

    private final Client client;
    private final int localPort;
    private MobileNumberCmd mobileNumberCmd;
    private Response response;
    public BlockingPeriodExpiredSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }

    @Given("user enters a mobile number for checking if its blocked or not")
    public void mobileNumberForChecking() {
        this.mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0908822911")
                .build();
    }

    @When("they make POST call to /otp/blocking-period-expired")
    public void makeApiCall() {
        Entity<MobileNumberCmd> cmdEntity = Entity.json(this.mobileNumberCmd);
        this.response = client.target(String.format("http://localhost:%d/otp/blocking-period-expired", localPort))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(cmdEntity);
    }

    @Then("service should return true as response")
    public void verify() {
        Map<String, Map<String, Boolean>> responseMap = response.readEntity(Map.class);
        assertTrue(responseMap.get("data").get("isBlockingPeriodExpired").booleanValue());
    }
}
