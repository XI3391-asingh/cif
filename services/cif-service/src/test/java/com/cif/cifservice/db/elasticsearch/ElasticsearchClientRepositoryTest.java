package com.cif.cifservice.db.elasticsearch;

import com.cif.cifservice.api.AdvanceSearchRequestCmd;
import com.cif.cifservice.api.PartyResponseCmd;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.core.party.util.ElasticSearchConfig;
import com.datical.liquibase.ext.rules.annotation.Rule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class ElasticsearchClientRepositoryTest {

    private final ObjectMapper MAPPER = new ObjectMapper();
    private PartyResponseCmd partyResponseCmd;

    private HttpClient client = mock(HttpClient.class);
    private HttpResponse response = mock(HttpResponse.class);

    private ElasticSearchConfig elasticSearchConfig = mock(ElasticSearchConfig.class);

    private CryptoUtil cryptoUtil = mock(CryptoUtil.class);
    private ElasticsearchClientRepository elasticsearchClientRepository;
    private String baseUrl;
    private Long partyId;
    private HttpRequest request;

    @BeforeEach
    void beforeTest() throws IOException {
        partyResponseCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), PartyResponseCmd.class);
        partyId = 1L;
        this.baseUrl = "http://localhost:0/test";
        when(elasticSearchConfig.getHost()).thenReturn("http://localhost");
        when(elasticSearchConfig.getPort()).thenReturn(0);
        when(elasticSearchConfig.getPartyIndex()).thenReturn("test");
        when(elasticSearchConfig.getActivityPostingIndex()).thenReturn("test");
        elasticsearchClientRepository = new ElasticsearchClientRepository(elasticSearchConfig, client, cryptoUtil);
        var jsonString = MAPPER.writeValueAsString(partyResponseCmd);
        var documentUrl = this.baseUrl.concat("/").concat("_doc").concat("/").concat(partyId.toString());
        request = HttpRequest.newBuilder().headers("Content-Type", "application/json").uri(URI.create(documentUrl)).POST(HttpRequest.BodyPublishers.ofString(jsonString)).build();

    }

    @Test
    void when_send_data_for_storing_is_successful() throws IOException, InterruptedException {
        when(client.send(request, HttpResponse.BodyHandlers.discarding())).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.CREATED_201);

        elasticsearchClientRepository.createAndUpdateDocument(partyResponseCmd, partyId);

        verify(client, times(1)).send(request, HttpResponse.BodyHandlers.discarding());
    }

    @Test
    void when_send_data_for_storing_not_successful() throws IOException, InterruptedException {
        when(client.send(request, HttpResponse.BodyHandlers.discarding())).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST_400);

        elasticsearchClientRepository.createAndUpdateDocument(partyResponseCmd, partyId);

        verify(client, times(1)).send(request, HttpResponse.BodyHandlers.discarding());
    }

    @Test
    void should_return_success_response_when_get_document() throws IOException, InterruptedException {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("party.firstName", "James");
        map.put("party.lastName", "Bond");
        Set<Map<String, String>> set = new HashSet<>();
        set.add(map);
        AdvanceSearchRequestCmd advanceSearchRequestCmd = AdvanceSearchRequestCmd.builder().must(set).optional(set).build();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.must(QueryBuilders.matchQuery("party.firstName", "James"));
        queryBuilder.must(QueryBuilders.matchQuery("party.lastName", "Bond"));
        searchSourceBuilder.query(queryBuilder);
        var documentUrl = this.baseUrl.concat("/").concat("_search");

        HttpRequest request = HttpRequest.newBuilder().headers("Content-Type", "application/json").uri(URI.create(documentUrl)).POST(HttpRequest.BodyPublishers.ofString(searchSourceBuilder.toString())).build();
        when(cryptoUtil.encrypt(Mockito.anyString())).thenReturn("sampleabcxyz");
        when(client.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.OK_200);
        when(response.body()).thenReturn("{\"hits\":{\"hits\":[]}}");

        elasticsearchClientRepository.getDocumentData(advanceSearchRequestCmd);

        verify(client, times(1)).send(request, HttpResponse.BodyHandlers.ofString());

    }

    @Test ()
    void should_return_error_response_when_get_document() throws IOException, InterruptedException {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("party.firstName", "James");
        map.put("party.lastName", "Bond");
        Set<Map<String, String>> set = new HashSet<>();
        set.add(map);
        AdvanceSearchRequestCmd advanceSearchRequestCmd = AdvanceSearchRequestCmd.builder().must(set).build();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder queryBuilder = new BoolQueryBuilder();
        queryBuilder.must(QueryBuilders.matchQuery("party.firstName", "James"));
        queryBuilder.must(QueryBuilders.matchQuery("party.lastName", "Bond"));
        searchSourceBuilder.query(queryBuilder);
        var documentUrl = this.baseUrl.concat("/").concat("_search");

        HttpRequest request = HttpRequest.newBuilder().headers("Content-Type", "application/json").uri(URI.create(documentUrl)).POST(HttpRequest.BodyPublishers.ofString(searchSourceBuilder.toString())).build();
        when(cryptoUtil.encrypt(Mockito.anyString())).thenReturn("sampleabcxyz");
        when(client.send(request, HttpResponse.BodyHandlers.ofString())).thenReturn(response);
        when(response.statusCode()).thenReturn(HttpStatus.BAD_REQUEST_400);
        when(response.body()).thenReturn("{\"hits\":{\"hits\":[]}}");

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            elasticsearchClientRepository.getDocumentData(advanceSearchRequestCmd);
        });

        String expectedMessage = "Error occurred while getting document in elasticsearch";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
        verify(client, times(1)).send(request, HttpResponse.BodyHandlers.ofString());




    }
}
