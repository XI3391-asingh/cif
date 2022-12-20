package com.finx.onboardingservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.onboardingservice.api.MobileNumberCmd;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.InternalServerErrorException;
import java.io.*;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Slf4j
public class CommonServiceClient {

    public static final String BLOCKING_PERIOD_EXPIRED_OR_NOT_FOR_CMD = "Exception while checking blocking period"
            + " expired or not for [cmd={}]";
    private final String urlCheckBlockingPeriod;

    private final ObjectMapper mapper = new ObjectMapper();


    public CommonServiceClient(String urlCheckBlockingPeriod) {
        this.urlCheckBlockingPeriod = urlCheckBlockingPeriod;
    }

    public boolean isBlockingPeriodExpired(MobileNumberCmd cmd) {
        log.info("Checking blocking period expired or not for [cmd={}]", cmd);
        HttpClient httpClient = HttpClient.newHttpClient();
        try {
            String mobileNumberBody = mapper.writeValueAsString(cmd);
            HttpRequest getLastUpdatedOnOtpRequest = HttpRequest.newBuilder()
                    .uri(new URI(this.urlCheckBlockingPeriod))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mobileNumberBody))
                    .build();

            var responseCommonUtil = httpClient.send(getLastUpdatedOnOtpRequest,
                    HttpResponse.BodyHandlers.ofString()).body();
            Map<String, Map<String, Boolean>> responseMap = mapper.readValue(responseCommonUtil, Map.class);
            return !responseMap.get("data").isEmpty() ? responseMap.get("data")
                    .get("isBlockingPeriodExpired").booleanValue() : false;
        } catch (JsonProcessingException e) {
            log.error(BLOCKING_PERIOD_EXPIRED_OR_NOT_FOR_CMD, cmd);
            throw new InternalServerErrorException(e);
        } catch (URISyntaxException | IOException e) {
            log.error(BLOCKING_PERIOD_EXPIRED_OR_NOT_FOR_CMD, cmd);
            throw new InternalServerErrorException(e);
        } catch (InterruptedException e) {
            log.error(BLOCKING_PERIOD_EXPIRED_OR_NOT_FOR_CMD, cmd);
            Thread.currentThread().interrupt();
            throw new InternalServerErrorException(e);
        }
    }
}
