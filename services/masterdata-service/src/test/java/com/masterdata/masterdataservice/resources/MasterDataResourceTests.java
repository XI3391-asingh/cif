package com.masterdata.masterdataservice.resources;

import ch.qos.logback.classic.Level;
import com.codahale.metrics.MetricRegistry;
import com.masterdata.masterdataservice.api.*;
import com.masterdata.masterdataservice.core.masterdata.helper.ResponseHelper;
import com.masterdata.masterdataservice.core.masterdata.services.MasterDataService;
import com.masterdata.masterdataservice.resources.exceptionmappers.GenericExceptionMapper;
import io.dropwizard.logging.BootstrapLogging;
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
import java.util.List;
import java.util.UUID;

import static com.masterdata.masterdataservice.core.masterdata.helper.ResponseHelper.buildErrorResponse;
import static com.masterdata.masterdataservice.core.masterdata.helper.ResponseHelper.buildSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(DropwizardExtensionsSupport.class)
class MasterDataResourceTests {
    private final MasterDataService mockMasterDataService = mock(MasterDataService.class);
    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new MasterDataResource(mockMasterDataService))
                    .addProvider(() ->
                            new GenericExceptionMapper(new MetricRegistry(), "masterdata_service"))
                    .build();
    private AutoCloseable closeable = mock(AutoCloseable.class);

    private static long masterDataId = 1l;

    private static String masterDataType = "LOOKUP_MASTER";


    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void should_return_success_response_when_master_data_save_succeed() {
        CreateMasterCmd createMasterCmd = CreateMasterCmd.builder().code("01").type("ADDRESS_TYPE").dtType(CreateMasterCmd.DtTypeEnum.valueOf("LOOKUP_MASTER")).build();
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("MASTER_DATA_CREATED", "Master data created", createMasterCmd)).build();

        when(mockMasterDataService.saveMasterData(Collections.singletonList(createMasterCmd))).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata")
                .request()
                .post(Entity.json(Collections.singleton(createMasterCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MASTER_DATA_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).saveMasterData(Collections.singletonList(createMasterCmd));

    }

    @Test
    void should_return_error_response_when_master_data_is_not_saved() {
        CreateMasterCmd createMasterCmd = CreateMasterCmd.builder().code(null).type("ADDRESS_TYPE").dtType(null).build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error occurred while saving master data !")).build();

        when(mockMasterDataService.saveMasterData(Collections.singletonList(createMasterCmd))).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata")
                .request()
                .post(Entity.json(Collections.singleton(createMasterCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).saveMasterData(Collections.singletonList(createMasterCmd));

    }

    @Test
    void should_return_failure_response_when_exception_occurs_during_master_data_save() {
        CreateMasterCmd createMasterCmd = CreateMasterCmd.builder().code(null).type("ADDRESS_TYPE").dtType(null).build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", "Error occurred while creating master data. Please contact the system administrator ! ")).build();

        when(mockMasterDataService.saveMasterData(Collections.singletonList(createMasterCmd))).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata")
                .request()
                .post(Entity.json(Collections.singleton(createMasterCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).saveMasterData(Collections.singletonList(createMasterCmd));

    }

    @Test
    void should_return_success_response_when_master_data_updated() {
        UpdateMasterDataRequestCmd updateMasterDataRequestCmd = UpdateMasterDataRequestCmd.builder().code("01").type("ADDRESS_TYPE").dtType(UpdateMasterDataRequestCmd.DtTypeEnum.valueOf("LOOKUP_MASTER")).build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MASTER_DATA_UPDATED", "Master data updated successfully!!", null)).build();

        when(mockMasterDataService.updateMasterData(masterDataId,updateMasterDataRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/update/"+ masterDataId)
                .request()
                .put(Entity.json(updateMasterDataRequestCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MASTER_DATA_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).updateMasterData(masterDataId,updateMasterDataRequestCmd);

    }

    @Test
    void should_return_error_when_master_data_is_not_updated() {
        UpdateMasterDataRequestCmd updateMasterDataRequestCmd = UpdateMasterDataRequestCmd.builder().code("01").type("ADDRESS_TYPE").dtType(UpdateMasterDataRequestCmd.DtTypeEnum.valueOf("LOOKUP_MASTER")).build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error occurred while updating record !")).build();

        when(mockMasterDataService.updateMasterData(masterDataId,updateMasterDataRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/update/"+ masterDataId)
                .request()
                .put(Entity.json(updateMasterDataRequestCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).updateMasterData(masterDataId,updateMasterDataRequestCmd);

    }
    @Test
    void should_return_failure_when_exception_occur_during_master_data_update() {
        UpdateMasterDataRequestCmd updateMasterDataRequestCmd = UpdateMasterDataRequestCmd.builder().code("01").type("ADDRESS_TYPE").dtType(UpdateMasterDataRequestCmd.DtTypeEnum.valueOf("LOOKUP_MASTER")).build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR_CODE", "Error occurred while update record. Please contact the system administrator ! ")).build();

        when(mockMasterDataService.updateMasterData(masterDataId,updateMasterDataRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/update/"+ masterDataId)
                .request()
                .put(Entity.json(updateMasterDataRequestCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR_CODE");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).updateMasterData(masterDataId,updateMasterDataRequestCmd);

    }


    @Test
    void should_return_success_when_fetch_master_data_success() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MASTER_DATA_FOUND", "Master data found !", null)).build();

        when(mockMasterDataService.fetchAllMasterData(masterDataType)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/"+masterDataType)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MASTER_DATA_FOUND");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).fetchAllMasterData(masterDataType);
    }
    @Test
    void should_return_error_when_no_record_found_during_fetch_master_data() throws IOException {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("ERROR", "Invalid category type")).build();

        when(mockMasterDataService.fetchAllMasterData(masterDataType)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/"+masterDataType)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).fetchAllMasterData(masterDataType);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_master_data() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_NOT_FOUND_ERROR_CODE,", null)).build();

        when(mockMasterDataService.fetchAllMasterData(masterDataType)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/"+masterDataType)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DATA_NOT_FOUND_ERROR_CODE,");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).fetchAllMasterData(masterDataType);
    }

    @Test
    void should_return_success_when_master_data_deleted() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MASTER_DATA_DELETE", "Master data deleted successfully!",null)).build();

        when(mockMasterDataService.deleteMasterData(masterDataId,masterDataType)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/"+masterDataId+"/"+masterDataType)
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MASTER_DATA_DELETE");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).deleteMasterData(masterDataId,masterDataType);
    }
    @Test
    void should_return_error_when_master_data_is_not_deleted() throws IOException {
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error occurred while deleting master data record!")).build();

        when(mockMasterDataService.deleteMasterData(masterDataId,masterDataType)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/"+masterDataId+"/"+masterDataType)
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).deleteMasterData(masterDataId,masterDataType);
    }

    @Test
    void should_return_failure_when_exception_occur_during_master_data_delete() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR_CODE", "Error occurred while deleting master data record. Please contact the system administrator!")).build();

        when(mockMasterDataService.deleteMasterData(masterDataId,masterDataType)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/"+masterDataId+"/"+masterDataType)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR_CODE");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).deleteMasterData(masterDataId,masterDataType);
    }


    @Test
    void should_return_success_response_when_country_master_data_save_succeed() {
        CreateCountryMasterCmd createCountryMasterCmd = CreateCountryMasterCmd.builder().code("01").createdBy("Aparna").build();
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("COUNTRY_MASTER_DATA_CREATED", "Country master data created", createCountryMasterCmd)).build();

        when(mockMasterDataService.saveCountryMasterData(Collections.singletonList(createCountryMasterCmd))).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster")
                .request()
                .post(Entity.json(Collections.singleton(createCountryMasterCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("COUNTRY_MASTER_DATA_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).saveCountryMasterData(Collections.singletonList(createCountryMasterCmd));

    }

    @Test
    void should_return_error_response_when_country_master_data_not_saved() {
        CreateCountryMasterCmd createCountryMasterCmd = CreateCountryMasterCmd.builder().code("01").createdBy("Aparna").build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error occurred while saving country master data !")).build();

        when(mockMasterDataService.saveCountryMasterData(Collections.singletonList(createCountryMasterCmd))).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster")
                .request()
                .post(Entity.json(Collections.singleton(createCountryMasterCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).saveCountryMasterData(Collections.singletonList(createCountryMasterCmd));

    }
    @Test
    void should_return_failure_exception_during_create_country_master() {
        CreateCountryMasterCmd createCountryMasterCmd = CreateCountryMasterCmd.builder().code("01").createdBy("Aparna").build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR_CODE", "Error occurred while creating country master data. Please contact the system administrator ! ")).build();

        when(mockMasterDataService.saveCountryMasterData(Collections.singletonList(createCountryMasterCmd))).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster")
                .request()
                .post(Entity.json(Collections.singleton(createCountryMasterCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR_CODE");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).saveCountryMasterData(Collections.singletonList(createCountryMasterCmd));

    }

    @Test
    void should_return_success_when_fetch_country_master_data_success() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("COUNTRY_MASTER_DATA_FOUND", "country master data found !", null)).build();

        when(mockMasterDataService.fetchAllCountryMasterData()).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("COUNTRY_MASTER_DATA_FOUND");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).fetchAllCountryMasterData();
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_country_master_data() throws IOException {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for country master data!!")).build();

        when(mockMasterDataService.fetchAllCountryMasterData()).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster")
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).fetchAllCountryMasterData();
    }
    @Test
    void should_return_failure_response_when_exception_occur_during_fetch_country_master_data() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_NOT_FOUND_ERROR_CODE", "Error occurred while fetch country master data.Please contact the system administrator!")).build();

        when(mockMasterDataService.fetchAllCountryMasterData()).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DATA_NOT_FOUND_ERROR_CODE");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).fetchAllCountryMasterData();
    }
    @Test
    void should_return_success_when_country_master_data_deleted() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("COUNTRY_MASTER_DATA_DELETE", "Country master data deleted successfully!",null)).build();

        when(mockMasterDataService.deleteCountryMasterData(masterDataId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster/"+masterDataId)
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("COUNTRY_MASTER_DATA_DELETE");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).deleteCountryMasterData(masterDataId);
    }
    @Test
    void should_return_error_when_country_master_data_is_not_deleted() throws IOException {
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error occurred while deleting country master data record!")).build();

        when(mockMasterDataService.deleteCountryMasterData(masterDataId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster/"+masterDataId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).deleteCountryMasterData(masterDataId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_country_master_delete() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR_CODE", "Error occurred while deleting country master data record. Please contact the system administrator!")).build();

        when(mockMasterDataService.deleteCountryMasterData(masterDataId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster/"+masterDataId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR_CODE");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).deleteCountryMasterData(masterDataId);
    }

    @Test
    void should_return_success_response_when_country_master_data_updated() {
        UpdateCountryMasterCmd updateCountryMasterCmd = UpdateCountryMasterCmd.builder().code("01").build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("COUNTRY_MASTER_DATA_UPDATED", "Country master data updated successfully!!", null)).build();

        when(mockMasterDataService.updateCountryMasterData(masterDataId,updateCountryMasterCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster/update/"+ masterDataId)
                .request()
                .put(Entity.json(updateCountryMasterCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("COUNTRY_MASTER_DATA_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).updateCountryMasterData(masterDataId,updateCountryMasterCmd);

    }

    @Test
    void should_return_error_response_when_country_master_data_is_not_update() {
        UpdateCountryMasterCmd updateCountryMasterCmd = UpdateCountryMasterCmd.builder().code("01").build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error occurred while updating record !")).build();

        when(mockMasterDataService.updateCountryMasterData(masterDataId,updateCountryMasterCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster/update/"+ masterDataId)
                .request()
                .put(Entity.json(updateCountryMasterCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).updateCountryMasterData(masterDataId,updateCountryMasterCmd);

    }
    @Test
    void should_return_failure_response_when_exception_occurs_during_country_master_update() {
        UpdateCountryMasterCmd updateCountryMasterCmd = UpdateCountryMasterCmd.builder().code("01").build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR_CODE", "Error occurred while update record. Please contact the system administrator ! ")).build();

        when(mockMasterDataService.updateCountryMasterData(masterDataId,updateCountryMasterCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/masterdata/countrymaster/update/"+ masterDataId)
                .request()
                .put(Entity.json(updateCountryMasterCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR_CODE");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockMasterDataService, times(1)).updateCountryMasterData(masterDataId,updateCountryMasterCmd);

    }









}