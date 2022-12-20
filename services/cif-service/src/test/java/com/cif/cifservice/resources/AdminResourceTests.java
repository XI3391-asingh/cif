package com.cif.cifservice.resources;

import com.cif.cifservice.api.SuccessResponseApi;
import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.core.party.services.AdminService;
import com.cif.cifservice.resources.exceptionmappers.GenericExceptionMapper;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.eclipse.jetty.http.HttpStatus;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;

import static com.cif.cifservice.core.party.helper.ResponseHelper.buildErrorResponse;
import static com.cif.cifservice.core.party.helper.ResponseHelper.buildSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
public class AdminResourceTests {
    private final AdminService mockAdminService = mock(AdminService.class);

    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new AdminResource(mockAdminService))
                    .addProvider(MultiPartFeature.class)
                    .addProvider(() ->
                            new GenericExceptionMapper(new MetricRegistry(), "admin_service"))
                    .build();

    private final ObjectMapper MAPPER = new ObjectMapper();
    private String type = "UNIVERSALSEARCHFIELDS";


    @Test
    void should_return_success_response_when_create_admin_panel_record() throws IOException {
        Config configRecord = MAPPER.readValue(getClass().getResource("/fixtures/CreateAdmin.json"), Config.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("CONFIG_CREATED", "Config record persist success", null)).build();

        when(mockAdminService.save(Mockito.any(Config.class))).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/admin/")
                .request()
                .post(Entity.json(configRecord));

        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockAdminService, times(1)).save(Mockito.any(Config.class));
    }

    @Test
    void should_return_validation_error_when_wrong_input_passed_during_create_admin_panel_record() throws IOException {
        Config configRecord = MAPPER.readValue(getClass().getResource("/fixtures/CreateAdmin.json"), Config.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error occurred while saving config record!")).build();

        when(mockAdminService.save(Mockito.any(Config.class))).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/admin/")
                .request()
                .post(Entity.json(configRecord));

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockAdminService, times(1)).save(Mockito.any(Config.class));
    }

    @Test
    void should_return_success_when_fetch_admin_data() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONFIG_FETCH", "Config record found!", null)).build();

        when(mockAdminService.fetchConfigByRecord(type)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/admin/" + type)
                .request().get();
        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONFIG_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockAdminService, times(1)).fetchConfigByRecord(type);
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_admin_data() {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found !")).build();

        when(mockAdminService.fetchConfigByRecord(type)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/admin/" + type)
                .request().get();
        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockAdminService, times(1)).fetchConfigByRecord(type);
    }
}
