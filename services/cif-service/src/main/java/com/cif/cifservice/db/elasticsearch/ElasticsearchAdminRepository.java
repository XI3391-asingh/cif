package com.cif.cifservice.db.elasticsearch;

import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.core.party.util.ElasticSearchConfig;
import com.cif.cifservice.resources.exceptions.InvalidParameterException;
import com.cif.cifservice.resources.exceptions.ServerSideException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jetty.http.HttpStatus;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class ElasticsearchAdminRepository {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchAdminRepository.class);

    private final ElasticSearchConfig searchConfig;

    private final String adminPostingBaseUrl;

    private final HttpClient client;

    private static final String ERROR = "ERROR";

    private final ObjectMapper objectMapper;


    public ElasticsearchAdminRepository(ElasticSearchConfig searchConfig, HttpClient client, ObjectMapper objectMapper) {
        this.searchConfig = searchConfig;
        this.adminPostingBaseUrl = searchConfig.getHost().concat(":").concat(searchConfig.getPort().toString()).concat("/").concat(searchConfig.getAdminPostingIndex());
        this.client = client;
        this.objectMapper = objectMapper;
    }


    public int addConfigRecord(Config configRecord) {
        int failed = HttpStatus.BAD_REQUEST_400;
        var jsonString = objectConvertToJsonString(configRecord);
        var documentUrl = this.adminPostingBaseUrl.concat("/").concat("_doc").concat("/").concat(configRecord.getType());
        HttpRequest request = HttpRequest.newBuilder().headers("Content-Type", "application/json").uri(URI.create(documentUrl)).POST(HttpRequest.BodyPublishers.ofString(jsonString)).build();
        try {
            HttpResponse<Void> response = client.send(request, HttpResponse.BodyHandlers.discarding());
            return response.statusCode();
        } catch (Exception e) {
            LOGGER.error("Error occurred while creating admin data errors {} ", e);
            Thread.currentThread().interrupt();
        }
        return failed;
    }


    public Config fetchAdminConfigRecordBasedOnType(String type) {
        var fetchConfigUrl = this.adminPostingBaseUrl.concat("/").concat("_doc").concat("/").concat(type);
        LOGGER.debug("Fetch Config URL {} ", fetchConfigUrl);

        HttpRequest fetchConfigElasticSearchRequest = HttpRequest.newBuilder().headers("Content-Type", "application/json").uri(URI.create(fetchConfigUrl)).GET().build();
        try {
            HttpResponse<String> configDetails = client.send(fetchConfigElasticSearchRequest, HttpResponse.BodyHandlers.ofString());
            if (configDetails.statusCode() == HttpStatus.OK_200 && isNotBlank(configDetails.body())) {
                LOGGER.info("Fetch Config data success with status code {} ", configDetails.statusCode());
                return convertJsonStringToObject(configDetails.body());
            } else {
                throw new InvalidParameterException("400", "Error occurred while fetching document in elasticsearch");
            }
        } catch (Exception e) {
            throw new ServerSideException(ERROR, e.getMessage(), e);
        }
    }

    public Config convertJsonStringToObject(String jsonData) {
        try {
            JSONObject json = new JSONObject(jsonData);
            Config configRecord = this.objectMapper.readValue(json.getJSONObject("_source").toString(), Config.class);
            return configRecord;
        } catch (Exception e) {
            throw new ServerSideException(ERROR, e.getMessage(), e);
        }
    }

    public String objectConvertToJsonString(Object jsonData) {
        try {
            return this.objectMapper.writeValueAsString(jsonData);
        } catch (JsonProcessingException e) {
            throw new ServerSideException(ERROR, e.getMessage(), e);
        }
    }


}
