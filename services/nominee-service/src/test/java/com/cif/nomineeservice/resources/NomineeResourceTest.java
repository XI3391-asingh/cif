package com.cif.nomineeservice.resources;

import com.cif.nomineeservice.api.*;
import com.cif.nomineeservice.core.nominee.services.NomineeService;
import com.cif.nomineeservice.helper.ResponseHelper;
import com.cif.nomineeservice.resources.exceptionmappers.GenericExceptionMapper;
import com.codahale.metrics.MetricRegistry;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class NomineeResourceTest {

    private final NomineeService mockNomineeService = mock(NomineeService.class);
    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new NomineeResource(mockNomineeService))
                    .addProvider(() ->
                            new GenericExceptionMapper(new MetricRegistry()))
                    .build();
    private AutoCloseable closeable = mock(AutoCloseable.class);

    private static long nomineeMappingId = 1l;


    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void should_return_success_response_when_nominees_save_succeed() {
        CreateNomineeRequest nomineeRequest = CreateNomineeRequest.builder().partyId(1).firstName("Test").lastName("Test").nationalId("01").relation(CreateNomineeRequest.RelationEnum.SPOUSE).salutation(CreateNomineeRequest.SalutationEnum.DR_).build();
        Response mockResponse = Response.status(HttpStatus.CREATED_201).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), "SUCCESS", "Create Nominee Success", null)).build();

        when(mockNomineeService.savePartyNominees(nomineeRequest)).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/")
                .request()
                .post(Entity.json(nomineeRequest));

        assertTrue(response.readEntity(SuccessResponseApi.class).getStatus().getMessage().equalsIgnoreCase("Create Nominee Success"));
        assertEquals(HttpStatus.CREATED_201, response.getStatus());
        verify(mockNomineeService, times(1)).savePartyNominees(nomineeRequest);
    }

    @Test
    void should_return_bad_request_when_wrong_input_is_passed() {
        CreateNomineeRequest nomineeRequest = CreateNomineeRequest.builder().firstName("Test").lastName("Test").nationalId("01").relation(CreateNomineeRequest.RelationEnum.SPOUSE).salutation(CreateNomineeRequest.SalutationEnum.MR_).build();
        Response mockResponse = Response.status(HttpStatus.CREATED_201).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), "SUCCESS", "Create Nominee Success", null)).build();

        when(mockNomineeService.savePartyNominees(nomineeRequest)).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/")
                .request()
                .post(Entity.json(nomineeRequest));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, response.getStatus());
    }

    @Test
    void should_return_failure_response_when_exception_occurs_during_nominee_save() {
        CreateNomineeRequest nomineeRequest = CreateNomineeRequest.builder().nomineeId(1).partyId(1).firstName("Test").lastName("Test").nationalId("01").relation(CreateNomineeRequest.RelationEnum.SPOUSE).salutation(CreateNomineeRequest.SalutationEnum.MR_).build();
        Response mockResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "ERROR", "Create Nominee Failed")).build();

        when(mockNomineeService.savePartyNominees(nomineeRequest)).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/")
                .request()
                .post(Entity.json(nomineeRequest));

        assertTrue(response.readEntity(ResponseApi.class).getStatus().getMessage().equalsIgnoreCase("Create Nominee Failed"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        verify(mockNomineeService, times(1)).savePartyNominees(nomineeRequest);
    }

    @Test
    void should_return_success_response_when_nominees_update_succeed() {
        UpdateNomineeRequest nomineeRequest = UpdateNomineeRequest.builder().nomineeId(1).partyId(1).firstName("Test").lastName("Test").nationalId("01").relation(UpdateNomineeRequest.RelationEnum.SPOUSE).salutation(UpdateNomineeRequest.SalutationEnum.DR_).build();
        Response mockResponse = Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), "SUCCESS", "Update Nominee Success", null)).build();

        when(mockNomineeService.updateNominee(nomineeRequest)).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/")
                .request()
                .put(Entity.json(nomineeRequest));

        assertTrue(response.readEntity(SuccessResponseApi.class).getStatus().getMessage().equalsIgnoreCase("Update Nominee Success"));
        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(mockNomineeService, times(1)).updateNominee(nomineeRequest);
    }

    @Test
    void should_return_failure_response_when_exception_occurs_during_nominee_update() {
        UpdateNomineeRequest nomineeRequest = UpdateNomineeRequest.builder().nomineeId(1).partyId(1).firstName("Test").lastName("Test").nationalId("01").relation(UpdateNomineeRequest.RelationEnum.SPOUSE).salutation(UpdateNomineeRequest.SalutationEnum.MR_).build();
        Response mockResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "ERROR", "Update Nominee Failed")).build();

        when(mockNomineeService.updateNominee(nomineeRequest)).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/")
                .request()
                .put(Entity.json(nomineeRequest));

        assertTrue(response.readEntity(ResponseApi.class).getStatus().getMessage().equalsIgnoreCase("Update Nominee Failed"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        verify(mockNomineeService, times(1)).updateNominee(nomineeRequest);
    }

    @Test
    void should_return_success_response_when_nominees_delete_succeed() {
        Response mockResponse = Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "SUCCESS", "Delete Nominee Success")).build();
        int nomineeId = 1;
        when(mockNomineeService.deleteNominee(nomineeId)).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/1")
                .request()
                .delete();

        assertTrue(response.readEntity(ResponseApi.class).getStatus().getMessage().equalsIgnoreCase("Delete Nominee Success"));
        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(mockNomineeService, times(1)).deleteNominee(nomineeId);
    }

    @Test
    void should_return_failure_response_when_exception_occurs_during_nominee_delete() {
        int nomineeId = 1;
        Response mockResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "ERROR", String.format("Delete For Nominee Id %s Failed", nomineeId))).build();
        when(mockNomineeService.deleteNominee(nomineeId)).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/1")
                .request()
                .delete();

        assertTrue(response.readEntity(ResponseApi.class).getStatus().getMessage().equalsIgnoreCase("Delete For Nominee Id 1 Failed"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        verify(mockNomineeService, times(1)).deleteNominee(nomineeId);
    }

    @Test
    void should_return_success_response_when_nominees_fetch_succeed() {
        FetchNomineeRequest nomineeRequest = FetchNomineeRequest.builder().nomineeId(1).build();
        Response mockResponse = Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), "SUCCESS", "Fetch Nominee Success", null)).build();
        when(mockNomineeService.fetchNomineeDetailsByPartyIdOrNomineeId(0, nomineeRequest.getNomineeId())).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/fetch/")
                .request()
                .post(Entity.json(nomineeRequest));

        assertTrue(response.readEntity(SuccessResponseApi.class).getStatus().getMessage().equalsIgnoreCase("Fetch Nominee Success"));
        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(mockNomineeService, times(1)).fetchNomineeDetailsByPartyIdOrNomineeId(0, nomineeRequest.getNomineeId());
    }

    @Test
    void should_return_failure_response_when_exception_occurs_during_nominee_fetch() {
        FetchNomineeRequest nomineeRequest = FetchNomineeRequest.builder().nomineeId(1).build();
        Response mockResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "ERROR", "Failed to fetch details for nominee")).build();
        when(mockNomineeService.fetchNomineeDetailsByPartyIdOrNomineeId(0, nomineeRequest.getNomineeId())).thenReturn(mockResponse);
        Response response = resourceTestClient.target("/nominee/fetch/")
                .request()
                .post(Entity.json(nomineeRequest));

        assertTrue(response.readEntity(ResponseApi.class).getStatus().getMessage().equalsIgnoreCase("Failed to fetch details for nominee"));
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, response.getStatus());
        verify(mockNomineeService, times(1)).fetchNomineeDetailsByPartyIdOrNomineeId(0, nomineeRequest.getNomineeId());
    }

    @Test
    void should_return_success_when_nominee_mapping_is_saved() {
        NomineeMappingRequest nomineeMappingRequest = NomineeMappingRequest.builder().nomineeId(1).partyId(1).accountNumber("70149340835549500").build();
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), "SUCCESS", "Create Nominee Mapping Success", null)).build();
        when(mockNomineeService.saveNomineeMapping(nomineeMappingRequest)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/nominee/mapping")
                .request()
                .post(Entity.json(nomineeMappingRequest));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("SUCCESS");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockNomineeService, times(1)).saveNomineeMapping(nomineeMappingRequest);
    }

    @Test
    void should_return_error_when_nominee_mapping_is_not_saved() {
        NomineeMappingRequest nomineeMappingRequest = NomineeMappingRequest.builder().nomineeId(1).partyId(1).accountNumber("70149340835549500").build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "ERROR", "Create Nominee Mapping Failed")).build();

        when(mockNomineeService.saveNomineeMapping(nomineeMappingRequest)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/nominee/mapping")
                .request()
                .post(Entity.json(nomineeMappingRequest));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockNomineeService, times(1)).saveNomineeMapping(nomineeMappingRequest);
    }

    @Test
    void should_return_failure_when_exception_occur_during_nominee_mapping_is_saved() {
        NomineeMappingRequest nomineeMappingRequest = NomineeMappingRequest.builder().nomineeId(1).partyId(1).accountNumber("70149340835549500").build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "ERROR", "Exception occurred")).build();

        when(mockNomineeService.saveNomineeMapping(nomineeMappingRequest)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/nominee/mapping")
                .request()
                .post(Entity.json(nomineeMappingRequest));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockNomineeService, times(1)).saveNomineeMapping(nomineeMappingRequest);
    }

    @Test
    void should_return_success_when_nominee_mapping_deleted() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), "SUCCESS", "Nominee mapping deleted successfully", null)).build();

        when(mockNomineeService.deleteNomineeMapping(nomineeMappingId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/nominee/mapping/" + nomineeMappingId )
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("SUCCESS");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockNomineeService, times(1)).deleteNomineeMapping(nomineeMappingId);
    }

    @Test
    void should_return_error_when_nominee_mapping_is_not_deleted() {
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "ERROR", "Error occurred while deleting nominee mapping record")).build();

        when(mockNomineeService.deleteNomineeMapping(nomineeMappingId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/nominee/mapping/" + nomineeMappingId )
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockNomineeService, times(1)).deleteNomineeMapping(nomineeMappingId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_nominee_mapping_delete() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "ERROR", "Exception occurred")).build();

        when(mockNomineeService.deleteNomineeMapping(nomineeMappingId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/nominee/mapping/" + nomineeMappingId )
                .request().delete();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockNomineeService, times(1)).deleteNomineeMapping(nomineeMappingId);
    }
}
