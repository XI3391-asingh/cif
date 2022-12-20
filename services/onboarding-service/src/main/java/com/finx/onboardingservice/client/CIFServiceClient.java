package com.finx.onboardingservice.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import javax.ws.rs.InternalServerErrorException;
import org.apache.commons.collections4.CollectionUtils;

public class CIFServiceClient {

    private final ObjectMapper mapper = new ObjectMapper();
    private String urlSearchMobile;

    public CIFServiceClient(String urlSearchMobile) {
        this.urlSearchMobile = urlSearchMobile;
    }

    public boolean isPhoneNumberExist(String mobileNumber) {
        HttpRequest getCustomerRequest;
        HttpClient httpClient = HttpClient.newHttpClient();
        String body = "{\"mobileNumber\":\"" + mobileNumber + "\"}";
        try {
            getCustomerRequest = HttpRequest.newBuilder()
                    .uri(new URI(this.urlSearchMobile))
                    .headers("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            var cifResponse = httpClient.send(getCustomerRequest, HttpResponse.BodyHandlers.ofString()).body();
            Map<String, List<Object>> map = mapper.readValue(cifResponse, Map.class);

            return CollectionUtils.isNotEmpty(map.get("data"));
        } catch (URISyntaxException | IOException e) {
            throw new InternalServerErrorException(e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            throw new InternalServerErrorException(e);
        }
    }
}
