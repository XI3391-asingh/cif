package com.finx.customerservice.resources;

import ch.qos.logback.classic.Level;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.customerservice.api.*;
import com.finx.customerservice.core.customer.services.PartyService;
import com.finx.customerservice.db.PartyTransactionRepository;
import com.finx.customerservice.resources.exceptionmappers.DatabaseExceptionMapper;
import com.finx.customerservice.resources.exceptionmappers.PartyExceptionMapper;
import io.dropwizard.jersey.errors.ErrorMessage;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.assertj.core.api.Assertions;
import org.eclipse.jetty.http.HttpStatus;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class PartyResourceTests {

    public static final String SEARCH_BY_MOBILE_ENDPOINT = "/party/searchbymobile/";

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }
    @Mock
    private AutoCloseable closeable;
    private final PartyService mockPartyService = mock(PartyService.class);
    private final ObjectMapper MAPPER = new ObjectMapper();

    private final PartyTransactionRepository partyTransactionRepository = mock(PartyTransactionRepository.class);

    @BeforeEach
    public void setUp() {
        closeable = MockitoAnnotations.openMocks(this);

    }

    @AfterEach
    void closeService() throws Exception {
        closeable.close();
    }

    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new PartyResource(mockPartyService))
                    .addProvider(() ->
                            new PartyExceptionMapper(new MetricRegistry()))
                    .addProvider(() ->
                            new DatabaseExceptionMapper(new MetricRegistry()))
                    .build();

    @Test
    void Should_Return_Response_With_Data_When_MobileNumber_Exists() {
        PartySearchCmd partySearchCmd = new PartySearchCmd();
        partySearchCmd.setMobileNumber("9990708912");
        PartyDetail partyRecord1 = PartyDetail.builder().firstName("Test1").primaryMobileNumber("9990708912").build();
        PartyDetail partyRecord2 = PartyDetail.builder().firstName("Test2").primaryMobileNumber("9990708912").build();
        PartySearchResponse partySearchResponse = new PartySearchResponse();
        partySearchResponse.setData(Arrays.asList(partyRecord1, partyRecord2));

        when(mockPartyService.findPartyDetailsByMobileNumber(partySearchCmd)).thenReturn(partySearchResponse);
        final Response response = resourceTestClient.target(SEARCH_BY_MOBILE_ENDPOINT)
                .request()
                .post(Entity.json(partySearchCmd));
        List<PartyDetail> parties = response.readEntity(PartySearchResponse.class).getData();

        assertThat(parties.get(0).getPrimaryMobileNumber()).isEqualTo("9990708912");
        assertThat(parties.size()).isGreaterThan(1);
        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(mockPartyService, times(1)).findPartyDetailsByMobileNumber(partySearchCmd);
    }

    @Test
    void Should_Return_Response_With_No_Data_When_MobileNumber_Not_Exists() {
        PartySearchCmd partySearchRequestDTO = new PartySearchCmd();
        partySearchRequestDTO.setMobileNumber("1111111111");
        PartySearchResponse partySearchResponseDTO = new PartySearchResponse();
        partySearchResponseDTO.setData(Collections.EMPTY_LIST);

        when(mockPartyService.findPartyDetailsByMobileNumber(partySearchRequestDTO)).thenReturn(partySearchResponseDTO);
        Response response = resourceTestClient.target(SEARCH_BY_MOBILE_ENDPOINT)
                .request()
                .post(Entity.json(partySearchRequestDTO));

        assertThat(response.readEntity(PartySearchResponse.class).getData().size()).isEqualTo(0);
        assertEquals(HttpStatus.OK_200, response.getStatus());
        verify(mockPartyService, times(1)).findPartyDetailsByMobileNumber(partySearchRequestDTO);
    }

    @Test
    void Should_Return_Failure_Response_When_Fail_To_Perform_MobileNumber_Search() {
        PartySearchCmd partySearchRequestDTO = new PartySearchCmd();
        partySearchRequestDTO.setMobileNumber("1111111111");

        when(mockPartyService.findPartyDetailsByMobileNumber(partySearchRequestDTO)).thenThrow(new RuntimeException());
        Response response = resourceTestClient.target(SEARCH_BY_MOBILE_ENDPOINT)
                .request()
                .post(Entity.json(partySearchRequestDTO));

        assertThat(response.getStatus()).isEqualTo(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode());
        assertThat(response.readEntity(ErrorMessage.class).getMessage())
                .isEqualTo("Failed to search party details");
        verify(mockPartyService, times(1)).findPartyDetailsByMobileNumber(partySearchRequestDTO);

    }
    @Test
    void should_return_response_when_create_party_record() throws IOException {
        PartyRequestCmd partyRequestCmd = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);
        PartyCmd partyCmd = new PartyCmd();
        partyCmd.setPartyId(1);
        PartyResponseCmd partyResponseCmd = new PartyResponseCmd();
        partyResponseCmd.setParty(partyCmd);
        when(mockPartyService.save(partyRequestCmd)).thenReturn(partyResponseCmd);
        when(mockPartyService.validatePartyRecord(partyRequestCmd.getParty())).thenReturn(null);
        Response actualResponse = resourceTestClient.target("/party")
                .request()
                .post(Entity.json(partyRequestCmd));

        Assertions.assertThat(actualResponse.readEntity(PartyResponseCmd.class).getParty().getPartyId()).isEqualTo(partyRequestCmd.getParty().getPartyId());
        assertEquals(HttpStatus.CREATED_201, actualResponse.getStatus());
        verify(mockPartyService, times(1)).save(partyRequestCmd);
    }

    @Test
    void should_return_no_content_when_create_Party_record() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);

        when(mockPartyService.validatePartyRecord(partyRequestCmd.getParty())).thenReturn(new ErrorMessage(HttpStatus.BAD_REQUEST_400, "ERROR", "Party Already Exists !"));
        Response actualResponse = resourceTestClient.target("/party/")
                .request()
                .post(Entity.json(partyRequestCmd));

        assertEquals(HttpStatus.BAD_REQUEST_400, actualResponse.getStatus());
        verify(mockPartyService, times(1)).validatePartyRecord(partyRequestCmd.getParty());
    }

    @Test
    void should_return_failure_response_when_exception_occur_during_create_Party_record() throws IOException {
        PartyRequestCmd partyRequestCmd  = MAPPER.readValue(getClass().getResource("/fixtures/Party.json"), PartyRequestCmd.class);

        when(mockPartyService.validatePartyRecord(partyRequestCmd.getParty())).thenThrow(new RuntimeException());
        Response actualResponse = resourceTestClient.target("/party/")
                .request()
                .post(Entity.json(partyRequestCmd));

        assertThat(actualResponse.readEntity(ErrorMessage.class).getMessage()).isEqualTo("Failed To Create Party");
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR_500, actualResponse.getStatus());
        verify(mockPartyService, times(1)).validatePartyRecord(partyRequestCmd.getParty());
    }

}