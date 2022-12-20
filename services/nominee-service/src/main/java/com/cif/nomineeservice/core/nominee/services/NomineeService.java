package com.cif.nomineeservice.core.nominee.services;

import com.cif.nomineeservice.api.*;
import com.cif.nomineeservice.core.nominee.domain.Nominee;
import com.cif.nomineeservice.core.nominee.util.NomineeMapper;
import com.cif.nomineeservice.db.NomineeRepository;
import com.cif.nomineeservice.helper.ResponseHelper;
import com.cif.nomineeservice.resources.exceptions.RecordNotFoundException;
import com.cif.nomineeservice.resources.exceptions.RecordPersistException;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.slf4j.LoggerFactory.getLogger;

public class NomineeService {

    public static final String ERROR = "ERROR";
    public static final String SUCCESS = "SUCCESS";
    private static final Logger LOGGER = getLogger(NomineeService.class);
    private final NomineeRepository nomineeRepository;

    public NomineeService(NomineeRepository nomineeRepository) {
        this.nomineeRepository = nomineeRepository;
    }

    public Response savePartyNominees(CreateNomineeRequest nomineeRequest) {
        LOGGER.info("Saving nominee for [partyId = {} ]", nomineeRequest.getPartyId());
        try {
            int nomineeId = nomineeRepository.save(nomineeRequest);
            if (nomineeId == 0) {
                LOGGER.info("Nominees save failed related for [partyId = {}]", nomineeRequest.getPartyId());
                return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), ERROR, "Create Nominee Failed")).build();
            } else {
                LOGGER.info("Nominees save success related for [partyId = {}]", nomineeRequest.getPartyId());
                var nomineeRecord = nomineeRepository.find(nomineeId);
                return Response.status(HttpStatus.CREATED_201).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), SUCCESS, "Create Nominee Success", buildNomineeResponse(nomineeRecord))).build();
            }
        } catch (Exception e) {
            throw new RecordPersistException("Failed to create party nominee", e);
        }
    }


    public Response updateNominee(UpdateNomineeRequest nomineeRequest) {

        LOGGER.info("Starting nominee update process for [nomineeId={}] ", nomineeRequest.getNomineeId());
        try {
            var existingNomineeRecord = nomineeRepository.find(nomineeRequest.getNomineeId());
            var nomineeRecordNeedToBeUpdated = NomineeMapper.MAPPER.updateWithNullAsNoChangeNominee(nomineeRequest, existingNomineeRecord);
            boolean updateStatus = nomineeRepository.updateNominee(nomineeRecordNeedToBeUpdated);
            if (!updateStatus) {
                LOGGER.info("Update failed for [nomineeId={}] for [partyId={}] ", nomineeRequest.getNomineeId(), nomineeRequest.getPartyId());
                return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), ERROR, "Update Nominee Failed")).build();
            } else {
                LOGGER.info("Update success for  [nominee ids {}] for [partyId = {} ]", nomineeRequest.getNomineeId(), nomineeRequest.getPartyId());
                return Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), SUCCESS, "Update Nominee Success", buildNomineeResponse(existingNomineeRecord))).build();
            }
        } catch (Exception e) {
            throw new RecordPersistException("Failed to update party nominee", e);
        }
    }

    public Response fetchNomineeDetailsByPartyIdOrNomineeId(int partyId, int nomineeId) {
        try {
            if (partyId != 0) {
                var nomineeList = nomineeRepository.findByPartyId(partyId);
                if (nomineeList.isEmpty())
                    return Response.status(HttpStatus.NO_CONTENT_204).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "NO CONTENT", "No Record Exist")).build();

                Set<NomineeResponse> nomineeResponseList = nomineeList.stream().map(this::buildNomineeResponse).collect(Collectors.toSet());
                return Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), SUCCESS, "Fetch Nominees Success", FetchNominee200ResponseAllOf.builder().data(nomineeResponseList).build())).build();
            } else {
                var nomineeRecord = nomineeRepository.find(nomineeId);
                if (nomineeRecord == null)
                    return Response.status(HttpStatus.NO_CONTENT_204).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), "NO CONTENT", "No Record Exist")).build();

                return Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), SUCCESS, "Fetch Nominee Success", buildNomineeResponse(nomineeRecord))).build();
            }
        } catch (Exception e) {
            throw new RecordNotFoundException("Failed to fetch details for nominee", e);
        }
    }

    public Response deleteNominee(int nomineeId) {
        try {
            var deleteStatus = nomineeRepository.delete(nomineeId);
            if (deleteStatus) {
                LOGGER.info("Delete success for [nominee id {}]", nomineeId);
                return Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), SUCCESS, "Delete Nominee Success")).build();
            } else {
                LOGGER.info("Delete failed for [nominee id {}]", nomineeId);
                return Response.status(HttpStatus.INTERNAL_SERVER_ERROR_500).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), ERROR, String.format("Delete For Nominee Id %s Failed", nomineeId))).build();
            }
        } catch (Exception e) {
            throw new RecordPersistException("Failed to delete party nominee", e);
        }
    }

    private NomineeResponse buildNomineeResponse(Nominee nomineeRecord) {
        return NomineeResponse.builder().partyId(nomineeRecord.getPartyId()).nomineeId(nomineeRecord.getNomineeId()).relation(nomineeRecord.getRelation()).salutation(nomineeRecord.getSalutation()).firstName(nomineeRecord.getFirstName()).lastName(nomineeRecord.getLastName()).middleName(nomineeRecord.getMiddleName()).nationalId(nomineeRecord.getNationalId()).build();
    }

    public Response saveNomineeMapping(NomineeMappingRequest nomineeMappingRequest) {
        try {
            var nomineeRecord = nomineeRepository.find(nomineeMappingRequest.getNomineeId());
            if (nomineeRecord != null && nomineeMappingRequest.getPartyId().equals(nomineeRecord.getPartyId()) && nomineeRepository.saveNomineeMapping(nomineeMappingRequest)) {
                LOGGER.info("Nominee mapping created for party id {} ", nomineeMappingRequest.getNomineeId());
                return Response.status(HttpStatus.CREATED_201).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), SUCCESS, "Create Nominee Mapping Success", null)).build();

            } else {
                LOGGER.error("nominee id not found for saving nominee mapping record for party id {}", nomineeMappingRequest.getNomineeId());
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), ERROR, "Create Nominee Mapping Failed")).build();

            }
        } catch (Exception exception) {
            throw new RecordPersistException("Failed to create nominee mapping", exception);
        }
    }

    public Response deleteNomineeMapping(Long nomineeMappingId) {
        try {
            var status = nomineeRepository.deleteNomineeMapping(nomineeMappingId);
            if (status) {
                LOGGER.info("Deleting nominee mapping for mapping id {} ", nomineeMappingId);
                return Response.status(HttpStatus.OK_200).entity(ResponseHelper.buildSuccessResponseApi(UUID.randomUUID().toString(), SUCCESS, "Nominee mapping deleted successfully", null)).build();
            } else {
                LOGGER.info("Unable to delete nominee mapping for mapping id {}", nomineeMappingId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(ResponseHelper.buildResponseApi(UUID.randomUUID().toString(), ERROR, "Error occurred while deleting nominee mapping record")).build();
            }
        } catch (Exception exception) {
            throw new RecordPersistException("Failed to delete nominee mapping", exception);
        }
    }

}
