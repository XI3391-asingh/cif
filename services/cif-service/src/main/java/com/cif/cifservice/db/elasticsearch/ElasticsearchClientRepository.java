package com.cif.cifservice.db.elasticsearch;

import com.cif.cifservice.api.AdvanceSearchRequestCmd;
import com.cif.cifservice.api.PartyResponseCmd;
import com.cif.cifservice.core.party.domain.Event;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.core.party.util.ElasticSearchConfig;
import com.cif.cifservice.resources.exceptions.InvalidParameterException;
import com.cif.cifservice.resources.exceptions.ServerSideException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ElasticsearchClientRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchClientRepository.class);

    private final ElasticSearchConfig searchConfig;

    private final String partyIndexBaseUrl;
    private final String activityPostingBaseUrl;

    private final HttpClient client;

    private static final String ERROR = "ERROR";
    private static final String CONTENT_TYPE = "Content-Type";

    private static final String APPLICATION_JSON = "application/json";

    private final ObjectMapper objectMapper;

    private final CryptoUtil cryptoUtil;


    public ElasticsearchClientRepository(ElasticSearchConfig searchConfig, HttpClient client, CryptoUtil cryptoUtil) {
        this.searchConfig = searchConfig;
        this.client = client;
        this.partyIndexBaseUrl = searchConfig.getHost().concat(":").concat(searchConfig.getPort().toString()).concat("/").concat(searchConfig.getPartyIndex());
        this.activityPostingBaseUrl = searchConfig.getHost().concat(":").concat(searchConfig.getPort().toString()).concat("/").concat(searchConfig.getActivityPostingIndex());
        this.objectMapper = new ObjectMapper();
        this.cryptoUtil = cryptoUtil;
    }

    public void createAndUpdateDocument(Object jsonData, Long partyId) {
        LOGGER.info("Document data storing on elasticsearch for party id {} and index {} ", partyId, searchConfig.getPartyIndex());
        var jsonString = objectConvertToJsonString(jsonData);
        var documentUrl = this.partyIndexBaseUrl.concat("/").concat("_doc").concat("/").concat(partyId.toString());
        LOGGER.info("Create and update document URL {} ", documentUrl);
        HttpRequest request = HttpRequest.newBuilder().headers(CONTENT_TYPE, APPLICATION_JSON).uri(URI.create(documentUrl)).POST(HttpRequest.BodyPublishers.ofString(jsonString)).build();

        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() == HttpStatus.CREATED_201) {
                LOGGER.info("Party id {} document data stored on elasticsearch and status code {} ", partyId, response.statusCode());
            } else {
                LOGGER.error("Error occurred while storing data elasticsearch for party id {} and status code {} ", partyId, response.statusCode());
            }
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating and updating document errors {} ", e);
            Thread.currentThread().interrupt();
        }
    }

    public boolean postActivityLog(Event eventData) {
        var jsonString = objectConvertToJsonString(eventData);
        var documentUrl = this.activityPostingBaseUrl.concat("/").concat("_doc").concat("/").concat(eventData.eventId());
        HttpRequest request = HttpRequest.newBuilder().headers(CONTENT_TYPE, APPLICATION_JSON).uri(URI.create(documentUrl)).POST(HttpRequest.BodyPublishers.ofString(jsonString)).build();
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            if (response.statusCode() == HttpStatus.CREATED_201)
                return true;

        } catch (Exception e) {
            LOGGER.error("Error occurred while creating and updating document errors {} ", e);
            Thread.currentThread().interrupt();
        }
        return false;
    }

    public Set<Object> getDocumentData(AdvanceSearchRequestCmd advanceSearchRequestCmd) {
        var getDocumentUrl = this.partyIndexBaseUrl.concat("/").concat("_search");
        LOGGER.info("Get document URL {} ", getDocumentUrl);
        var searchQuery = buildSearchQuery(advanceSearchRequestCmd);

        LOGGER.info("Elastic search generated query : {} ", searchQuery);
        HttpRequest request = HttpRequest.newBuilder().headers(CONTENT_TYPE, APPLICATION_JSON).uri(URI.create(getDocumentUrl)).POST(HttpRequest.BodyPublishers.ofString(searchQuery.toString())).build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == HttpStatus.OK_200 && isNotBlank(response.body())) {
                LOGGER.info("Get Document data and status code {} ", response.statusCode());
                return convertJsonStringToObject(response.body());
            } else {
                throw new InvalidParameterException("400", "Error occurred while getting document in elasticsearch");
            }
        } catch (Exception e) {
            throw new ServerSideException(ERROR, e.getMessage(), e);
        }
    }

    public SearchSourceBuilder buildSearchQuery(AdvanceSearchRequestCmd advanceSearchRequestCmd) {
        var searchSourceBuilder = new SearchSourceBuilder();
        var finalBoolQuery = new BoolQueryBuilder();

        if (!advanceSearchRequestCmd.getMust().isEmpty()) {
            for (Map<String, String> mustMap : advanceSearchRequestCmd.getMust()) {
                Set<String> keySet = mustMap.keySet();
                for (String key : keySet) {
                    StringBuilder value = new StringBuilder("*");
                    if (cryptoUtil.getFields().contains(key)) {
                        value.append(cryptoUtil.encrypt(mustMap.get(key)));
                    } else {
                        value.append(mustMap.get(key));
                    }
                    key += ".keyword";
                    value.append("*");
                    finalBoolQuery.must(QueryBuilders.wildcardQuery(key, value.toString()));
                }
            }
        }
        if (!advanceSearchRequestCmd.getOptional().isEmpty()) {
            for (Map<String, String> optionalMap : advanceSearchRequestCmd.getOptional()) {
                Set<String> keySet = optionalMap.keySet();
                for (String key : keySet) {
                    StringBuilder value = new StringBuilder("*");
                    if (cryptoUtil.getFields().contains(key)) {
                        value.append(cryptoUtil.encrypt(optionalMap.get(key)));
                    } else {
                        value.append(optionalMap.get(key));
                    }
                    key += ".keyword";
                    value.append("*");
                    finalBoolQuery.should(QueryBuilders.wildcardQuery(key, value.toString()));
                }
            }
        }

        searchSourceBuilder.query(finalBoolQuery);
        return searchSourceBuilder;
    }

    public String objectConvertToJsonString(Object jsonData) {
        try {
            return this.objectMapper.writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            throw new ServerSideException(ERROR, e.getMessage(), e);
        }
    }

    public Set<Object> convertJsonStringToObject(String jsonData) {
        Set<Object> searchResponseSet = new HashSet<>();
        try {
            JSONObject json = new JSONObject(jsonData);
            JSONObject hits = json.getJSONObject("hits");
            JSONArray hitsArray = hits.getJSONArray("hits");
            for (int i = 0; i < hitsArray.length(); i++) {
                JSONObject h = hitsArray.getJSONObject(i);
                PartyResponseCmd partyResponseCmd = this.objectMapper.readValue(h.getJSONObject("_source").toString(), PartyResponseCmd.class);
                cryptoUtil.decryptObjectUsingConfiguredField(partyResponseCmd);
                searchResponseSet.add(partyResponseCmd);
            }
            return searchResponseSet;
        } catch (Exception e) {
            throw new ServerSideException(ERROR, e.getMessage(), e);
        }
    }

}
