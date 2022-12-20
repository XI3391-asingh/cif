package com.cif.cifservice.core.party.services;

import com.cif.cifservice.api.*;
import com.cif.cifservice.core.party.domain.ApiResponse;
import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.core.party.domain.Party;
import com.cif.cifservice.core.party.domain.PartyDetailsView;
import com.cif.cifservice.core.party.event.CreateEvent;
import com.cif.cifservice.core.party.event.DeleteEvent;
import com.cif.cifservice.core.party.event.GenericUpdateEvent;
import com.cif.cifservice.core.party.event.UpdateEvent;
import com.cif.cifservice.core.party.util.CryptoUtil;
import com.cif.cifservice.core.party.util.PartyMapper;
import com.cif.cifservice.db.PartyRepository;
import com.cif.cifservice.db.PartyTransactionRepository;
import com.cif.cifservice.db.elasticsearch.ElasticsearchClientRepository;
import com.cif.cifservice.resources.exceptions.DatabasePersistException;
import com.cif.cifservice.resources.exceptions.DuplicateRecordException;
import com.cif.cifservice.resources.exceptions.InvalidParameterException;
import com.cif.cifservice.resources.exceptions.ServerSideException;
import com.google.common.eventbus.EventBus;
import org.apache.commons.lang3.ObjectUtils;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.cif.cifservice.core.party.helper.ResponseHelper.buildErrorResponse;
import static com.cif.cifservice.core.party.helper.ResponseHelper.buildSuccessResponse;
import static org.apache.commons.lang3.StringUtils.*;
import static org.slf4j.LoggerFactory.getLogger;

public class PartyService {
    public static final String EMAIL_REGEX = "^\\w+([\\.-]?\\w+)*@\\w+([\\.-]?\\w+)*(\\.\\w{2,3})+$";
    public static final String NUMBER = "number";
    public static final String TEXT = "text";
    public static final String DATE = "date";
    public static final String BOOLEAN = "boolean";
    private final Logger logger = getLogger(PartyService.class);
    public static final String ERROR = "ERROR";

    public static final String DATA_PERSIST_ERROR_CODE = "DATA_PERSIST_ERROR";
    public static final String DATA_NOT_FOUND_ERROR_CODE = "DATA_NOT_FOUND_ERROR";
    public static final String NO_RECORD_FOUND = "NO_RECORD_FOUND";

    private static final String MOBILE = "Mobile";

    private static final String EMAIL = "Email";
    private final PartyRepository partyRepository;
    private final PartyTransactionRepository partyTransactionRepository;

    private final EventBus eventBus;

    private final ElasticsearchClientRepository clientRepository;

    private final Map<String, List<String>> universalSearchFields;
    private final CryptoUtil cryptoUtil;
    private Config configRecord;
    public PartyService(PartyRepository partyRepository, PartyTransactionRepository partyTransactionRepository, EventBus eventBus, ElasticsearchClientRepository clientRepository, Map<String, List<String>> universalSearchFields, CryptoUtil cryptoUtil) {
        this.partyRepository = partyRepository;
        this.partyTransactionRepository = partyTransactionRepository;
        this.eventBus = eventBus;
        this.clientRepository = clientRepository;
        this.universalSearchFields = universalSearchFields;
        this.cryptoUtil = cryptoUtil;
    }

    public Response save(PartyRequestCmd partyRequestCmd) {
        if (isEmpty(partyRequestCmd.getParty().getFullName())) {
            var fullName = partyRequestCmd.getParty().getFirstName().concat(" ")
                    .concat(partyRequestCmd.getParty().getMiddleName() != null ? partyRequestCmd.getParty().getMiddleName().concat(" ") : "")
                    .concat(partyRequestCmd.getParty().getLastName() != null ? partyRequestCmd.getParty().getLastName() : "");
            partyRequestCmd.getParty().setFullName(fullName.trim());
        }
        partyRequestCmd.getContactDetails().stream().forEach(contactDetail -> validateContact(contactDetail.getContactTypeCode(), contactDetail.getContactValue()));
        cryptoUtil.encryptObjectUsingConfiguredField(partyRequestCmd);
        validateParty(partyRequestCmd.getParty());
        partyRequestCmd.getMemos().stream().forEach(memoDetail -> validateDate(memoDetail.getValidFrom(), memoDetail.getValidTo()));
        try {
            var partyId = partyTransactionRepository.createAllPartySection(partyRequestCmd);
            logger.info("Party record created with Party ID {}", partyId);
            var partyResponseCmd = fetchPartyDetails(partyId);
            if (partyResponseCmd != null && partyId != null) {
                eventBus.post(new CreateEvent(partyResponseCmd, partyId));
            }
            return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("PARTY_CREATED", "Party record saved successfully!", partyResponseCmd)).build();
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while saving the record. Please contact the system administrator ! ", exception);
        }
    }

    public Response update(UpdatePartyRequestCmd updatePartyRequestCmd) {
        Long partyId = partyRepository.findIdByPartyIdentifier(updatePartyRequestCmd.getParty().getPartyIdentifier());
        cryptoUtil.encryptObjectUsingConfiguredField(updatePartyRequestCmd);
        validatePartyRecord(Math.toIntExact(partyId), updatePartyRequestCmd.getParty().getPrimaryMobileNumber());
        try {
            var updateStatus = partyTransactionRepository.updateAllPartySection(updatePartyRequestCmd);
            if (updateStatus && partyId != null) {
                logger.info("Party record(s) updated for party ID:{}", partyId);
                var partyResponse = fetchPartyDetails(partyId.longValue());
                if (partyResponse != null) {
                    eventBus.post(new GenericUpdateEvent(partyResponse, partyId, updatePartyRequestCmd));
                }
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("PARTY_UPDATED", "Party record updated successfully!", null)).build();
            } else {
                logger.error("Party record(s) not updated for PARTY ID {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while updating party record!")).build();
            }
        } catch (Exception e) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while updating the record. Please contact the system administrator ! ", e);
        }
    }

    public Response getPartyDetails(List<String> partiesIsd) {
        List<Long> partyIds = new ArrayList<>();
        partiesIsd.stream().forEach(partyIdentifier -> {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            partyIds.add(partyId);
        });
        var parties = partyIds.stream().filter(Objects::nonNull)
                .map(partyId -> cryptoUtil.decryptObjectUsingConfiguredField(fetchPartyDetails(partyId)))
                .filter(partyResponseCmd -> ObjectUtils.isNotEmpty(partyResponseCmd.getParty())).toList();
        logger.info("Total record(s) :{} found", parties.size());
        if (!parties.isEmpty()) {
            return Response.status(HttpStatus.OK_200)
                    .entity(buildSuccessResponse("RECORD_FOUND", "Total record found : " + parties.size(), parties)).build();
        } else {
            return Response.status(HttpStatus.NOT_FOUND_404)
                    .entity(buildErrorResponse(NO_RECORD_FOUND, "No party record found !")).build();
        }
    }

    public PartyResponseCmd fetchPartyDetails(Long partyId) {
        List<PartyDetailsView> partyDetailList;
        PartyResponseCmd partyResponseCmd;
        try {
            partyDetailList = partyRepository.findPartyDetail(partyId);
            logger.info("Fetching party details for party id {} and total count {} ", partyId, partyDetailList.size());
        } catch (Exception e) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetch party details. Please contact the system administrator ! ", e);
        }
        partyResponseCmd = new PartyResponseCmd();
        partyResponseCmd.setParty(PartyMapper.MAPPER.mapPartyInformation(!partyDetailList.isEmpty() ? partyDetailList.get(0) : null));
        partyResponseCmd.setOccupationDetail(PartyMapper.MAPPER.mapPartyOccupationDetails(!partyDetailList.isEmpty() ? partyDetailList.get(0) : null));
        partyResponseCmd.setPartyFlag(PartyMapper.MAPPER.mapPartyFlag(!partyDetailList.isEmpty() ? partyDetailList.get(0) : null));
        partyResponseCmd.setAddress(PartyMapper.MAPPER.mapAddressDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyAddressId() != null ? partyDetailList : new ArrayList<>()));
        partyResponseCmd.setContactDetails(PartyMapper.MAPPER.mapPartyContactDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyContactDetailsId() != null ? partyDetailList : new ArrayList<>()));
        partyResponseCmd.setAssets(PartyMapper.MAPPER.mapPartyAssetDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyAssetId() != null ? partyDetailList : new ArrayList<>()));
        partyResponseCmd.setRisks(PartyMapper.MAPPER.mapPartyRiskDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyRiskId() != null ? partyDetailList : new ArrayList<>()));
        setMappedPartyDetails(partyResponseCmd, partyDetailList);

        return partyResponseCmd;
    }

    public void setMappedPartyDetails(PartyResponseCmd partyResponseCmd, List<PartyDetailsView> partyDetailList) {
        partyResponseCmd.setMemos(PartyMapper.MAPPER.mapPartyMemoDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyMemoId() != null ? partyDetailList : new ArrayList<>()));
        partyResponseCmd.setRelations(PartyMapper.MAPPER.mapPartyRelationDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyRelationId() != null ? partyDetailList : new ArrayList<>()));
        partyResponseCmd.setFatcaDetails(PartyMapper.MAPPER.mapPartyFatcaDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyFatcaDetailsId() != null ? partyDetailList : new ArrayList<>()));
        partyResponseCmd.setDocuments(PartyMapper.MAPPER.mapPartyDocumentDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyDocumentId() != null ? partyDetailList : new ArrayList<>()));
        partyResponseCmd.setXrefs(PartyMapper.MAPPER.mapPartyXrefDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyXrefId() != null ? partyDetailList : new ArrayList<>()));
        partyResponseCmd.setGuardians(PartyMapper.MAPPER.mapPartyGuardianDetails(!partyDetailList.isEmpty() && partyDetailList.get(0).getPartyGuardianId() != null ? partyDetailList : new ArrayList<>()));
    }


    public void validatePartyRecord(Integer partyId, String mobileNumber) {
        if (checkDuplicatePartyByPartyIdAndMobileNumber(partyId, mobileNumber)) {
            throw new DuplicateRecordException("DUPLICATE_DATA_ERROR", "The record is already registered with a mobile number!");
        }
    }

    public boolean checkDuplicatePartyByPartyIdAndMobileNumber(Integer partyId, String mobileNumber) {
        Long id = partyRepository.findPartyIdByPartyIdAndMobileNumber(partyId, mobileNumber);
        logger.info("Duplicate party check for party id {} ", partyId);
        boolean isPartyDuplicate = id != null && id != 0 ? true : false;
        return isPartyDuplicate;
    }

    public boolean isPartyRecordExist(long partyId) {
        logger.info("Checking party record exist for party id {}", partyId);
        return partyRepository.findIdByPartyId(partyId) != null ? true : false;
    }

    public Response updatePartyRecordForSoftDeleteByPartyId(UpdatePartyRecordForSoftDeleteRequest recordForSoftDeleteRequest) {
        var partyIdentifier = recordForSoftDeleteRequest.getPartyIdentifier();
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            if (partyId != null && partyRepository.updatePartyIsDeletedFlagByPartyId(partyId)) {
                logger.info("Soft delete update for Party Id {}", partyId);
                eventBus.post(new UpdateEvent(partyId, recordForSoftDeleteRequest));
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("SOFT_DELETE", "Soft delete operation perform successfully!", null)).build();
            } else {
                logger.error("Party id not found to performing soft delete for PARTY ID {} ", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while performing Soft delete!")).build();
            }
        } catch (Exception e) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while soft delete. Please contact the system administrator ! ", e);
        }
    }

    public Response saveAddress(Set<CreateAddressCmd> createAddressCmd, String partyIdentifier) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            cryptoUtil.encryptObjectUsingConfiguredField(createAddressCmd);
            if (partyId != null && partyRepository.saveAddress(createAddressCmd, partyId)[0]) {
                logger.info("Address created for party id {} ", partyId);
                var addressDetailsByIds = partyRepository.findAddressDetailsByIds(partyId, null);
                Optional<CreateAddressCmd> addressRecord = createAddressCmd.stream().findFirst();
                if (addressRecord.isPresent()) {
                    eventBus.post(new UpdateEvent(partyId, addressRecord.get()));
                }
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("ADDRESS_CREATED", "Address saved successfully!!", cryptoUtil.decryptObjectUsingConfiguredField(addressDetailsByIds))).build();
            } else {
                logger.error("Party id not found for saving address record for PARTY ID {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while saving address !")).build();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while creating address. Please contact the system administrator ! ", exception);
        }
    }

    public Response updateAddress(String partyIdentifier, Long partyAddressId, UpdateAddressCmd updateAddressCmd) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            updateAddressCmd.setPartyAddressId(partyAddressId.intValue());
            cryptoUtil.encryptObjectUsingConfiguredField(updateAddressCmd);
            Set<UpdateAddressCmd> updateAddressCmdSet = Collections.singleton(updateAddressCmd);
            if (partyId != null && partyTransactionRepository.updateAddress(updateAddressCmdSet, partyId)[0]) {
                logger.info("Address updated for PARTY ID {} ", partyId);
                eventBus.post(new UpdateEvent(partyId, updateAddressCmd));
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADDRESS_UPDATED", "Address updated successfully!!", null)).build();
            } else {
                logger.error("Party id not found to updating address record for PARTY ID {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while updating record !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while update record. Please contact the system administrator ! ", exception);
        }
    }

    public Response fetchAddressDetails(String partyIdentifier, Long partyAddressId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            Set<ViewAddressCmd> viewAddressCmdSet = partyRepository.findAddressDetailsByIds(partyId, partyAddressId).stream()
                    .map(address -> cryptoUtil.decryptObjectUsingConfiguredField(address)).collect(Collectors.toSet());
            if (!viewAddressCmdSet.isEmpty()) {
                logger.info("Total address record(s) {} for PARTY ID {} ", viewAddressCmdSet.size(), partyId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADDRESS_FETCH", "Address details found", viewAddressCmdSet)).build();
            } else {
                logger.info("No address record(s) found for PARTY ID {} ", viewAddressCmdSet.size());
                return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found for address!!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetch address details. Please contact the system administrator ! ", exception);
        }
    }

    public Response checkDedupe(DedupeFieldRequestCmd dedupeFieldRequestCmd) {
        var errorMessage = "";
        try {
            cryptoUtil.encryptObjectUsingConfiguredField(dedupeFieldRequestCmd);
            errorMessage = validateDedupeParameters(dedupeFieldRequestCmd);
            if (isEmpty(errorMessage)) {
                List<Long> partiesId = partyRepository.findDedupeDetails(dedupeFieldRequestCmd);
                logger.info("Finding dedupe [count = {}]", partiesId.size());
                var partyResponseCmds = partiesId.stream()
                        .filter(Objects::nonNull)
                        .map(partyId -> cryptoUtil.decryptObjectUsingConfiguredField(fetchPartyDetails(partyId)))
                        .collect(Collectors.toSet());
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DEDUPE", "Total Dedupe count is : " + partyResponseCmds.size(), partyResponseCmds)).build();
            } else {
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, errorMessage)).build();
            }
        } catch (Exception e) {
            errorMessage = "National Id type is PASSPORT then National Id, National Id type code and Nationality code is required to identify dedupe !";
            return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, e.getMessage().contains("PASSPORT") ? errorMessage : e.getMessage())).build();
        }
    }

    private String validateDedupeParameters(DedupeFieldRequestCmd dedupeField) {
        var errorMessage = "";
        if (isEmpty(dedupeField.getMobileNumber()) && isEmpty(dedupeField.getNationalId())
                && isEmpty(dedupeField.getFullName()) && isEmpty(dedupeField.getDateOfBirth())
                && isEmpty(dedupeField.getNationalIdTypeCode())) {
            return "Input parameters are required to identify dedupe !";
        }
        if (isNotBlank(dedupeField.getCountryCode()) && isEmpty(dedupeField.getMobileNumber())) {
            return "Mobile number and country code are required to identify dedupe !";
        }
        if (isNotBlank(dedupeField.getDateOfBirth()) && isEmpty(dedupeField.getFullName())) {
            return "Date Of Birth and Full Name are required to identify dedupe !";
        }
        if (isNotBlank(dedupeField.getNationalIdTypeCode()) && isEmpty(dedupeField.getNationalId())) {
            return "National Id and National Id type code is required to identify dedupe !";
        }
        return errorMessage;
    }

    public Response getPartyDetailsByPassedParameter(
            String sortingOrder, Boolean isDelete, PartyDistinctiveSearchCmd partySearchData, Integer limit, Integer offset) {
        Long partyId = partyRepository.findIdByPartyIdentifier(partySearchData.getPartyIdentifier());
        cryptoUtil.encryptObjectUsingConfiguredField(partySearchData);
        try {
            String orderByField = "";
            if (partySearchData.getLastName() != null) {
                orderByField = orderByField.concat("last_name");
            } else {
                orderByField = orderByField.concat("first_name");
            }
            var parties = PartyMapper.MAPPER.mapPartyRecordCmdList(partyRepository.fetchPartyDetailsByPassedParameter(partyId, partySearchData, orderByField, sortingOrder, limit, offset, isDelete))
                    .stream().map(party -> cryptoUtil.decryptObjectUsingConfiguredField(party)).toList();
            logger.info("Total search count {} ", parties.stream().count());
            if (!parties.isEmpty()) {
                PartySearchResponseCmd partySearchResponseCmd = PartySearchResponseCmd.builder().limit(limit).offset(offset).total(parties.stream().count()).result(parties).build();
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("SEARCH_RECORD_FOUND", "Search result found : ", partySearchResponseCmd)).build();
            } else {
                return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while searching record . Please contact the system administrator! ", exception);
        }
    }

    public Response deleteAddressByPassedParameter(String partyIdentifier, Long partyAddressId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            var status = partyRepository.deleteAddressByPassedParameter(partyId, partyAddressId);
            if (status) {
                eventBus.post(new DeleteEvent(partyId));
                logger.info("Deleting party address for party id {} and party address id {}", partyId, partyAddressId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADDRESS_DELETE", "Address deleted successfully!", null)).build();
            } else {
                logger.info("Unable to delete address record for party id {} and address id {} ", partyId, partyAddressId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while deleting address!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while deleting address. Please contact the system administrator!", exception);
        }
    }

    public Response saveContacts(Set<CreateContactDetailsCmd> createContactDetailsCmd, String partyIdentifier) {
        createContactDetailsCmd.stream().forEach(x -> validateContact(x.getContactTypeCode(), x.getContactValue()));
        cryptoUtil.encryptObjectUsingConfiguredField(createContactDetailsCmd);
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            if (partyId != null && partyTransactionRepository.saveContact(createContactDetailsCmd, partyId)) {
                logger.info("Contact created for party id {} ", partyId);
                var contactDetailsCmdSet = partyRepository.findContactDetailsById(partyId, null)
                        .stream().map(contact -> cryptoUtil.decryptObjectUsingConfiguredField(contact)).collect(Collectors.toSet());
                Optional<CreateContactDetailsCmd> contactRecord = createContactDetailsCmd.stream().findFirst();
                if (contactRecord.isPresent()) {
                    eventBus.post(new UpdateEvent(partyId, contactRecord.get()));
                }
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("CONTACT_DETAILS_CREATED", "Contact saved successfully!!", contactDetailsCmdSet)).build();
            } else {
                logger.error("Party id {} not found for saving contact details ", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while creating contact")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while saving contact. Please contact the system administrator!! ", exception);
        }
    }

    public Response updateContacts(String partyIdentifier, Long partyContactDetailsId, UpdateContactDetailsCmd contactDetailsCmd) {
        validateContact(contactDetailsCmd.getContactTypeCode(), contactDetailsCmd.getContactValue());
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            contactDetailsCmd.setPartyContactDetailsId(partyContactDetailsId.intValue());
            cryptoUtil.encryptObjectUsingConfiguredField(contactDetailsCmd);
            if (partyId != null && partyTransactionRepository.updateContactDetails(partyId, contactDetailsCmd)) {
                logger.info("Contact updated for party id {} ", partyId);
                eventBus.post(new UpdateEvent(partyId, contactDetailsCmd));
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONTACT_DETAILS_UPDATED", "Contact updated successfully!!", null)).build();
            } else {
                logger.error("Party id {} not found to updating contact details", partyIdentifier);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Party Id not found to update contact!!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while updating contact.Please contact the system administrator!! ", exception);
        }
    }

    public Response fetchContactDetails(String partyIdentifier, Long partyContactDetailsId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            Set<ViewContactDetailsCmd> viewContactDetailsCmdSet = partyRepository.findContactDetailsById(partyId, partyContactDetailsId)
                    .stream().filter(Objects::nonNull).map(obj -> cryptoUtil.decryptObjectUsingConfiguredField(obj)).collect(Collectors.toSet());
            if (!viewContactDetailsCmdSet.isEmpty()) {
                logger.info("Total contact record(s) {} for party id {} ", viewContactDetailsCmdSet.size(), partyId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONTACT_DETAILS_FOUND", "Contact details found !", viewContactDetailsCmdSet)).build();
            } else {
                logger.info("No contact record(s) found for party id{} ", viewContactDetailsCmdSet.size());
                return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found for contact!!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetch contact details.Please contact the system administrator! ", exception);
        }
    }

    public Response deleteContact(String partyIdentifier, Long contactId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            var status = partyRepository.deleteContact(partyId, contactId);
            if (status) {
                eventBus.post(new DeleteEvent(partyId));
                logger.info("Deleting party contact for party id {} and party contact id {}", partyId, contactId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("CONTACT_DELETE", "Contact details deleted successfully!!", null)).build();
            } else {
                logger.info("Unable to delete contact record for party id {} and contact id {} ", partyId, contactId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while delete contact!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while delete contact.Please contact the system administrator!", exception);
        }
    }

    public Response saveDocument(Set<CreateDocumentCmd> createDocumentCmd, String partyIdentifier) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            if (partyId != null && partyRepository.saveDocument(createDocumentCmd, partyId)[0]) {
                logger.info("Document created for party id {} ", partyId);
                var viewDocumentsCmdSet = partyRepository.findDocumentById(partyId, null);
                Optional<CreateDocumentCmd> documentRecord = createDocumentCmd.stream().findFirst();
                if (documentRecord.isPresent()) {
                    eventBus.post(new UpdateEvent(partyId, documentRecord.get()));
                }
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("DOCUMENT_CREATED", "Document saved successfully!!", viewDocumentsCmdSet)).build();
            } else {
                logger.error("Party id not found for saving document record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while saving document!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while saving document .Please contact the system administrator!! ", exception);
        }
    }

    public Response updateDocuments(String partyIdentifier, Long partyDocumentsId, UpdateDocumentsCmd updateDocumentsCmd) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            updateDocumentsCmd.setPartyDocumentId(partyDocumentsId.intValue());
            Set<UpdateDocumentsCmd> updateDocumentDetailsCmd = Collections.singleton(updateDocumentsCmd);
            if (partyId != null && partyRepository.updateDocument(updateDocumentDetailsCmd, partyId)[0]) {
                logger.info("Document updated for party id {} ", partyId);
                eventBus.post(new UpdateEvent(partyId, updateDocumentsCmd));
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DOCUMENT_DETAILS_UPDATED", "Document updated successfully!!", null)).build();
            } else {
                logger.error("Party id not found for updating document record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while updating document!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while updating document.Please contact the system administrator! ", exception);
        }
    }

    public Response fetchDocuments(String partyIdentifier, Long partyDocumentsId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            Set<ViewDocumentsCmd> viewDocumentsCmdSet = partyRepository.findDocumentById(partyId, partyDocumentsId);
            if (!viewDocumentsCmdSet.isEmpty()) {
                logger.info("Total document record(s) {} for party id {} ", viewDocumentsCmdSet.size(), partyId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DOCUMENT_RECORD_FOUND", "Document record found !", viewDocumentsCmdSet)).build();
            } else {
                logger.info("No document record(s) found for party id {} ", viewDocumentsCmdSet.size());
                return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found for document!!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetch document details.Please contact the system administrator!", exception);
        }
    }

    public Response deleteDocuments(String partyIdentifier, Long documentId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            var status = partyRepository.deleteDocument(partyId, documentId);
            if (status) {
                eventBus.post(new DeleteEvent(partyId));
                logger.info("Deleting party document for party id {} and party document id {}", partyId, documentId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DOCUMENT_DELETE", "Document details deleted successfully!!", null)).build();
            } else {
                logger.info("Unable to delete document record for party id {} and document id {} ", partyId, documentId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while deleting document !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while deleting document .Please contact the system administrator!", exception);
        }
    }

    public Response saveRisk(Set<CreateRisksCmd> createRisksCmd, String partyIdentifier) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            if (partyId != null && partyRepository.saveRisks(createRisksCmd, partyId)[0]) {
                logger.info("Risk created for party id {} ", partyId);
                var riskByIds = partyRepository.findRiskById(partyId, null);
                Optional<CreateRisksCmd> riskRecord = createRisksCmd.stream().findFirst();
                if (riskRecord.isPresent()) {
                    eventBus.post(new UpdateEvent(partyId, riskRecord.get()));
                }
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("RISK_CREATED", "Risk created successfully!!", riskByIds)).build();
            } else {
                logger.error("Party id not found for saving risk record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while saving risk!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while saving risk .Please contact the system administrator! ", exception);
        }
    }

    public Response updateRisk(String partyIdentifier, Long partyRiskId, UpdateRisksCmd updateRisksCmd) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            updateRisksCmd.setPartyRiskId(partyRiskId.intValue());
            Set<UpdateRisksCmd> updateRiskCmd = Collections.singleton(updateRisksCmd);
            if (partyId != null && partyRepository.updateRisks(updateRiskCmd, partyId)[0]) {
                logger.info("Risk updated for party id {} ", partyId);
                eventBus.post(new UpdateEvent(partyId, updateRisksCmd));
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("RISK_DETAILS_UPDATED", "Risk updated successfully!!", null)).build();
            } else {
                logger.error("Party id not found to updating risk record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while updating risk!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while updating risk .Please contact the system administrator! ", exception);
        }
    }

    public Response fetchRisks(String partyIdentifier, Long partyRiskId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            Set<ViewRisksCmd> viewRisksCmdSet = partyRepository.findRiskById(partyId, partyRiskId);
            if (!viewRisksCmdSet.isEmpty()) {
                logger.info("Total risk record(s) {} for party id {} ", viewRisksCmdSet.size(), partyId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("RISK_FETCH", "Risk record found!", viewRisksCmdSet)).build();
            } else {
                logger.info("No risk record(s) found for party id {} ", viewRisksCmdSet.size());
                return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while fetching risk details .Please contact the system administrator!", exception);
        }
    }

    public Response saveFatcaDetails(Set<CreateFatcaDetailsCmd> createFatcaDetailsCmd, String partyIdentifier) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            if (partyId != null && partyRepository.saveFatcaDetails(createFatcaDetailsCmd, partyId)[0]) {
                logger.info("Fatca created for party id {} ", partyId);
                var fatcaByIds = partyRepository.findFatcaById(partyId, null);
                Optional<CreateFatcaDetailsCmd> fatcaRecord = createFatcaDetailsCmd.stream().findFirst();
                if (fatcaRecord.isPresent()) {
                    eventBus.post(new UpdateEvent(partyId, fatcaRecord.get()));
                }
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("FATCA_CREATED", "Fatca details saved successfully!!", fatcaByIds)).build();
            } else {
                logger.error("Party id not found for saving fatca record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while saving fatca details!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while saving fatca details. Please contact the system administrator!", exception);
        }
    }

    public Response updateFatcaDetails(String partyIdentifier, Long partyFatcaDetailsId, UpdateFatcaDetailsCmd updateFatcaDetailsCmd) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            updateFatcaDetailsCmd.setPartyFatcaDetailsId(partyFatcaDetailsId.intValue());
            Set<UpdateFatcaDetailsCmd> updateFatcaCmd = Collections.singleton(updateFatcaDetailsCmd);
            if (partyId != null && partyRepository.updateFatcaDetails(updateFatcaCmd, partyId)[0]) {
                logger.info("Fatca updated for party id {} ", partyId);
                eventBus.post(new UpdateEvent(partyId, updateFatcaDetailsCmd));
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("FATCA_DETAILS_UPDATED", "Fatca details updated successfully!!", null)).build();
            } else {
                logger.error("Party id not found to updating fatca record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while updating fatca details !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while updating fatca details .Please contact the system administrator!", exception);
        }
    }

    public Response fetchFatcaDetails(String partyIdentifier, Long partyFatcaDetailsId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            Set<ViewFatcaDetailsCmd> viewFatcaCmdSet = partyRepository.findFatcaById(partyId, partyFatcaDetailsId);
            if (!viewFatcaCmdSet.isEmpty()) {
                logger.info("Total fatca record(s) {} for party id {} ", viewFatcaCmdSet.size(), partyId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("FATCA_DETAILS_FOUND", "Fatca details record found!", viewFatcaCmdSet)).build();
            } else {
                logger.info("No fatca record(s) found for party id {} ", viewFatcaCmdSet.size());
                return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found for fatca!!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetching fatca details . Please contact the system administrator!", exception);
        }
    }

    public Response saveMemos(Set<CreateMemosCmd> createMemosCmd, String partyIdentifier) {
        createMemosCmd.stream().forEach(x -> validateDate(x.getValidFrom(), x.getValidTo()));
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            if (partyId != null && partyRepository.saveMemo(createMemosCmd, partyId)[0]) {
                logger.info("Memo created for party id {} ", partyId);
                var memosByIds = partyRepository.findMemoById(partyId, null);
                Optional<CreateMemosCmd> memoRecord = createMemosCmd.stream().findFirst();
                if (memoRecord.isPresent()) {
                    eventBus.post(new UpdateEvent(partyId, memoRecord.get()));
                }
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("MEMO_CREATED", "Memo details saved successfully!!", memosByIds)).build();
            } else {
                logger.error("Party id not found for saving memo record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while saving memo !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while saving memo.Please contact the system administrator!", exception);
        }
    }

    public Response updateMemos(String partyIdentifier, Long partyMemosId, UpdateMemosCmd updateMemosCmd) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            updateMemosCmd.setPartyMemoId(partyMemosId.intValue());
            Set<UpdateMemosCmd> updateMemosCmdSet = Collections.singleton(updateMemosCmd);
            if (partyId != null && partyRepository.updateMemo(updateMemosCmdSet, partyId)[0]) {
                logger.info("Memo updated for party id {}", partyId);
                eventBus.post(new UpdateEvent(partyId, updateMemosCmd));
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MEMOS_UPDATED", "Memo updated successfully!!", null)).build();
            } else {
                logger.error("Party id not found for updating memo record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while update memos details")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while update memos details .Please contact the system administrator!", exception);
        }
    }

    public Response fetchMemos(String partyIdentifier, Long partyMemosId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            Set<ViewMemosCmd> viewMemoCmdSet = partyRepository.findMemoById(partyId, partyMemosId);
            if (!viewMemoCmdSet.isEmpty()) {
                logger.info("Total memo record(s) {} for party id {} ", viewMemoCmdSet.size(), partyId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MEMO_DETAILS_FOUND", "Memo record found", viewMemoCmdSet)).build();
            } else {
                logger.info("No memo record(s) found for party id {} ", viewMemoCmdSet.size());
                return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found for memo!!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetching memos details .Please contact the system administrator!", exception);
        }
    }

    public void validateContact(String contactTypeCode, String contactValue) {
        var value = partyTransactionRepository.findTypeValueByCode(contactTypeCode);
        if (isNotBlank(value) && MOBILE.equals(value) && ((contactValue.length() < 10 || contactValue.length() > 12) || !isNumeric(contactValue))) {
            throw new InvalidParameterException("VALIDATION_ERROR", "Mobile number should be numeric and in range of 10 to 12 digits");
        }
        if (isNotBlank(value) && EMAIL.equals(value) && !Pattern.matches(EMAIL_REGEX, contactValue)) {
            throw new InvalidParameterException("VALIDATION_ERROR", "Invalid email id ");
        }
    }

    public Response fetchDataUsingElasticSearch(AdvanceSearchRequestCmd searchRequestCmd) {
        try {
            var searchResponse = clientRepository.getDocumentData(searchRequestCmd);
            return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("ADVANCE_SEARCH", "Total record(s) found : ".concat(String.valueOf(searchResponse.stream().count())), searchResponse)).build();
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while performing an advanced search .Please contact the system administrator!", exception);

        }
    }

    public void validateParty(CreatePartyCmd party) {

        DedupeFieldRequestCmd dedupeFieldRequestCmd = DedupeFieldRequestCmd.builder().countryCode(party.getCountryOfResidenceCode())
                .fullName(party.getFullName().trim())
                .mobileNumber(party.getPrimaryMobileNumber())
                .dateOfBirth(party.getDateOfBirth())
                .nationalIdTypeCode(party.getNationalIdTypeCode())
                .nationalId(party.getNationalId())
                .nationalityCode(party.getNationalityCode()).build();
        List<Long> partiesId;

        try {
            partiesId = partyRepository.findDedupeDetails(dedupeFieldRequestCmd);
        } catch (Exception e) {
            throw new ServerSideException("DATABASE ERROR CODE", "Error occurred while performing implicit dedupe. Please contact the system administrator ! ", e);
        }
        if (!partiesId.isEmpty()) {
            throw new DuplicateRecordException("DUPLICATE_DATA_ERROR", "Party record already registered!");
        }
    }

    public Response performUniversalSearchBasedOnUserData(UniversalSearchRequest universalSearchRequest) {
        Map<String, String> fields = new HashMap<>();
        String searchFor = universalSearchRequest.getSearchFor();
        AdvanceSearchRequestCmd advanceSearchRequestCmd = new AdvanceSearchRequestCmd();
        try {
            if (!isValuePresent()) {
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse("UNIVERSAL_SEARCH", "Searchable fields not present")).build();
            }
            configRecord = cryptoUtil.getUniversalSearchConfigData(configRecord);
            if (configRecord != null) {
                if (isNumeric(searchFor)) {
                    configRecord.getConfigData().stream().filter(configData -> NUMBER.equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                    configRecord.getConfigData().stream().filter(configData -> DATE.equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                } else if (isAlpha(searchFor)) {
                    configRecord.getConfigData().stream().filter(configData -> TEXT.equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                } else if (isAlphanumeric(searchFor)) {
                    configRecord.getConfigData().stream().filter(configData -> EMAIL.toLowerCase().equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                    configRecord.getConfigData().stream().filter(configData -> TEXT.equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                } else if (isValidEmail(searchFor) || searchFor.contains("@")) {
                    configRecord.getConfigData().stream().filter(configData -> EMAIL.toLowerCase().equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                } else if (isValidDateOfBirth(searchFor) || searchFor.contains("-")) {
                    configRecord.getConfigData().stream().filter(configData -> DATE.equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                } else {
                    configRecord.getConfigData().stream().filter(configData -> EMAIL.toLowerCase().equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                    configRecord.getConfigData().stream().filter(configData -> TEXT.equalsIgnoreCase(configData.getKey())).findFirst().get().getValues().forEach(field -> fields.put(field, searchFor));
                }
            }
            advanceSearchRequestCmd.setOptional(Collections.singleton(fields));
            var searchResponse = clientRepository.getDocumentData(advanceSearchRequestCmd);
            return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("UNIVERSAL_SEARCH", "Total record(s) found : ".concat(String.valueOf(searchResponse.stream().count())), searchResponse)).build();

        } catch (Exception exception) {
            throw new ServerSideException("UNIVERSAL_SEARCH_ERROR", "Error occurred while performing universal search .Please contact the system administrator!", exception);
        }
    }

    private boolean isValuePresent() {
        return universalSearchFields.containsKey(DATE) && universalSearchFields.containsKey(EMAIL.toLowerCase())
                && universalSearchFields.containsKey(TEXT) && universalSearchFields.containsKey(NUMBER);
    }

    public static boolean isValidEmail(String email) {
        Pattern pat = Pattern.compile(EMAIL_REGEX);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    public static boolean isValidDateOfBirth(String dob) {
        String dobRegex = "(0[1-9]|1[0-9]|2[0-9]|3[0-1]|[1-9])-(0[1-9]|1[0-2]|[1-9])-([0-9]{4})";

        Pattern pat = Pattern.compile(dobRegex);
        return pat.matcher(dob).matches();
    }

    public boolean mapXrefIdWithPartyId(@NotNull ApiResponse apiResponse) {
        Long partyId = partyRepository.findIdByPartyIdentifier(apiResponse.getPartyId());
        String xRefId = apiResponse.getxRefId();
        CreateXrefsCmd xRefRecord = CreateXrefsCmd.builder().xrefId(xRefId)
                .systemCode(apiResponse.getSystemCode()).build();
        boolean[] recordCreated;
        try {
            recordCreated = partyRepository.saveXref(Collections.singleton(xRefRecord), partyId);
        } catch (Exception e) {
            throw new DatabasePersistException("DATABASE ERROR CODE", "Error occurred while saving xref record. Please contact the system administrator ! ", e);
        }
        return recordCreated[0];
    }

    public Response fetchXref(String partyIdentifier) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            Set<ViewXrefsCmd> viewXrefsCmdSet = partyRepository.findXref(partyId);
            if (!viewXrefsCmdSet.isEmpty()) {
                logger.info("Total xref record(s) {} for party id {} ", viewXrefsCmdSet.size(), partyId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("XREF_FETCH", "Xref record found!", viewXrefsCmdSet)).build();
            } else {
                logger.info("No xref record(s) found for party id {} ", viewXrefsCmdSet.size());
                return Response.status(HttpStatus.NOT_FOUND_404).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetching xref details .Please contact the system administrator!", exception);
        }
    }

    public Response saveGuardians(Set<CreateGuardianCmd> createGuardianCmd, String partyIdentifier) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            if (partyId != null && partyRepository.saveGuardian(createGuardianCmd, partyId)[0]) {
                logger.info("Guardian created for party id {} ", partyId);
                var guardianIds = partyRepository.findGuardianById(partyId, null);
                Optional<CreateGuardianCmd> guardianRecord = createGuardianCmd.stream().findFirst();
                if (guardianRecord.isPresent()) {
                    eventBus.post(new UpdateEvent(partyId, guardianRecord.get()));
                }
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("GUARDIAN_CREATED", "Guardian created successfully!!", guardianIds)).build();
            } else {
                logger.error("Party id not found for saving guardian record for party id {}", partyId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while saving guardian!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while saving guardian .Please contact the system administrator! ", exception);
        }
    }

    public Response deleteGuardians(String partyIdentifier, Long partyGuardianId) {
        try {
            Long partyId = partyRepository.findIdByPartyIdentifier(partyIdentifier);
            var status = partyRepository.deleteGuardian(partyId, partyGuardianId);
            if (status) {
                eventBus.post(new DeleteEvent(partyId));
                logger.info("Deleting party document for party id {} and party guardian id {}", partyId, partyGuardianId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("GUARDIAN_DELETE", "Guardian details deleted successfully!!", null)).build();
            } else {
                logger.info("Unable to delete document record for party id {} and guardian id {} ", partyId, partyGuardianId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while deleting guardian !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while deleting guardian .Please contact the system administrator!", exception);
        }
    }

    public Response encryptPIIData(Integer batchSize) {
        try {
            List<Party> parties = partyRepository.findPartyDetailByLimit(batchSize);
            logger.info("Total number of records for encryption in batch are: {}", parties.size());
            parties.stream().forEach(party -> {
                Set<UpdateAddressCmd> updateAddresses = partyRepository.findPartyAddressByPartyId(party.getPartyId());
                Set<UpdateContactDetailsCmd> contactDetails = partyRepository.findContactDetailsByPartyId(party.getPartyId());
                contactDetails.stream().forEach(contactDetail -> contactDetail.setLastVerifiedDate(null));
                UpdatePartyCmd updatePartyCmd = UpdatePartyCmd.builder().partyIdentifier(party.getPartyIdentifier()).firstName(party.getFirstName()).middleName(party.getMiddleName())
                        .lastName(party.getLastName()).fullName(party.getFullName()).primaryEmail(party.getPrimaryEmail())
                        .primaryMobileNumber(party.getPrimaryMobileNumber()).nationalId(party.getNationalId()).build();
                UpdateOccupationDetailCmd updateOccupationDetailCmd = UpdateOccupationDetailCmd.builder().taxId(party.getTaxId()).build();
                UpdatePartyRequestCmd updatePartyRequestCmd = UpdatePartyRequestCmd.builder().party(updatePartyCmd).address(updateAddresses).occupationDetail(updateOccupationDetailCmd).contactDetails(contactDetails).build();
                cryptoUtil.encryptObjectUsingConfiguredField(updatePartyRequestCmd);
                partyTransactionRepository.updateAllPartySection(updatePartyRequestCmd);
            });
            return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("DATA_ENCRYPTION", "Total record(s) found for encryption in this batch : ", parties.size())).build();
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while encrypting data .Please contact the system administrator!", exception);
        }
    }

    public void validateDate(String validFrom, String validTo) {
        try {
            if (isNotBlank(validFrom) && isNotBlank(validTo)) {
                Date fromDate = new SimpleDateFormat("dd-MM-yyyy").parse(validFrom);
                Date toDate = new SimpleDateFormat("dd-MM-yyyy").parse(validTo);
                if ((fromDate).after(toDate)) {
                    throw new InvalidParameterException("VALIDATION_ERROR", "ValidTo date should be greater than validFrom");
                }
            }
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }

}
