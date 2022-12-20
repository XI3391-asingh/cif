package com.cif.syncerservice.resources;

import com.cif.syncerservice.api.*;
import com.cif.syncerservice.core.syncer.domain.AdapterConfig;
import com.cif.syncerservice.core.syncer.services.SyncerService;
import com.cif.syncerservice.resources.exceptionmappers.GenericExceptionMapper;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.cif.syncerservice.core.syncer.helper.ResponseHelper.buildSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SyncerResourceTest {

    private final SyncerService syncerService = mock(SyncerService.class);

    private ObjectMapper objectMapper;
    private CreateConfigRequest createConfigRequest;
    private UpdateConfigRequest updateConfigRequest;

    private CreateAdapterRequest createAdapterRequest;
    private UpdateAdapterRequest updateAdapterRequest;

    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new SyncerResource(syncerService))
                    .addProvider(() ->
                            new GenericExceptionMapper(new MetricRegistry(), "syncer_service"))
                    .build();

    @BeforeAll
    void setup() throws IOException {
        objectMapper = new ObjectMapper();
        createConfigRequest = objectMapper.readValue(getClass().getResource("/fixtures/config.json"), CreateConfigRequest.class);
        updateConfigRequest = objectMapper.readValue(getClass().getResource("/fixtures/config.json"), UpdateConfigRequest.class);
        createAdapterRequest = objectMapper.readValue(getClass().getResource("/fixtures/adapter.json"), CreateAdapterRequest.class);
        updateAdapterRequest = objectMapper.readValue(getClass().getResource("/fixtures/adapter.json"), UpdateAdapterRequest.class);
    }


    @Test
    void should_return_success_response_when_config_created_successfully() {
        Response expectedResponse = Response.status(HttpStatus.CREATED_201)
                .entity(buildSuccessResponse("CONFIG_CREATED", "Config record created successfully!",
                        createConfigRequest)).build();

        when(syncerService.createConfigData(createConfigRequest)).thenReturn(expectedResponse);

        Response actualResponse = resourceTestClient.target("/syncer/config/")
                .request()
                .post(Entity.json(createConfigRequest));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONFIG_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(syncerService, times(1)).createConfigData(createConfigRequest);

    }


    @Test
    void should_return_success_response_when_config_updated_successfully() {
        Response expectedResponse = Response.status(HttpStatus.OK_200)
                .entity(buildSuccessResponse("CONFIG_UPDATED", "Update Config record Success",
                        updateConfigRequest)).build();

        when(syncerService.updateConfigData(updateConfigRequest)).thenReturn(expectedResponse);

        Response actualResponse = resourceTestClient.target("/syncer/config/")
                .request()
                .put(Entity.json(updateConfigRequest));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONFIG_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(syncerService, times(1)).updateConfigData(updateConfigRequest);

    }


    @Test
    void should_return_success_response_when_config_fetched_successfully() {
        FetchRequest fetchConfigRequest = FetchRequest.builder().systemCode("TEST").build();
        Response expectedResponse = Response.status(HttpStatus.OK_200).
                entity(buildSuccessResponse("SUCCESS", "Fetch Change Config Success",
                        new ConfigResponseCmd())).build();

        when(syncerService.fetchConfigDetailsBySystemCode(fetchConfigRequest.getSystemCode())).thenReturn(expectedResponse);

        Response actualResponse = resourceTestClient.target("/syncer/config/fetch")
                .request()
                .post(Entity.json(fetchConfigRequest));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("SUCCESS");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(syncerService, times(1)).fetchConfigDetailsBySystemCode(fetchConfigRequest.getSystemCode());

    }


    @Test
    void should_return_success_response_when_adapter_created_successfully() {
        Response expectedResponse = Response.status(HttpStatus.CREATED_201)
                .entity(buildSuccessResponse("ADAPTER_CREATED", "ADAPTER record created successfully!",
                        createAdapterRequest)).build();

        when(syncerService.createAdapterData(createAdapterRequest)).thenReturn(expectedResponse);

        Response actualResponse = resourceTestClient.target("/syncer/adapter/")
                .request()
                .post(Entity.json(createAdapterRequest));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADAPTER_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(syncerService, times(1)).createAdapterData(createAdapterRequest);
    }


    @Test
    void should_return_success_response_when_adapter_updated_successfully() {
        Response expectedResponse = Response.status(HttpStatus.OK_200)
                .entity(buildSuccessResponse("ADAPTER_UPDATED", "Update Adapter record Success",
                        updateAdapterRequest)).build();

        when(syncerService.updateAdapterData(updateAdapterRequest)).thenReturn(expectedResponse);

        Response actualResponse = resourceTestClient.target("/syncer/adapter/")
                .request()
                .put(Entity.json(updateAdapterRequest));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADAPTER_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(syncerService, times(1)).updateAdapterData(updateAdapterRequest);

    }


    @Test
    void should_return_success_response_when_adapter_fetched_successfully() {
        FetchAdapterRequest fetchRequest = FetchAdapterRequest.builder().adapterId(1).build();
        Response expectedResponse = Response.status(HttpStatus.OK_200).
                entity(buildSuccessResponse("SUCCESS", "Fetch Adapter Config Success",
                        new AdapterConfig())).build();

        when(syncerService.fetchAdapterDetailsBySystemCodeOrSystemId(fetchRequest.getAdapterId())).thenReturn(expectedResponse);

        Response actualResponse = resourceTestClient.target("/syncer/adapter/fetch")
                .request()
                .post(Entity.json(fetchRequest));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("SUCCESS");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(syncerService, times(1)).fetchAdapterDetailsBySystemCodeOrSystemId(fetchRequest.getAdapterId());
    }

}