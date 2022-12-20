package com.finx.customerservice.resources;

import com.finx.customerservice.api.PartyApi;
import com.finx.customerservice.api.PartyRequestCmd;
import com.finx.customerservice.api.PartySearchCmd;
import com.finx.customerservice.api.PartySearchResponse;
import com.finx.customerservice.core.customer.services.PartyService;
import com.finx.customerservice.resources.exceptions.PartyException;
import io.dropwizard.jersey.errors.ErrorMessage;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;


@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)

public class PartyResource implements PartyApi {

    private Logger log = getLogger(PartyResource.class);

    private final PartyService partyService;

    public PartyResource(PartyService partyService) {
        this.partyService = partyService;
    }


    @Override
    public Response createParty(PartyRequestCmd partyRequestCmd) {
        Response response = null;
        try {
            log.info("createParty :");
            ErrorMessage responseStatus = partyService.validatePartyRecord(partyRequestCmd.getParty());
            if (responseStatus == null) {
                response = Response.status(HttpStatus.CREATED_201).entity(partyService.save(partyRequestCmd)).build();
            } else {
                response = Response.status(HttpStatus.BAD_REQUEST_400).entity(responseStatus).build();
            }
        } catch (Exception e) {
            throw new PartyException("Failed To Create Party", e);
        }
        return response;
    }

    public Response searchByMobileNumber(@Valid @NotNull PartySearchCmd partySearchRequest) {
        log.info("Search party details for mobileNumber {}", partySearchRequest.getMobileNumber());
        try {
            PartySearchResponse partySearchResponseDTO = partyService.findPartyDetailsByMobileNumber(partySearchRequest);
            return Response.status(Response.Status.OK)
                    .entity(partySearchResponseDTO)
                    .build();
        } catch (Exception e) {
            log.error("Error occurred while searching party details mobileNumber {} ", partySearchRequest.getMobileNumber());
            throw new PartyException(
                    "Failed to search party details",
                    e);
        }

    }
}
