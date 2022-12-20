package com.cif.cifservice.db.elasticsearch;

import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.core.party.util.ElasticSearchConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ElasticsearchAdminRepositoryTest {

    private ElasticsearchAdminRepository elasticsearchAdminRepository;
    private final ObjectMapper MAPPER = new ObjectMapper();

    private Config configRecord;

    private HttpResponse response = mock(HttpResponse.class);
    private ElasticSearchConfig elasticSearchConfig = mock(ElasticSearchConfig.class);

    private HttpClient client = mock(HttpClient.class);
    private HttpRequest request;
    private String adminPostingBaseUrl;

    private String documentUrl;

    @BeforeEach
    void beforeTest() throws IOException {
        configRecord= MAPPER.readValue(getClass().getResource("/fixtures/CreateAdmin.json"), Config.class);
        this.adminPostingBaseUrl = "http://localhost:0/test";
        when(elasticSearchConfig.getHost()).thenReturn("http://localhost");
        when(elasticSearchConfig.getPort()).thenReturn(0);
        when(elasticSearchConfig.getActivityPostingIndex()).thenReturn("test");
        when(elasticSearchConfig.getAdminPostingIndex()).thenReturn("test");
        elasticsearchAdminRepository= new ElasticsearchAdminRepository(elasticSearchConfig,client,this.MAPPER);
        var jsonString = MAPPER.writeValueAsString(configRecord);
        this.documentUrl = this.adminPostingBaseUrl.concat("/").concat("_doc").concat("/").concat(configRecord.getType());
        request = HttpRequest.newBuilder().headers("Content-Type", "application/json").uri(URI.create(documentUrl)).POST(HttpRequest.BodyPublishers.ofString(jsonString)).build();
    }
    @Order(1)
    @Test
    void should_return_success_response_when_config_added() throws IOException, InterruptedException {
        when(client.send(request, HttpResponse.BodyHandlers.discarding())).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.CREATED_201);

        elasticsearchAdminRepository.addConfigRecord(configRecord);

        verify(client,times(1)).send(request, HttpResponse.BodyHandlers.discarding());
    }

    @Order(2)
    @Test
    void should_return_failure_response_when_config_not_added() throws IOException, InterruptedException {
        when(client.send(request, HttpResponse.BodyHandlers.discarding())).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST_400);

        elasticsearchAdminRepository.addConfigRecord(configRecord);

        verify(client,times(1)).send(request, HttpResponse.BodyHandlers.discarding());
    }

    @Order(3)
    @Test
    void should_return_success_response_on_fetch_config() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder().headers("Content-Type", "application/json").uri(URI.create(documentUrl)).GET().build();

        when(client.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.OK_200);
        when(response.body()).thenReturn("{\"_source\":{\"configData\":[]}}");

        elasticsearchAdminRepository.fetchAdminConfigRecordBasedOnType("UNIVERSALSEARCHFIELDS");

       verify(client,times(1)).send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

    @Order(4)
    @Test
    void should_return_failure_response_on_fetch_config() throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder().headers("Content-Type", "application/json").uri(URI.create(documentUrl)).GET().build();

        when(client.send(httpRequest, HttpResponse.BodyHandlers.ofString())).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST_400);
        when(response.body()).thenReturn("{\"_source\":{\"configData\":[]}}");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            elasticsearchAdminRepository.fetchAdminConfigRecordBasedOnType("UNIVERSALSEARCHFIELDS");
        });

        String expectedMessage = "Error occurred while fetching document in elasticsearch";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(client,times(1)).send(httpRequest, HttpResponse.BodyHandlers.ofString());
    }

}
