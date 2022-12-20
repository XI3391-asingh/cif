package com.cif.cifservice.resources;

import ch.qos.logback.classic.Level;
import com.cif.cifservice.api.*;
import com.cif.cifservice.core.party.services.PartyService;
import com.cif.cifservice.resources.exceptionmappers.GenericExceptionMapper;
import com.cif.cifservice.resources.exceptions.ServerSideException;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.Arrays;
import java.util.Collections;

import static com.cif.cifservice.core.party.helper.ResponseHelper.buildErrorResponse;
import static com.cif.cifservice.core.party.helper.ResponseHelper.buildSuccessResponse;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class PartyResourceTests {

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    private final PartyService mockPartyService = mock(PartyService.class);
    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new PartyResource(mockPartyService))
                    .addProvider(() ->
                            new GenericExceptionMapper(new MetricRegistry(), "customer_service"))
                    .build();

    private final ObjectMapper MAPPER = new ObjectMapper();

    private long partyId = 1l;

    private String partyIdentifier = "1234567890";
    private long partyAddressId = 1l;
    private long partyContactDetailsId = 1l;

    private long partyDocumentId = 1l;

    private static long partyRiskId = 1l;

    private static long partyFatcaDetailsId = 1l;

    private static long partyMemosId = 1l;

    private static long partyGuardianId = 1l;
    private AutoCloseable closeable = mock(AutoCloseable.class);

    @BeforeEach
    void initService() {
        closeable = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    @Test
    void should_return_success_response_when_create_party_record() throws IOException {
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("PARTY_CREATED", "Party record created successfully!", PartyResponseCmd.builder().build())).build();

        doNothing().when(mockPartyService).validatePartyRecord(null, partyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.save(partyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/")
                .request()
                .post(Entity.json(partyRequestCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("PARTY_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).save(partyRequestCmd);
    }

    @Test
    void should_return_validation_error_when_wrong_input_passed_during_create_party_record() throws IOException {
        PartyRequestCmd partyRequestCmd = PartyRequestCmd.builder().party(CreatePartyCmd.builder().firstName(null).primaryMobileNumber("89898").type(null).build()).build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Wrong data input")).build();

        doNothing().when(mockPartyService).validatePartyRecord(null, partyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.save(partyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/")
                .request()
                .post(Entity.json(partyRequestCmd));

        assertEquals(HttpStatus.BAD_REQUEST_400, HttpStatus.UNPROCESSABLE_ENTITY_422, actualResponse.getStatus());
    }

    @Test
    void should_return_failure_response_when_exception_occurs_during_create_party_record() throws IOException {
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", "Error occurred while creating the record")).build();

        doNothing().when(mockPartyService).validatePartyRecord(null, partyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.save(partyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/")
                .request()
                .post(Entity.json(partyRequestCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
    }


    @Test
    void should_return_success_response_when_update_party_record() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("PARTY_UPDATED", "Party record updated successfully!", PartyResponseCmd.builder().build())).build();

        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update")
                .request()
                .put(Entity.json(updatePartyRequestCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("PARTY_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).update(updatePartyRequestCmd);
    }

    @Test
    void should_return_validation_error_when_wrong_input_passed_during_update_party_record() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Wrong data input")).build();
        updatePartyRequestCmd.setParty(UpdatePartyCmd.builder().partyIdentifier(null).build());

        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update")
                .request()
                .put(Entity.json(updatePartyRequestCmd));

        assertEquals(HttpStatus.BAD_REQUEST_400, HttpStatus.UNPROCESSABLE_ENTITY_422, actualResponse.getStatus());
    }

    @Test
    void should_return_failure_response_when_exception_occurs_during_update_party_record() throws IOException {
        UpdatePartyRequestCmd updatePartyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/PartyUpdate.json"), UpdatePartyRequestCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", "Error occurred while updating the record")).build();

        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update")
                .request()
                .put(Entity.json(updatePartyRequestCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
    }

    @Test
    void should_return_success_when_party_is_found() {
        PartyResponseCmd partyResponseCmd = PartyResponseCmd.builder().party(ViewPartyCmd.builder().partyIdentifier("1234567890").firstName("Ramesh").build()).build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("RECORD_FOUND", "Record found !", partyResponseCmd)).build();

        when(mockPartyService.getPartyDetails(Arrays.asList(partyIdentifier))).thenReturn(apiResponse);
        Response response = resourceTestClient.target("/party")
                .queryParam("id", partyIdentifier)
                .request().get();

        assertThat(response.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("RECORD_FOUND");
        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(mockPartyService, times(1)).getPartyDetails(Arrays.asList(partyIdentifier));

    }


    @Test
    void should_return_success_response_when_soft_delete_success() {
        UpdatePartyRecordForSoftDeleteRequest softDeleteRequest = UpdatePartyRecordForSoftDeleteRequest.builder().partyIdentifier("1234567890").build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("SOFT_DELETE", "Soft delete operation perform successfully!", null)).build();

        when(mockPartyService.updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest)).thenReturn(apiResponse);
        Response response = resourceTestClient.target("/party/soft-delete")
                .request().put(Entity.json(softDeleteRequest));

        assertThat(response.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("SOFT_DELETE");
        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(mockPartyService, times(1)).updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest);
    }

    @Test
    void should_return_error_response_when_soft_delete_failed() {
        UpdatePartyRecordForSoftDeleteRequest softDeleteRequest = UpdatePartyRecordForSoftDeleteRequest.builder().partyIdentifier("1234567890").build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Error Occurred While Performing Soft delete!")).build();

        when(mockPartyService.updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest)).thenReturn(apiResponse);
        Response response = resourceTestClient.target("/party/soft-delete")
                .request().put(Entity.json(softDeleteRequest));

        assertThat(response.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, response.getStatus());
        verify(mockPartyService, times(1)).updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest);
    }

    @Test
    void should_return_failure_when_exception_occur_during_soft_delete() {
        UpdatePartyRecordForSoftDeleteRequest softDeleteRequest = UpdatePartyRecordForSoftDeleteRequest.builder().partyIdentifier("1234567890").build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/soft-delete")
                .request().put(Entity.json(softDeleteRequest));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updatePartyRecordForSoftDeleteByPartyId(softDeleteRequest);
    }

    @Test
    void should_return_success_when_address_is_saved() throws IOException {
        CreateAddressCmd createAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), CreateAddressCmd.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("ADDRESS_CREATED", "Address created successfully!!", null)).build();
        when(mockPartyService.saveAddress(Collections.singleton(createAddressCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address")
                .request()
                .post(Entity.json(Collections.singleton(createAddressCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADDRESS_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveAddress(Collections.singleton(createAddressCmd), partyIdentifier);
    }

    @Test
    void should_return_error_when_address_is_not_saved() throws IOException {
        CreateAddressCmd createAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), CreateAddressCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Party Id not found to create address!!")).build();

        when(mockPartyService.saveAddress(Collections.singleton(createAddressCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address")
                .request()
                .post(Entity.json(Collections.singleton(createAddressCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveAddress(Collections.singleton(createAddressCmd), partyIdentifier);
    }

    @Test
    void should_return_failure_when_exception_occur_during_address_is_saved() throws IOException {
        CreateAddressCmd createAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateAddress.json"), CreateAddressCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.saveAddress(Collections.singleton(createAddressCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address")
                .request()
                .post(Entity.json(Collections.singleton(createAddressCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveAddress(Collections.singleton(createAddressCmd), partyIdentifier);
    }

    @Test
    void should_return_success_when_address_is_update() throws IOException {
        UpdateAddressCmd updateAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateAddress.json"), UpdateAddressCmd.class);
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADDRESS_UPDATED", "Address updated successfully!!", null)).build();

        when(mockPartyService.updateAddress(partyIdentifier, Long.valueOf(updateAddressCmd.getPartyAddressId()), updateAddressCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + updateAddressCmd.getPartyAddressId())
                .request()
                .put(Entity.json(updateAddressCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADDRESS_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateAddress(partyIdentifier, Long.valueOf(updateAddressCmd.getPartyAddressId()), updateAddressCmd);
    }

    @Test
    void should_return_error_when_address_is_not_updated() throws IOException {
        UpdateAddressCmd updateAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateAddress.json"), UpdateAddressCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ADDRESS_UPDATE_ERROR", "Party Id not found to update address!!")).build();


        when(mockPartyService.updateAddress(partyIdentifier, Long.valueOf(updateAddressCmd.getPartyAddressId()), updateAddressCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + updateAddressCmd.getPartyAddressId())
                .request()
                .put(Entity.json(updateAddressCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADDRESS_UPDATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateAddress(partyIdentifier, Long.valueOf(updateAddressCmd.getPartyAddressId()), updateAddressCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_during_address_is_update() throws IOException {
        UpdateAddressCmd updateAddressCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateAddress.json"), UpdateAddressCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.updateAddress(partyIdentifier, Long.valueOf(updateAddressCmd.getPartyAddressId()), updateAddressCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + updateAddressCmd.getPartyAddressId())
                .request()
                .put(Entity.json(updateAddressCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateAddress(partyIdentifier, Long.valueOf(updateAddressCmd.getPartyAddressId()), updateAddressCmd);
    }

    @Test
    void should_return_success_when_fetch_address() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADDRESS_FETCH", "Address fetch successfully!!", null)).build();

        when(mockPartyService.fetchAddressDetails(partyIdentifier, partyAddressId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + partyAddressId)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADDRESS_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchAddressDetails(partyIdentifier, partyAddressId);
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_address() throws IOException {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for address!!")).build();


        when(mockPartyService.fetchAddressDetails(partyIdentifier, partyAddressId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + partyAddressId)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchAddressDetails(partyIdentifier, partyAddressId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_address() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchAddressDetails(partyIdentifier, partyAddressId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + partyAddressId)
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchAddressDetails(partyIdentifier, partyAddressId);
    }

    @Test
    void should_return_success_when_fetch_all_address() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADDRESS_FETCH", "Address fetch successfully!!", null)).build();

        when(mockPartyService.fetchAddressDetails(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADDRESS_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchAddressDetails(partyIdentifier, null);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_all_address() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchAddressDetails(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/")
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchAddressDetails(partyIdentifier, null);
    }

    @Test
    void should_return_success_when_address_deleted() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADDRESS_DELETE", "Address delete successfully!!", null)).build();

        when(mockPartyService.deleteAddressByPassedParameter(partyIdentifier, partyAddressId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + partyAddressId)
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADDRESS_DELETE");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteAddressByPassedParameter(partyIdentifier, partyAddressId);
    }

    @Test
    void should_return_error_when_address_is_not_deleted() throws IOException {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("ERROR", "No address record found for address!!")).build();

        when(mockPartyService.deleteAddressByPassedParameter(partyIdentifier, partyAddressId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + partyAddressId)
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteAddressByPassedParameter(partyIdentifier, partyAddressId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_address_deleted() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.deleteAddressByPassedParameter(partyIdentifier, partyAddressId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/address/" + partyAddressId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteAddressByPassedParameter(partyIdentifier, partyAddressId);
    }

    @Test
    void should_return_success_response_when_dedupe_verification_is_done() throws IOException {
        DedupeFieldRequestCmd dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().countryCode("+91").mobileNumber("9898989898").build();
        Response response = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DEDUPE", null, null)).build();

        when(mockPartyService.checkDedupe(dedupeFieldRequestCmd)).thenReturn(response);
        Response actualResponse = resourceTestClient.target("/party/dedupe")
                .request()
                .post(Entity.json(dedupeFieldRequestCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DEDUPE");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).checkDedupe(dedupeFieldRequestCmd);
    }

    @Test
    void should_return_error_response_when_dedupe_verification_is_not_done() throws IOException {
        DedupeFieldRequestCmd dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().countryCode("+91").mobileNumber("9898989898").build();
        Response response = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", null)).build();

        when(mockPartyService.checkDedupe(dedupeFieldRequestCmd)).thenReturn(response);
        Response actualResponse = resourceTestClient.target("/party/dedupe")
                .request()
                .post(Entity.json(dedupeFieldRequestCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).checkDedupe(dedupeFieldRequestCmd);
    }

    @Test
    void should_return_failure_response_when_exception_occur_during_dedupe_verification() throws IOException {
        DedupeFieldRequestCmd dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().countryCode("+91").mobileNumber("9898989898").build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.checkDedupe(dedupeFieldRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/dedupe")
                .request()
                .post(Entity.json(dedupeFieldRequestCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).checkDedupe(dedupeFieldRequestCmd);
    }

    @Test
    void should_return_success_when_contact_is_saved() throws IOException {
        CreateContactDetailsCmd createContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("CONTACT_DETAILS_CREATED", "Contact created successfully!!", null)).build();
        when(mockPartyService.saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts")
                .request()
                .post(Entity.json(Collections.singleton(createContactDetailsCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONTACT_DETAILS_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier);
    }

    @Test
    void should_return_error_when_contact_is_not_saved() throws IOException {
        CreateContactDetailsCmd createContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("CONTACT_DETAILS_CREATE_ERROR", "Party Id not found to create contact!!")).build();
        when(mockPartyService.saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts")
                .request()
                .post(Entity.json(Collections.singleton(createContactDetailsCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONTACT_DETAILS_CREATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier);
    }

    @Test
    void should_return_failure_when_exception_occur_during_contact_is_saved() throws IOException {
        CreateContactDetailsCmd createContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateContact.json"), CreateContactDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        when(mockPartyService.saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts")
                .request()
                .post(Entity.json(Collections.singleton(createContactDetailsCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveContacts(Collections.singleton(createContactDetailsCmd), partyIdentifier);
    }

    @Test
    void should_return_success_when_contact_is_update() throws IOException {
        UpdateContactDetailsCmd updateContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONTACT_DETAILS_UPDATED", "Contact updated successfully!!", null)).build();

        when(mockPartyService.updateContacts(partyIdentifier, Long.valueOf(updateContactDetailsCmd.getPartyContactDetailsId()), updateContactDetailsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts/" + updateContactDetailsCmd.getPartyContactDetailsId())
                .request()
                .put(Entity.json(updateContactDetailsCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONTACT_DETAILS_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateContacts(partyIdentifier, Long.valueOf(updateContactDetailsCmd.getPartyContactDetailsId()), updateContactDetailsCmd);
    }

    @Test
    void should_return_error_when_contact_is_not_updated() throws IOException {
        UpdateContactDetailsCmd updateContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "Party Id not found to update contact!!")).build();

        when(mockPartyService.updateContacts(partyIdentifier, Long.valueOf(updateContactDetailsCmd.getPartyContactDetailsId()), updateContactDetailsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts/" + updateContactDetailsCmd.getPartyContactDetailsId())
                .request()
                .put(Entity.json(updateContactDetailsCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateContacts(partyIdentifier, Long.valueOf(updateContactDetailsCmd.getPartyContactDetailsId()), updateContactDetailsCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_during_contact_is_update() throws IOException {
        UpdateContactDetailsCmd updateContactDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateContact.json"), UpdateContactDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.updateContacts(partyIdentifier, Long.valueOf(updateContactDetailsCmd.getPartyContactDetailsId()), updateContactDetailsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts/" + updateContactDetailsCmd.getPartyContactDetailsId())
                .request()
                .put(Entity.json(updateContactDetailsCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateContacts(partyIdentifier, Long.valueOf(updateContactDetailsCmd.getPartyContactDetailsId()), updateContactDetailsCmd);
    }

    @Test
    void should_return_success_when_fetch_contact() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONTACT_FETCH", "Contact details fetch successfully!!", null)).build();

        when(mockPartyService.fetchContactDetails(partyIdentifier, partyContactDetailsId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts/" + partyContactDetailsId)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONTACT_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchContactDetails(partyIdentifier, partyContactDetailsId);
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_contact() throws IOException {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for contact!!")).build();

        when(mockPartyService.fetchContactDetails(partyIdentifier, partyContactDetailsId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts/" + partyContactDetailsId)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchContactDetails(partyIdentifier, partyContactDetailsId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_contact() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchContactDetails(partyIdentifier, partyContactDetailsId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts/" + partyContactDetailsId)
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchContactDetails(partyIdentifier, partyContactDetailsId);
    }

    @Test
    void should_return_success_when_fetch_all_contacts() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONTACT_FETCH", "Contact details fetch successfully!!", null)).build();

        when(mockPartyService.fetchContactDetails(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONTACT_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchContactDetails(partyIdentifier, null);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_all_contacts() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchContactDetails(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contacts/")
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchContactDetails(partyIdentifier, null);
    }

    @Test
    void should_return_success_when_contact_deleted() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONTACT_DELETE", "Contact details deleted successfully!!", null)).build();


        when(mockPartyService.deleteContact(partyIdentifier, partyContactDetailsId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contact/" + partyContactDetailsId)
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("CONTACT_DELETE");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteContact(partyIdentifier, partyContactDetailsId);
    }

    @Test
    void should_return_error_when_contact_is_not_deleted() {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for contact!!")).build();

        when(mockPartyService.deleteContact(partyIdentifier, partyContactDetailsId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contact/" + partyContactDetailsId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteContact(partyIdentifier, partyContactDetailsId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_contact_delete() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.deleteContact(partyIdentifier, partyContactDetailsId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/contact/" + partyContactDetailsId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteContact(partyIdentifier, partyContactDetailsId);
    }

    @Test
    void should_return_success_when_document_is_saved() throws IOException {
        CreateDocumentCmd createDocumentCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateDocument.json"), CreateDocumentCmd.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("DOCUMENT_CREATED", "Document created successfully!!", null)).build();
        when(mockPartyService.saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents")
                .request()
                .post(Entity.json(Collections.singleton(createDocumentCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DOCUMENT_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier);
    }

    @Test
    void should_return_error_when_document_is_not_saved() throws IOException {
        CreateDocumentCmd createDocumentCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateDocument.json"), CreateDocumentCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("DOCUMENT_CREATE_ERROR", "Party Id not found to create document!!")).build();
        when(mockPartyService.saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents")
                .request()
                .post(Entity.json(Collections.singleton(createDocumentCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DOCUMENT_CREATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier);
    }

    @Test
    void should_return_failure_when_exception_occur_during_document_is_saved() throws IOException {
        CreateDocumentCmd createDocumentCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateDocument.json"), CreateDocumentCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        when(mockPartyService.saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents")
                .request()
                .post(Entity.json(Collections.singleton(createDocumentCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveDocument(Collections.singleton(createDocumentCmd), partyIdentifier);
    }

    @Test
    void should_return_success_when_document_is_update() throws IOException {
        UpdateDocumentsCmd updateDocumentsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateDocument.json"), UpdateDocumentsCmd.class);
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DOCUMENT_DETAILS_UPDATED", "Document updated successfully!!", null)).build();

        when(mockPartyService.updateDocuments(partyIdentifier, Long.valueOf(updateDocumentsCmd.getPartyDocumentId()), updateDocumentsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents/" + updateDocumentsCmd.getPartyDocumentId())
                .request()
                .put(Entity.json(updateDocumentsCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DOCUMENT_DETAILS_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateDocuments(partyIdentifier, Long.valueOf(updateDocumentsCmd.getPartyDocumentId()), updateDocumentsCmd);
    }

    @Test
    void should_return_error_when_document_is_not_updated() throws IOException {
        UpdateDocumentsCmd updateDocumentsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateDocument.json"), UpdateDocumentsCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("DOCUMENT_DETAILS_UPDATE_ERROR", "Party Id not found to update document!!")).build();

        when(mockPartyService.updateDocuments(partyIdentifier, Long.valueOf(updateDocumentsCmd.getPartyDocumentId()), updateDocumentsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents/" + updateDocumentsCmd.getPartyDocumentId())
                .request()
                .put(Entity.json(updateDocumentsCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DOCUMENT_DETAILS_UPDATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateDocuments(partyIdentifier, Long.valueOf(updateDocumentsCmd.getPartyDocumentId()), updateDocumentsCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_during_document_update() throws IOException {
        UpdateDocumentsCmd updateDocumentsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateDocument.json"), UpdateDocumentsCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.updateDocuments(partyIdentifier, Long.valueOf(updateDocumentsCmd.getPartyDocumentId()), updateDocumentsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents/" + updateDocumentsCmd.getPartyDocumentId())
                .request()
                .put(Entity.json(updateDocumentsCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateDocuments(partyIdentifier, Long.valueOf(updateDocumentsCmd.getPartyDocumentId()), updateDocumentsCmd);
    }

    @Test
    void should_return_success_when_fetch_all_documents() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DOCUMENTS_FETCH", "document fetch successfully!!", null)).build();

        when(mockPartyService.fetchDocuments(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DOCUMENTS_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchDocuments(partyIdentifier, null);
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_all_documents() {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for document!!")).build();

        when(mockPartyService.fetchDocuments(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchDocuments(partyIdentifier, null);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_all_documents() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchDocuments(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents/")
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchDocuments(partyIdentifier, null);
    }

    @Test
    void should_return_success_when_fetch_document() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DOCUMENTS_FETCH", "Contact details fetch successfully!!", null)).build();

        when(mockPartyService.fetchDocuments(partyIdentifier, partyDocumentId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents/" + partyDocumentId)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DOCUMENTS_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchDocuments(partyIdentifier, partyDocumentId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_document() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchDocuments(partyIdentifier, partyDocumentId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/documents/" + partyDocumentId)
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchDocuments(partyIdentifier, partyDocumentId);
    }

    @Test
    void should_return_success_when_document_deleted() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DOCUMENT_DELETE", "Document details deleted successfully!!", null)).build();

        when(mockPartyService.deleteDocuments(partyIdentifier, partyDocumentId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/document/" + partyContactDetailsId)
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DOCUMENT_DELETE");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteDocuments(partyIdentifier, partyDocumentId);
    }

    @Test
    void should_return_error_when_document_is_not_deleted() {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for document!!")).build();

        when(mockPartyService.deleteDocuments(partyIdentifier, partyDocumentId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/document/" + partyDocumentId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteDocuments(partyIdentifier, partyDocumentId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_document_delete() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.deleteDocuments(partyIdentifier, partyDocumentId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/document/" + partyContactDetailsId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteDocuments(partyIdentifier, partyDocumentId);
    }

    @Test
    void should_return_success_when_risk_is_saved() throws IOException {
        CreateRisksCmd createRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateRisk.json"), CreateRisksCmd.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("RISK_CREATED", "Risk created successfully!!", null)).build();
        when(mockPartyService.saveRisk(Collections.singleton(createRisksCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks")
                .request()
                .post(Entity.json(Collections.singleton(createRisksCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("RISK_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveRisk(Collections.singleton(createRisksCmd), partyIdentifier);
    }

    @Test
    void should_return_error_when_risk_is_not_saved() throws IOException {
        CreateRisksCmd createRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateRisk.json"), CreateRisksCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("RISK_CREATE_ERROR", "Party Id not found to create risk!!")).build();
        when(mockPartyService.saveRisk(Collections.singleton(createRisksCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks")
                .request()
                .post(Entity.json(Collections.singleton(createRisksCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("RISK_CREATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveRisk(Collections.singleton(createRisksCmd), partyIdentifier);
    }

    @Test
    void should_return_failure_when_exception_occur_during_risk_is_saved() throws IOException {
        CreateRisksCmd createRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateRisk.json"), CreateRisksCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.saveRisk(Collections.singleton(createRisksCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks")
                .request()
                .post(Entity.json(Collections.singleton(createRisksCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveRisk(Collections.singleton(createRisksCmd), partyIdentifier);
    }

    @Test
    void should_return_success_when_risk_is_update() throws IOException {
        UpdateRisksCmd updateRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateRisk.json"), UpdateRisksCmd.class);
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("RISK_DETAILS_UPDATED", "Risk updated successfully!!", null)).build();

        when(mockPartyService.updateRisk(partyIdentifier, Long.valueOf(updateRisksCmd.getPartyRiskId()), updateRisksCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks/" + updateRisksCmd.getPartyRiskId())
                .request()
                .put(Entity.json(updateRisksCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("RISK_DETAILS_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateRisk(partyIdentifier, Long.valueOf(updateRisksCmd.getPartyRiskId()), updateRisksCmd);
    }

    @Test
    void should_return_error_when_risk_is_not_updated() throws IOException {
        UpdateRisksCmd updateRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateRisk.json"), UpdateRisksCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("RISK_DETAILS_UPDATE_ERROR", "Party Id not found to update risk!!")).build();
        when(mockPartyService.updateRisk(partyIdentifier, Long.valueOf(updateRisksCmd.getPartyRiskId()), updateRisksCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks/" + updateRisksCmd.getPartyRiskId())
                .request()
                .put(Entity.json(updateRisksCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("RISK_DETAILS_UPDATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateRisk(partyIdentifier, Long.valueOf(updateRisksCmd.getPartyRiskId()), updateRisksCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_during_risk_is_update() throws IOException {
        UpdateRisksCmd updateRisksCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateRisk.json"), UpdateRisksCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.updateRisk(partyIdentifier, Long.valueOf(updateRisksCmd.getPartyRiskId()), updateRisksCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks/" + updateRisksCmd.getPartyRiskId())
                .request()
                .put(Entity.json(updateRisksCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateRisk(partyIdentifier, Long.valueOf(updateRisksCmd.getPartyRiskId()), updateRisksCmd);
    }

    @Test
    void should_return_success_when_fetch_all_risks() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("RISK_FETCH", "Risk fetch successfully!!", null)).build();

        when(mockPartyService.fetchRisks(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("RISK_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchRisks(partyIdentifier, null);
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_all_risks() throws IOException {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for risk!!")).build();

        when(mockPartyService.fetchRisks(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchRisks(partyIdentifier, null);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_all_risks() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchRisks(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks/")
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchRisks(partyIdentifier, null);
    }

    @Test
    void should_return_success_when_fetch_risk() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("RISK_FETCH", "Risk fetch successfully!!", null)).build();

        when(mockPartyService.fetchRisks(partyIdentifier, partyDocumentId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks/" + partyRiskId)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("RISK_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchRisks(partyIdentifier, partyDocumentId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_risk() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchRisks(partyIdentifier, partyDocumentId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/risks/" + partyRiskId)
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchRisks(partyIdentifier, partyDocumentId);
    }

    @Test
    void should_return_success_when_fatca_is_saved() throws IOException {
        CreateFatcaDetailsCmd createFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateFatca.json"), CreateFatcaDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("FATCA_CREATED", "Fatca created successfully!!", null)).build();
        when(mockPartyService.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca")
                .request()
                .post(Entity.json(Collections.singleton(createFatcaDetailsCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("FATCA_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier);
    }

    @Test
    void should_return_error_when_fatca_is_not_saved() throws IOException {
        CreateFatcaDetailsCmd createFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateFatca.json"), CreateFatcaDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("FATCA_CREATE_ERROR", "Party Id not found to create fatca!!")).build();
        when(mockPartyService.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca")
                .request()
                .post(Entity.json(Collections.singleton(createFatcaDetailsCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("FATCA_CREATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fatca_is_saved() throws IOException {
        CreateFatcaDetailsCmd createFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateFatca.json"), CreateFatcaDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        when(mockPartyService.saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca")
                .request()
                .post(Entity.json(Collections.singleton(createFatcaDetailsCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveFatcaDetails(Collections.singleton(createFatcaDetailsCmd), partyIdentifier);
    }

    @Test
    void should_return_success_when_fatca_is_update() throws IOException {
        UpdateFatcaDetailsCmd updateFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateFatca.json"), UpdateFatcaDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("FATCA_DETAILS_UPDATED", "Fatca updated successfully!!", null)).build();

        when(mockPartyService.updateFatcaDetails(partyIdentifier, Long.valueOf(updateFatcaDetailsCmd.getPartyFatcaDetailsId()), updateFatcaDetailsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca/" + updateFatcaDetailsCmd.getPartyFatcaDetailsId())
                .request()
                .put(Entity.json(updateFatcaDetailsCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("FATCA_DETAILS_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateFatcaDetails(partyIdentifier, Long.valueOf(updateFatcaDetailsCmd.getPartyFatcaDetailsId()), updateFatcaDetailsCmd);
    }

    @Test
    void should_return_error_when_fatca_is_not_updated() throws IOException {
        UpdateFatcaDetailsCmd updateFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateFatca.json"), UpdateFatcaDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("FATCA_DETAILS_UPDATE_ERROR", "Party Id not found to update fatca!!")).build();

        when(mockPartyService.updateFatcaDetails(partyIdentifier, Long.valueOf(updateFatcaDetailsCmd.getPartyFatcaDetailsId()), updateFatcaDetailsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca/" + updateFatcaDetailsCmd.getPartyFatcaDetailsId())
                .request()
                .put(Entity.json(updateFatcaDetailsCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("FATCA_DETAILS_UPDATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateFatcaDetails(partyIdentifier, Long.valueOf(updateFatcaDetailsCmd.getPartyFatcaDetailsId()), updateFatcaDetailsCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fatca_update() throws IOException {
        UpdateFatcaDetailsCmd updateFatcaDetailsCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateFatca.json"), UpdateFatcaDetailsCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.updateFatcaDetails(partyIdentifier, Long.valueOf(updateFatcaDetailsCmd.getPartyFatcaDetailsId()), updateFatcaDetailsCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca/" + updateFatcaDetailsCmd.getPartyFatcaDetailsId())
                .request()
                .put(Entity.json(updateFatcaDetailsCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateFatcaDetails(partyIdentifier, Long.valueOf(updateFatcaDetailsCmd.getPartyFatcaDetailsId()), updateFatcaDetailsCmd);
    }

    @Test
    void should_return_success_when_fetch_all_fatca() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("FATCA_FETCH", "Fatca fetch successfully!!", null)).build();

        when(mockPartyService.fetchFatcaDetails(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("FATCA_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchFatcaDetails(partyIdentifier, null);
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_all_fatca() throws IOException {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for fatca!!")).build();

        when(mockPartyService.fetchFatcaDetails(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchFatcaDetails(partyIdentifier, null);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_all_fatca() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchFatcaDetails(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca/")
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchFatcaDetails(partyIdentifier, null);
    }

    @Test
    void should_return_success_when_fetch_fatca() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("FATCA_FETCH", "Fatca fetch successfully!!", null)).build();

        when(mockPartyService.fetchFatcaDetails(partyIdentifier, partyFatcaDetailsId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca/" + partyFatcaDetailsId)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("FATCA_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchFatcaDetails(partyIdentifier, partyFatcaDetailsId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_fatca() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchFatcaDetails(partyIdentifier, partyFatcaDetailsId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/fatca/" + partyFatcaDetailsId)
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchFatcaDetails(partyIdentifier, partyFatcaDetailsId);
    }

    @Test
    void should_return_success_when_memo_is_saved() throws IOException {
        CreateMemosCmd createMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateMemo.json"), CreateMemosCmd.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("MEMO_CREATED", "Memo created successfully!!", null)).build();
        when(mockPartyService.saveMemos(Collections.singleton(createMemosCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos")
                .request()
                .post(Entity.json(Collections.singleton(createMemosCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MEMO_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveMemos(Collections.singleton(createMemosCmd), partyIdentifier);
    }

    @Test
    void should_return_error_when_memo_is_not_saved() throws IOException {
        CreateMemosCmd createMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateMemo.json"), CreateMemosCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("MEMO_CREATE_ERROR", "Party Id not found to create memo!!")).build();
        when(mockPartyService.saveMemos(Collections.singleton(createMemosCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos")
                .request()
                .post(Entity.json(Collections.singleton(createMemosCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("MEMO_CREATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveMemos(Collections.singleton(createMemosCmd), partyIdentifier);
    }

    @Test
    void should_return_failure_when_exception_occur_during_memo_is_saved() throws IOException {
        CreateMemosCmd createMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateMemo.json"), CreateMemosCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        when(mockPartyService.saveMemos(Collections.singleton(createMemosCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos")
                .request()
                .post(Entity.json(Collections.singleton(createMemosCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveMemos(Collections.singleton(createMemosCmd), partyIdentifier);
    }

    @Test
    void should_return_success_when_memo_is_update() throws IOException {
        UpdateMemosCmd updateMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateMemo.json"), UpdateMemosCmd.class);
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MEMOS_UPDATED", "Memo updated successfully!!", null)).build();

        when(mockPartyService.updateMemos(partyIdentifier, Long.valueOf(updateMemosCmd.getPartyMemoId()), updateMemosCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos/" + updateMemosCmd.getPartyMemoId())
                .request()
                .put(Entity.json(updateMemosCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MEMOS_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateMemos(partyIdentifier, Long.valueOf(updateMemosCmd.getPartyMemoId()), updateMemosCmd);
    }

    @Test
    void should_return_error_when_memo_is_not_updated() throws IOException {
        UpdateMemosCmd updateMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateMemo.json"), UpdateMemosCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("MEMOS_UPDATE_ERROR", "Party Id not found to update memo!!")).build();

        when(mockPartyService.updateMemos(partyIdentifier, Long.valueOf(updateMemosCmd.getPartyMemoId()), updateMemosCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos/" + updateMemosCmd.getPartyMemoId())
                .request()
                .put(Entity.json(updateMemosCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MEMOS_UPDATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateMemos(partyIdentifier, Long.valueOf(updateMemosCmd.getPartyMemoId()), updateMemosCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_during_memo_is_update() throws IOException {
        UpdateMemosCmd updateMemosCmd = MAPPER.readValue(getClass().getResource("/fixtures/UpdateMemo.json"), UpdateMemosCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.updateMemos(partyIdentifier, Long.valueOf(updateMemosCmd.getPartyMemoId()), updateMemosCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos/" + updateMemosCmd.getPartyMemoId())
                .request()
                .put(Entity.json(updateMemosCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).updateMemos(partyIdentifier, Long.valueOf(updateMemosCmd.getPartyMemoId()), updateMemosCmd);
    }

    @Test
    void should_return_success_when_fetch_all_memos() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MEMO_FETCH", "Memo fetch successfully!!", null)).build();

        when(mockPartyService.fetchMemos(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MEMO_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchMemos(partyIdentifier, null);
    }

    @Test
    void should_return_error_when_no_record_found_during_fetch_all_memos() throws IOException {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for memo!!")).build();

        when(mockPartyService.fetchMemos(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchMemos(partyIdentifier, null);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_all_memos() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchMemos(partyIdentifier, null)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos/")
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchMemos(partyIdentifier, null);
    }

    @Test
    void should_return_success_when_fetch_memo() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MEMO_FETCH", "Memo fetch successfully!!", null)).build();

        when(mockPartyService.fetchMemos(partyIdentifier, partyMemosId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos/" + partyMemosId)
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("MEMO_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchMemos(partyIdentifier, partyMemosId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_fetch_memo() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.fetchMemos(partyIdentifier, partyMemosId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/memos/" + partyMemosId)
                .request().get();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchMemos(partyIdentifier, partyMemosId);
    }

    @Test
    void should_return_success_when_search_party() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("PARTY_SEARCH", "Party search successfully!!", null)).build();
        PartyDistinctiveSearchCmd partyDistinctiveSearchCmd = PartyDistinctiveSearchCmd.builder().partyIdentifier("1234567890").firstName("Ramesh").mobileNumber("9999999999").build();

        when(mockPartyService.getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/search")
                .queryParam("sortingOrder", "asc")
                .queryParam("isDelete", true)
                .queryParam("limit", 100)
                .queryParam("offset", 1)
                .request()
                .post(Entity.json(partyDistinctiveSearchCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("PARTY_SEARCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1);
    }

    @Test
    void should_return_error_when_no_record_found_during_search_party() {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_SEARCH_RECORD_FOUND", "Party data not found!!")).build();
        PartyDistinctiveSearchCmd partyDistinctiveSearchCmd = PartyDistinctiveSearchCmd.builder().partyIdentifier("1234567890").firstName("Ramesh").mobileNumber("9999999999").build();
        when(mockPartyService.getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/search")
                .queryParam("sortingOrder", "asc")
                .queryParam("isDelete", true)
                .queryParam("limit", 100)
                .queryParam("offset", 1)
                .request()
                .post(Entity.json(partyDistinctiveSearchCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("NO_SEARCH_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1);
    }

    @Test
    void should_return_failure_when_exception_occur_during_search_party() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        PartyDistinctiveSearchCmd partyDistinctiveSearchCmd = PartyDistinctiveSearchCmd.builder().partyIdentifier("1234567890").firstName("Ramesh").mobileNumber("9999999999").build();
        when(mockPartyService.getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/search")
                .queryParam("sortingOrder", "asc")
                .queryParam("isDelete", true)
                .queryParam("limit", 100)
                .queryParam("offset", 1)
                .request()
                .post(Entity.json(partyDistinctiveSearchCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).getPartyDetailsByPassedParameter("asc", true, partyDistinctiveSearchCmd, 100, 1);
    }

    @Test
    void should_return_success_response_when_party_status_updated() {
        PartyStatusUpdateCmd partyStatusUpdateCmd = PartyStatusUpdateCmd.builder().build().partyIdentifier("1234567890").status(PartyStatusUpdateCmd.StatusEnum.ACTIVE);
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyStatusUpdateCmd.getPartyIdentifier()).status(UpdatePartyCmd.StatusEnum.fromString(partyStatusUpdateCmd.getStatus().name())).build()).build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("PARTY_UPDATED", "Party record updated successfully!", PartyResponseCmd.builder().build())).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/status")
                .request()
                .put(Entity.json(partyStatusUpdateCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("PARTY_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).update(updatePartyRequestCmd);
    }


    @Test
    void should_return_validation_error_response_when_party_status_not_update() {
        PartyStatusUpdateCmd partyStatusUpdateCmd = PartyStatusUpdateCmd.builder().build().partyIdentifier(null).status(PartyStatusUpdateCmd.StatusEnum.ACTIVE);
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyStatusUpdateCmd.getPartyIdentifier()).status(UpdatePartyCmd.StatusEnum.fromString(partyStatusUpdateCmd.getStatus().name())).build()).build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "ERROR!")).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/status")
                .request()
                .put(Entity.json(partyStatusUpdateCmd));
        assertEquals(HttpStatus.BAD_REQUEST_400, HttpStatus.UNPROCESSABLE_ENTITY_422, actualResponse.getStatus());
    }

    @Test
    void should_return_failure_when_exception_occur_during_party_status_updated() {
        PartyStatusUpdateCmd partyStatusUpdateCmd = PartyStatusUpdateCmd.builder().build().partyIdentifier("1234567890").status(PartyStatusUpdateCmd.StatusEnum.ACTIVE);
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyStatusUpdateCmd.getPartyIdentifier()).status(UpdatePartyCmd.StatusEnum.fromString(partyStatusUpdateCmd.getStatus().name())).build()).build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/status")
                .request()
                .put(Entity.json(partyStatusUpdateCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).update(updatePartyRequestCmd);
    }

    @Test
    void should_return_success_response_when_party_primary_email_updated() {
        PartyEmailUpdateCmd partyEmailUpdateCmd = PartyEmailUpdateCmd.builder().build().partyIdentifier("1234567890").primaryEmail("test@gmail.com");
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyEmailUpdateCmd.getPartyIdentifier()).primaryEmail(partyEmailUpdateCmd.getPrimaryEmail()).build()).build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("PARTY_UPDATED", "Party record updated successfully!", PartyResponseCmd.builder().build())).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/email")
                .request()
                .put(Entity.json(partyEmailUpdateCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("PARTY_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).update(updatePartyRequestCmd);
    }


    @Test
    void should_return_validation_error_response_when_party_primary_email_not_update() {
        PartyEmailUpdateCmd partyEmailUpdateCmd = PartyEmailUpdateCmd.builder().build().partyIdentifier(null).primaryEmail("test@gmail.com");
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyEmailUpdateCmd.getPartyIdentifier()).primaryEmail(partyEmailUpdateCmd.getPrimaryEmail()).build()).build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "ERROR!")).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/email")
                .request()
                .put(Entity.json(partyEmailUpdateCmd));
        assertEquals(HttpStatus.BAD_REQUEST_400, HttpStatus.UNPROCESSABLE_ENTITY_422, actualResponse.getStatus());
    }

    @Test
    void should_return_failure_when_exception_occur_during_party_primary_email_update() {
        PartyEmailUpdateCmd partyEmailUpdateCmd = PartyEmailUpdateCmd.builder().build().partyIdentifier("1234567890").primaryEmail("test@gmail.com");
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyEmailUpdateCmd.getPartyIdentifier()).primaryEmail(partyEmailUpdateCmd.getPrimaryEmail()).build()).build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/email")
                .request()
                .put(Entity.json(partyEmailUpdateCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).update(updatePartyRequestCmd);
    }

    @Test
    void should_return_success_response_when_party_primary_mobile_updated() {
        PartyMobileUpdateCmd partyMobileUpdateCmd = PartyMobileUpdateCmd.builder().build().partyIdentifier("1234567890").primaryMobileNumber("9990601876");
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyMobileUpdateCmd.getPartyIdentifier()).primaryMobileNumber(partyMobileUpdateCmd.getPrimaryMobileNumber()).build()).build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("PARTY_UPDATED", "Party record updated successfully!", PartyResponseCmd.builder().build())).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/mobilenumber")
                .request()
                .put(Entity.json(partyMobileUpdateCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("PARTY_UPDATED");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).update(updatePartyRequestCmd);
    }


    @Test
    void should_return_validation_error_response_when_party_primary_mobile_not_update() throws IOException {
        PartyEmailUpdateCmd partyEmailUpdateCmd = PartyEmailUpdateCmd.builder().build().partyIdentifier(null).primaryEmail("test@gmail.com");
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyEmailUpdateCmd.getPartyIdentifier()).primaryEmail(partyEmailUpdateCmd.getPrimaryEmail()).build()).build();
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("ERROR", "ERROR!")).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/mobile")
                .request()
                .put(Entity.json(partyEmailUpdateCmd));
        assertEquals(HttpStatus.BAD_REQUEST_400, HttpStatus.UNPROCESSABLE_ENTITY_422, actualResponse.getStatus());
    }

    @Test
    void should_return_failure_when_exception_occur_during_party_primary_mobile_update() {
        PartyMobileUpdateCmd partyMobileUpdateCmd = PartyMobileUpdateCmd.builder().build().partyIdentifier("1234567890").primaryMobileNumber("9990601876");
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyMobileUpdateCmd.getPartyIdentifier()).primaryMobileNumber(partyMobileUpdateCmd.getPrimaryMobileNumber()).build()).build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        doNothing().when(mockPartyService).validatePartyRecord(1, updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        when(mockPartyService.update(updatePartyRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/update/mobilenumber")
                .request()
                .put(Entity.json(partyMobileUpdateCmd));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).update(updatePartyRequestCmd);
    }

    @Test
    void should_return_success_when_advance_search_is_done() {
        AdvanceSearchRequestCmd advanceSearchRequestCmd = AdvanceSearchRequestCmd.builder().build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADVANCE_SEARCH", null, null)).build();
        when(mockPartyService.fetchDataUsingElasticSearch(advanceSearchRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/advance-search")
                .request()
                .post(Entity.json(advanceSearchRequestCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("ADVANCE_SEARCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchDataUsingElasticSearch(advanceSearchRequestCmd);
    }

    @Test
    void should_return_failure_when_exception_occur_advance_search() {
        AdvanceSearchRequestCmd advanceSearchRequestCmd = AdvanceSearchRequestCmd.builder().build();
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_NOT_FOUND_ERROR_CODE", null)).build();
        when(mockPartyService.fetchDataUsingElasticSearch(advanceSearchRequestCmd)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/advance-search")
                .request()
                .post(Entity.json(advanceSearchRequestCmd));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DATA_NOT_FOUND_ERROR_CODE");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchDataUsingElasticSearch(advanceSearchRequestCmd);
    }

    @Test
    void should_return_data_when_universal_search_api_success() {
        UniversalSearchRequest request = UniversalSearchRequest.builder().searchFor("TEST").build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("UNIVERSAL_SEARCH", "Total record(s) found : ".concat("1"), null)).build();

        when(mockPartyService.performUniversalSearchBasedOnUserData(request)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/universal-search")
                .request()
                .post(Entity.json(request));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("UNIVERSAL_SEARCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).performUniversalSearchBasedOnUserData(request);
    }

    @Test
    void should_return_error_when_invalid_date_passed_in_universal_search() {
        UniversalSearchRequest request = UniversalSearchRequest.builder().build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("UNIVERSAL_SEARCH", "Total record(s) found : ".concat("1"), null)).build();

        when(mockPartyService.performUniversalSearchBasedOnUserData(request)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/universal-search")
                .request()
                .post(Entity.json(request));

        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY_422, actualResponse.getStatus());
        verify(mockPartyService, times(0)).performUniversalSearchBasedOnUserData(request);
    }

    @Test
    void should_return_exception_when_error_occurred_in_universal_search_api_() {
        UniversalSearchRequest request = UniversalSearchRequest.builder().searchFor("TEST").build();
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("UNIVERSAL_SEARCH", "Total record(s) found : ".concat("1"), null)).build();

        when(mockPartyService.performUniversalSearchBasedOnUserData(request)).thenThrow(new ServerSideException("UNIVERSAL_SEARCH_ERROR", "Error occurred while performing universal search .Please contact the system administrator!", null));
        Response actualResponse = resourceTestClient.target("/party/universal-search")
                .request()
                .post(Entity.json(request));

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        assertThat(actualResponse.readEntity(ErrorResponseApi.class).getStatus().getCode()).isEqualTo("UNIVERSAL_SEARCH_ERROR");
        verify(mockPartyService, times(1)).performUniversalSearchBasedOnUserData(request);
    }

    @Test
    void should_return_success_when_fetch_all_xref() throws IOException {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("XREF_FETCH", "Xref record found!", null)).build();

        when(mockPartyService.fetchXref(partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/xref/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("XREF_FETCH");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchXref(partyIdentifier);
    }
    @Test
    void should_return_failure_when_exception_occur_during_fetch_all_xref() throws IOException {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR",  null)).build();

        when(mockPartyService.fetchXref(partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/xref/")
                .request().get();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).fetchXref(partyIdentifier);
    }
    @Test
    void should_return_success_when_guardian_is_saved() throws IOException {
        CreateGuardianCmd createGuardianCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateGuardian.json"), CreateGuardianCmd.class);
        Response apiResponse = Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("GUARDIAN_CREATED", "Guardian created successfully!!", null)).build();
        when(mockPartyService.saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/guardian")
                .request()
                .post(Entity.json(Collections.singleton(createGuardianCmd)));

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("GUARDIAN_CREATED");
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier);
    }

    @Test
    void should_return_success_when_guardian_is_not_saved() throws IOException {
        CreateGuardianCmd createGuardianCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateGuardian.json"), CreateGuardianCmd.class);
        Response apiResponse = Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("GUARDIAN_CREATE_ERROR", "Party Id not found to create guardian!!")).build();
        when(mockPartyService.saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/guardian")
                .request()
                .post(Entity.json(Collections.singleton(createGuardianCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("GUARDIAN_CREATE_ERROR");
        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier);
    }

    @Test
    void should_return_failure_when_exception_occur_during_guardian_is_saved() throws IOException {
        CreateGuardianCmd createGuardianCmd = MAPPER.readValue(getClass().getResource("/fixtures/CreateGuardian.json"), CreateGuardianCmd.class);
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();
        when(mockPartyService.saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/guardian")
                .request()
                .post(Entity.json(Collections.singleton(createGuardianCmd)));

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).saveGuardians(Collections.singleton(createGuardianCmd), partyIdentifier);
    }

    @Test
    void should_return_success_when_guardian_deleted() {
        Response apiResponse = Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("GUARDIAN_DELETE", "Guardian details deleted successfully!!", null)).build();

        when(mockPartyService.deleteGuardians(partyIdentifier, partyGuardianId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/guardian/" + partyGuardianId)
                .request().delete();

        assertThat(actualResponse.readEntity(SuccessResponseApi.class).getStatus().getCode()).isEqualTo("GUARDIAN_DELETE");
        assertEquals(HttpStatus.OK_200, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteGuardians(partyIdentifier, partyGuardianId);
    }

    @Test
    void should_return_success_when_guardian_is_not_deleted() {
        Response apiResponse = Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse("NO_RECORD_FOUND", "No record found for guardian!!")).build();

        when(mockPartyService.deleteGuardians(partyIdentifier, partyGuardianId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/guardian/" + partyGuardianId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("NO_RECORD_FOUND");
        assertEquals(HttpStatus.NOT_FOUND_404, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteGuardians(partyIdentifier, partyGuardianId);
    }

    @Test
    void should_return_failure_when_exception_occur_during_guardian_delete() {
        Response apiResponse = Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(buildErrorResponse("DATA_PERSIST_ERROR", null)).build();

        when(mockPartyService.deleteGuardians(partyIdentifier, partyGuardianId)).thenReturn(apiResponse);
        Response actualResponse = resourceTestClient.target("/party/" + partyIdentifier + "/guardian/" + partyGuardianId)
                .request().delete();

        assertThat(actualResponse.readEntity(ResponseApi.class).getStatus().getCode()).isEqualTo("DATA_PERSIST_ERROR");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).deleteGuardians(partyIdentifier, partyGuardianId);
    }

}

