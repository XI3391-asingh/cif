package com.finx.stories.onboardingservice;

import com.finx.onboardingservice.api.MobileNumberCmd;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.http.Body;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static org.assertj.core.api.Assertions.assertThat;

public class VerifyMobileNumberSteps {
    private static final String MOBILE_DEDUPE_PATH = "/party/searchbymobile";
    private final Client client;
    private final int localPort;
    private MobileNumberCmd mobileNumberCmd;
    private Response response;

    private final WireMockServer wireMockServer = new WireMockServer(8080);

    public VerifyMobileNumberSteps(Client client, int localPort) {
        this.client = client;
        this.localPort = localPort;
    }


    @Given("that the user is not registered and we have started a workflow and they have a valid mobile number")
    public void newUserWithMobileNo() {
        /*
        TODO: Save a new worklfow in the database. Use that workflow Id
         */
        this.mobileNumberCmd = new MobileNumberCmd("+84", "0908822911");
    }

    @When("they make a POST call to /customer/verify-mobile")
    public void makeApiCall() {
        String body = "{\"mobileNumber\":\"" + mobileNumberCmd.getNumber() + "\"}";
        wireMockServer.start();
        configureFor("localhost", 8080);
        stubFor(post(urlEqualTo(MOBILE_DEDUPE_PATH))
                .withHeader("content-type", equalTo(APPLICATION_JSON))
                .withRequestBody(containing(body))
                .willReturn(aResponse().withStatus(200).withResponseBody(Body.fromJsonBytes("{\"data\":[]}".getBytes()))));

        Entity<MobileNumberCmd> cmdEntity = Entity.json(this.mobileNumberCmd);
        this.response = client.target(String.format("http://localhost:%d/api/v1/customer/verify-mobile", localPort))
                .request(MediaType.APPLICATION_JSON_TYPE)
                .post(cmdEntity);

        wireMockServer.stop();

    }

    @Then("service should return 200")
    public void verify() {
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());

    }
}
