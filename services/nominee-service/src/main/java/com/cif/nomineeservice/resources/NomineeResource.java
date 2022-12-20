package com.cif.nomineeservice.resources;

import com.cif.nomineeservice.api.*;
import com.cif.nomineeservice.core.nominee.services.NomineeService;
import org.slf4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class NomineeResource implements NomineeApi {

    private static final Logger LOGGER = getLogger(NomineeResource.class);

    private final NomineeService nomineeService;

    public NomineeResource(NomineeService nomineeService) {
        this.nomineeService = nomineeService;
    }


    @Override
    public Response createNominee(CreateNomineeRequest nomineeRequest) {
        LOGGER.info("Create nominees for party id:{}", nomineeRequest.getPartyId());
        return nomineeService.savePartyNominees(nomineeRequest);
    }


    @Override
    public Response updateNominee(UpdateNomineeRequest nomineeRequest) {
        LOGGER.info("Update nominees for party id:{}", nomineeRequest.getPartyId());
        return nomineeService.updateNominee(nomineeRequest);
    }

    @Override
    public Response deleteNominee(Integer nomineeId) {
        LOGGER.info("Delete [nominee id={}]", nomineeId);
        return nomineeService.deleteNominee(nomineeId);
    }

    @Override
    public Response fetchNominee(FetchNomineeRequest fetchNomineeRequest) {
        if (fetchNomineeRequest.getNomineeId() != null) {
            LOGGER.info("Fetching nominee record for [nominee id={}] or [party id={}] ", fetchNomineeRequest.getNomineeId(), fetchNomineeRequest.getPartyId());
            return nomineeService.fetchNomineeDetailsByPartyIdOrNomineeId(0, fetchNomineeRequest.getNomineeId());
        } else {
            LOGGER.info("Fetching nominee record for [nominee id={}] or [party id={}] ", fetchNomineeRequest.getNomineeId(), fetchNomineeRequest.getPartyId());
            return nomineeService.fetchNomineeDetailsByPartyIdOrNomineeId(fetchNomineeRequest.getPartyId(), 0);
        }
    }
    @Override
    public Response createNomineeMapping(NomineeMappingRequest nomineeMappingRequest) {
        LOGGER.info("Creating nominee mapping record for party id: {}", nomineeMappingRequest.getPartyId());
        return nomineeService.saveNomineeMapping(nomineeMappingRequest);
    }

    @Override
    public Response deleteNomineeMapping(Long nomineeMappingId) {
        LOGGER.info("Deleting nominee mapping record for mapping id : {} ", nomineeMappingId);
        return nomineeService.deleteNomineeMapping(nomineeMappingId);
    }

}