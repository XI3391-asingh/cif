package com.cif.cifservice.resources;

import com.cif.cifservice.api.*;
import com.cif.cifservice.core.party.services.PartyService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

import static org.slf4j.LoggerFactory.getLogger;

public class PartyResource implements PartyApi {

    private final Logger logger = getLogger(PartyResource.class);

    private final PartyService partyService;

    public PartyResource(PartyService partyService) {
        this.partyService = partyService;
    }

    @Override
    public Response create(PartyRequestCmd partyRequestCmd) {
        logger.info("Creating party record for type:{}", partyRequestCmd.getParty().getType());
        return partyService.save(partyRequestCmd);

    }

    @Override
    public Response fetchPartyDetailByIdentifier(List<String> id) {
        logger.info("Fetching party details for party ids:{}", id);
        return partyService.getPartyDetails(id);
    }

    @Override
    public Response update(UpdatePartyRequestCmd updatePartyRequestCmd) {
        logger.info("Updating party record for party id: {}", updatePartyRequestCmd.getParty().getPartyIdentifier());
        return partyService.update(updatePartyRequestCmd);
    }

    @Override
    public Response updatePartyRecordForSoftDelete(UpdatePartyRecordForSoftDeleteRequest updatePartyRecordForSoftDeleteRequest) {
        logger.info("Performing soft delete for party id: {}", updatePartyRecordForSoftDeleteRequest.getPartyIdentifier());
        return partyService.updatePartyRecordForSoftDeleteByPartyId(updatePartyRecordForSoftDeleteRequest);
    }

    @Override
    public Response createAddress(String partyIdentifier, Set<CreateAddressCmd> createAddressCmd) {
        logger.info("Creating address record for party id: {}", partyIdentifier);
        return partyService.saveAddress(createAddressCmd, partyIdentifier);
    }

    @Override
    public Response updateAddress(String partyIdentifier, Long partyAddressId, UpdateAddressCmd updateAddressCmd) {
        logger.info("Updating address record for party id: {} and address id {} ", partyIdentifier, partyAddressId);
        return partyService.updateAddress(partyIdentifier, partyAddressId, updateAddressCmd);
    }

    @Override
    public Response fetchAddress(String partyIdentifier, Long partyAddressId) {
        logger.info("Fetch address details for party id: {} and address id {} ", partyIdentifier, partyAddressId);
        return partyService.fetchAddressDetails(partyIdentifier, partyAddressId);
    }

    @Override
    public Response fetchAllAddress(String partyIdentifier) {
        logger.info("Fetch All address details for party id: {} ", partyIdentifier);
        return partyService.fetchAddressDetails(partyIdentifier, null);
    }

    @Override
    public Response dedupeVerification(DedupeFieldRequestCmd dedupeFieldRequestCmd) {
        return partyService.checkDedupe(dedupeFieldRequestCmd);
    }

    @Override
    public Response search(String sortingOrder, Boolean isDelete, PartyDistinctiveSearchCmd partyDistinctiveSearchCmd, Integer limit, Integer offset) {
        return partyService.getPartyDetailsByPassedParameter(sortingOrder, isDelete, partyDistinctiveSearchCmd, limit, offset);
    }

    @Override
    public Response deleteAddress(String partyIdentifier, Long partyAddressIdentifier) {
        logger.info("Performing address delete for address id : {} ", partyAddressIdentifier);
        return partyService.deleteAddressByPassedParameter(partyIdentifier, partyAddressIdentifier);
    }

    @Override
    public Response createContacts(String partyIdentifier, Set<CreateContactDetailsCmd> createContactDetailsCmd) {
        logger.info("Creating contacts record for party id: {}", partyIdentifier);
        return partyService.saveContacts(createContactDetailsCmd, partyIdentifier);
    }

    @Override
    public Response updateContacts(String partyIdentifier, Long partyContactDetailsId, UpdateContactDetailsCmd updateContactDetailsCmd) {
        logger.info("Updating contact record for party id: {} and contact id {} ", partyIdentifier, partyContactDetailsId);
        return partyService.updateContacts(partyIdentifier, partyContactDetailsId, updateContactDetailsCmd);
    }

    @Override
    public Response fetchAllContacts(String partyIdentifier) {
        logger.info("Fetch All contacts details for party id: {} ", partyIdentifier);
        return partyService.fetchContactDetails(partyIdentifier, null);
    }

    @Override
    public Response fetchContact(String partyIdentifier, Long partyContactDetailsId) {
        logger.info("Fetch contact details for party id: {} and contact id {} ", partyIdentifier, partyContactDetailsId);
        return partyService.fetchContactDetails(partyIdentifier, partyContactDetailsId);
    }

    @Override
    public Response deleteContacts(String partyIdentifier, Long contactIdentifier) {
        logger.info("Performing contact delete for address id : {} ", contactIdentifier);
        return partyService.deleteContact(partyIdentifier, contactIdentifier);
    }

    @Override
    public Response createDocument(String partyIdentifier, Set<CreateDocumentCmd> createDocumentCmd) {
        logger.info("Creating document record for party id: {}", partyIdentifier);
        return partyService.saveDocument(createDocumentCmd, partyIdentifier);
    }

    @Override
    public Response updateDocuments(String partyIdentifier, Long partyDocumentsId, UpdateDocumentsCmd updateDocumentsCmd) {
        logger.info("Updating document record for party id: {} and document id {} ", partyIdentifier, partyDocumentsId);
        return partyService.updateDocuments(partyIdentifier, partyDocumentsId, updateDocumentsCmd);
    }


    @Override
    public Response fetchAllDocuments(String partyIdentifier) {
        logger.info("Fetch All document details for party id: {} ", partyIdentifier);
        return partyService.fetchDocuments(partyIdentifier, null);
    }

    @Override
    public Response fetchDocument(String partyIdentifier, Long partyDocumentsId) {
        logger.info("Fetch document details for party id: {} and document id {} ", partyIdentifier, partyDocumentsId);
        return partyService.fetchDocuments(partyIdentifier, partyDocumentsId);
    }

    @Override
    public Response deleteDocuments(String partyIdentifier, Long documentId) {
        logger.info("Performing document delete for document id : {} ", documentId);
        return partyService.deleteDocuments(partyIdentifier, documentId);
    }


    @Override
    public Response createRisks(String partyIdentifier, Set<CreateRisksCmd> createRisksCmd) {
        logger.info("Creating risk record for party identifier: {}", partyIdentifier);
        return partyService.saveRisk(createRisksCmd, partyIdentifier);
    }

    @Override
    public Response updateRisks(String partyIdentifier, Long partyRiskId, UpdateRisksCmd updateRisksCmd) {
        logger.info("Updating risk record for party identifier: {} and risk id {} ", partyIdentifier, partyRiskId);
        return partyService.updateRisk(partyIdentifier, partyRiskId, updateRisksCmd);
    }

    @Override
    public Response fetchAllRisks(String partyIdentifier) {
        logger.info("Fetch All risk details for party identifier: {} ", partyIdentifier);
        return partyService.fetchRisks(partyIdentifier, null);
    }

    @Override
    public Response fetchAllxref(String partyIdentifier) {
        logger.info("Fetch All xref details for party identifier: {} ", partyIdentifier);
        return partyService.fetchXref(partyIdentifier);
    }

    @Override
    public Response fetchRisks(String partyIdentifier, Long partyRiskId) {
        logger.info("Fetch risk details for party identifier: {} and risk id {} ", partyIdentifier, partyRiskId);
        return partyService.fetchRisks(partyIdentifier, partyRiskId);
    }

    @Override
    public Response createFatca(String partyIdentifier, Set<CreateFatcaDetailsCmd> createFatcaDetailsCmd) {
        logger.info("Creating fatca record for party identifier: {}", partyIdentifier);
        return partyService.saveFatcaDetails(createFatcaDetailsCmd, partyIdentifier);
    }

    @Override
    public Response updateFatca(String partyIdentifier, Long partyFatcaDetailsId, UpdateFatcaDetailsCmd updateFatcaDetailsCmd) {
        logger.info("Updating fatca record for party identifier: {} and fatca id {} ", partyIdentifier, partyFatcaDetailsId);
        return partyService.updateFatcaDetails(partyIdentifier, partyFatcaDetailsId, updateFatcaDetailsCmd);
    }

    @Override
    public Response fetchAllFatca(String partyIdentifier) {
        logger.info("Fetch All fatca details for party identifier: {} ", partyIdentifier);
        return partyService.fetchFatcaDetails(partyIdentifier, null);
    }

    @Override
    public Response fetchFatca(String partyIdentifier, Long partyFatcaDetailsId) {
        logger.info("Fetch fatca details for party identifier: {} and fatca id {} ", partyIdentifier, partyFatcaDetailsId);
        return partyService.fetchFatcaDetails(partyIdentifier, partyFatcaDetailsId);
    }

    @Override
    public Response createMemos(String partyIdentifier, Set<CreateMemosCmd> createMemosCmd) {
        logger.info("Creating memo record for party identifier: {}", partyIdentifier);
        return partyService.saveMemos(createMemosCmd, partyIdentifier);
    }

    @Override
    public Response updateMemos(String partyIdentifier, Long partyMemosId, UpdateMemosCmd updateMemosCmd) {
        logger.info("Updating memo record for party id: {} and memo id {} ", partyIdentifier, partyMemosId);
        return partyService.updateMemos(partyIdentifier, partyMemosId, updateMemosCmd);
    }

    @Override
    public Response fetchAllMemos(String partyIdentifier) {
        logger.info("Fetch All memo details for party id: {} ", partyIdentifier);
        return partyService.fetchMemos(partyIdentifier, null);
    }

    @Override
    public Response fetchMemo(String partyIdentifier, Long partyMemosId) {
        logger.info("Fetch memo details for party id: {} and memo id {} ", partyIdentifier, partyMemosId);
        return partyService.fetchMemos(partyIdentifier, partyMemosId);
    }

    @Override
    public Response updateEmail(PartyEmailUpdateCmd partyEmailUpdateCmd) {
        logger.info("Update email for party id: {}", partyEmailUpdateCmd.getPartyIdentifier());
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyEmailUpdateCmd.getPartyIdentifier()).primaryEmail(partyEmailUpdateCmd.getPrimaryEmail()).build()).build();
        return partyService.update(updatePartyRequestCmd);
    }

    @Override
    public Response updateMobileNumber(PartyMobileUpdateCmd partyMobileUpdateCmd) {
        logger.info("Update mobile number for party id: {}", partyMobileUpdateCmd.getPartyIdentifier());
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyMobileUpdateCmd.getPartyIdentifier()).primaryMobileNumber(partyMobileUpdateCmd.getPrimaryMobileNumber()).build()).build();
        return partyService.update(updatePartyRequestCmd);
    }

    @Override
    public Response updateStatus(PartyStatusUpdateCmd partyStatusUpdateCmd) {
        logger.info("Update status for party id: {}", partyStatusUpdateCmd.getPartyIdentifier());
        UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(UpdatePartyCmd.builder().partyIdentifier(partyStatusUpdateCmd.getPartyIdentifier()).status(UpdatePartyCmd.StatusEnum.fromString(partyStatusUpdateCmd.getStatus().value())).build()).build();
        return partyService.update(updatePartyRequestCmd);
    }

    @Override
    public Response advanceSearch(AdvanceSearchRequestCmd advanceSearchRequestCmd) {
        return partyService.fetchDataUsingElasticSearch(advanceSearchRequestCmd);
    }

    @Override
    public Response universalSearch(UniversalSearchRequest universalSearchRequest) {
        return partyService.performUniversalSearchBasedOnUserData(universalSearchRequest);
    }

    @Override
    public Response createGuardian(String partyIdentifier, Set<CreateGuardianCmd> createGuardianCmd) {
        logger.info("Creating guardian record for party identifier: {}", partyIdentifier);
        return partyService.saveGuardians(createGuardianCmd, partyIdentifier);
    }

    @Override
    public Response deleteGuardian(String partyIdentifier, Long partyGuardianId) {
        logger.info("Performing guardian delete for guardian id : {} ", partyGuardianId);
        return partyService.deleteGuardians(partyIdentifier, partyGuardianId);

    }

    @GET
    @Path("/encryptPreviousData/{batchSize}")
    @Produces({"application/json"})
    @ApiOperation(value = "encrypt all PII data", notes = "", tags = {"encrypt"})
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = SuccessResponseApi.class),
            @ApiResponse(code = 400, message = "Failed", response = ErrorResponseApi.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = ErrorResponseApi.class)})
    public Response encryptPIIData(@PathParam("batchSize") @ApiParam("Batch Size") Integer batchSize) {
        logger.info("Performing PII data encryption of previously saved plain text data");
        return partyService.encryptPIIData(batchSize);
    }

}
