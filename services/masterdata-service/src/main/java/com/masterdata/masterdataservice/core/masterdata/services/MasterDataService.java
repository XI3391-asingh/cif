package com.masterdata.masterdataservice.core.masterdata.services;


import com.masterdata.masterdataservice.api.*;
import com.masterdata.masterdataservice.db.MasterDataRepository;
import com.masterdata.masterdataservice.resources.exceptions.DatabasePersistException;
import com.masterdata.masterdataservice.resources.exceptions.InvalidParameterException;
import org.eclipse.jetty.http.HttpStatus;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Set;

import static com.masterdata.masterdataservice.core.masterdata.helper.ResponseHelper.buildErrorResponse;
import static com.masterdata.masterdataservice.core.masterdata.helper.ResponseHelper.buildSuccessResponse;
import static org.slf4j.LoggerFactory.getLogger;

public class MasterDataService {
    private final Logger logger = getLogger(MasterDataService.class);
    public static final String ERROR = "ERROR";

    public static final String LOOKUP_MASTER = "LOOKUP_MASTER";

    public static final String ADDRESS_MASTER = "ADDRESS_MASTER";

    public static final String COUNTRY_MASTER = "COUNTRY_MASTER";

    public static final String DATA_PERSIST_ERROR_CODE = "DATA_PERSIST_ERROR";

    public static final String DATA_NOT_FOUND_ERROR_CODE = "DATA_NOT_FOUND_ERROR";
    public static final String NO_RECORD_FOUND = "NO_RECORD_FOUND";

    public static final String INVALID_CATEGORY_TYPE = "Invalid category type";
    private MasterDataRepository masterdataRepository;

    public MasterDataService(MasterDataRepository masterdataRepository) {
        this.masterdataRepository = masterdataRepository;
    }

    public Response saveMasterData(List<CreateMasterCmd> createMasterCmds) {
        try {
            boolean valid;
            Set<ViewMasterDataCmd> viewMasterDataCmdSet = null;
            String dtType = createMasterCmds.stream().findFirst().get().getDtType().value();
            switch (dtType) {
                case LOOKUP_MASTER:
                    createMasterCmds.stream().forEach(x -> checkIfRecordExist(x.getCode(), LOOKUP_MASTER));
                    valid = masterdataRepository.saveLookUpMasterData(createMasterCmds)[0];
                    if (valid) {
                        viewMasterDataCmdSet = masterdataRepository.findAllLookUpMasterData();
                    }
                    break;
                case ADDRESS_MASTER:
                    createMasterCmds.stream().forEach(x -> checkIfRecordExist(x.getCode(), ADDRESS_MASTER));
                    valid = masterdataRepository.saveAddressMasterData(createMasterCmds)[0];
                    if (valid) {
                        viewMasterDataCmdSet = masterdataRepository.findAllAddressMasterData();
                    }
                    break;
                default:
                    logger.error(INVALID_CATEGORY_TYPE, dtType);
                    return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, INVALID_CATEGORY_TYPE)).build();
            }
            if (valid) {
                logger.info("Master data created for {}", dtType);
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("MASTER_DATA_CREATED", "Master data created", viewMasterDataCmdSet)).build();
            } else {
                logger.error("Master data creation failed for {}", dtType);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while saving master data !")).build();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while creating master data. Please contact the system administrator ! ", exception);
        }
    }

    public Response updateMasterData(Long masterDataId, UpdateMasterDataRequestCmd updateMasterDataRequestCmd) {
        try {
            boolean valid;
            String dtType = updateMasterDataRequestCmd.getDtType().value();
            switch (dtType) {
                case LOOKUP_MASTER:
                    valid = masterdataRepository.updateLookUpMasterData(masterDataId, updateMasterDataRequestCmd);
                    break;
                case ADDRESS_MASTER:
                    valid = masterdataRepository.updateAddressMasterData(masterDataId, updateMasterDataRequestCmd);
                    break;
                default:
                    logger.error(INVALID_CATEGORY_TYPE, dtType);
                    return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, INVALID_CATEGORY_TYPE)).build();
            }
            if (valid) {
                logger.info("Master data updated for {} ", dtType);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MASTER_DATA_UPDATED", "Master data updated successfully!!", null)).build();
            } else {
                logger.error("Master data record not found for updating record {}", dtType);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while updating record !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while update record. Please contact the system administrator ! ", exception);
        }
    }

    public Response fetchAllMasterData(String masterDataType) {
        try {
            Set<ViewMasterDataCmd> viewMasterDataCmdSet;
            switch (masterDataType) {
                case LOOKUP_MASTER:
                    viewMasterDataCmdSet = masterdataRepository.findAllLookUpMasterData();
                    break;
                case ADDRESS_MASTER:
                    viewMasterDataCmdSet = masterdataRepository.findAllAddressMasterData();
                    break;
                default:
                    logger.error(INVALID_CATEGORY_TYPE, masterDataType);
                    return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, INVALID_CATEGORY_TYPE)).build();
            }
            if (viewMasterDataCmdSet != null && !viewMasterDataCmdSet.isEmpty()) {
                logger.info("Total master data record(s) {} for category{} ", viewMasterDataCmdSet.size(), masterDataType);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MASTER_DATA_FOUND", "Master data found !", viewMasterDataCmdSet)).build();
            } else {
                logger.info("No master data record(s) found for {}", masterDataType);
                return Response.status(HttpStatus.NO_CONTENT_204).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found for master data!!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetch master data.Please contact the system administrator!", exception);
        }
    }

    public Response deleteMasterData(Long masterDataId, String masterDataType) {
        try {
            boolean valid;
            switch (masterDataType) {
                case LOOKUP_MASTER:
                    valid = masterdataRepository.deleteLookUpMasterData(masterDataId);
                    break;
                case ADDRESS_MASTER:
                    valid = masterdataRepository.deleteAddressMasterData(masterDataId);
                    break;
                default:
                    logger.error(INVALID_CATEGORY_TYPE, masterDataType);
                    return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, INVALID_CATEGORY_TYPE)).build();
            }
            if (valid) {
                logger.info("Deleting master data record for {}", masterDataType);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("MASTER_DATA_DELETE", "Master data deleted successfully!", null)).build();
            } else {
                logger.info("Unable to delete master data record for {} ", masterDataType);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while deleting master data record!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while deleting master data record. Please contact the system administrator!", exception);
        }
    }

    public Response saveCountryMasterData(List<CreateCountryMasterCmd> createCountryMasterCmd) {
        createCountryMasterCmd.stream().forEach(x -> checkIfRecordExist(x.getCode(), COUNTRY_MASTER));
        try {
            if (masterdataRepository.saveCountryMasterData(createCountryMasterCmd)[0]) {
                Set<ViewCountryMasterDataCmd> viewCountryMasterDataCmdSet = masterdataRepository.findAllCountryMasterData();
                logger.info("Country master data created");
                return Response.status(HttpStatus.CREATED_201).entity(buildSuccessResponse("COUNTRY_MASTER_DATA_CREATED", "Country master data created", viewCountryMasterDataCmdSet)).build();
            } else {
                logger.error("Country master data creation failed");
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while saving country master data !")).build();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while creating country master data. Please contact the system administrator ! ", exception);
        }
    }

    private void checkIfRecordExist(String code, String dtType) {
        Long value = null;
        switch (dtType) {
            case LOOKUP_MASTER:
                value = masterdataRepository.findLookupMasterRecordByCode(code);
                break;
            case ADDRESS_MASTER:
                value = masterdataRepository.findAddressMasterRecordByCode(code);
                break;
            case COUNTRY_MASTER:
                value = masterdataRepository.findCountryMasterRecordByCode(code);
                break;
            default:
                throw new InvalidParameterException("Duplicate Record Exist", "Record already present with same code!");
        }
        if (value != null && value != 0) {
            throw new InvalidParameterException("Duplicate Record Exist", "Record already present with same code!");
        }

    }

    public Response updateCountryMasterData(Long countryMasterDataId, UpdateCountryMasterCmd updateCountryMasterCmd) {
        try {
            if (masterdataRepository.updateCountryMasterData(countryMasterDataId, updateCountryMasterCmd)) {
                logger.info("Country master data updated for id {} ", countryMasterDataId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("COUNTRY_MASTER_DATA_UPDATED", "Country master data updated successfully!!", null)).build();
            } else {
                logger.error("Country master data record not found for updating record id {}", countryMasterDataId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while updating record !")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while update record. Please contact the system administrator ! ", exception);
        }
    }

    public Response fetchAllCountryMasterData() {
        try {
            Set<ViewCountryMasterDataCmd> viewCountryMasterDataCmdSet = masterdataRepository.findAllCountryMasterData();

            if (!viewCountryMasterDataCmdSet.isEmpty()) {
                logger.info("Total country master data record(s) {} ", viewCountryMasterDataCmdSet.size());
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("COUNTRY_MASTER_DATA_FOUND", "country master data found !", viewCountryMasterDataCmdSet)).build();
            } else {
                logger.info("No country master data record(s) found ", viewCountryMasterDataCmdSet.size());
                return Response.status(HttpStatus.NO_CONTENT_204).entity(buildErrorResponse(NO_RECORD_FOUND, "No record found for country master data!!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_NOT_FOUND_ERROR_CODE, "Error occurred while fetch country master data.Please contact the system administrator!", exception);
        }
    }

    public Response deleteCountryMasterData(Long countryMasterDataId) {
        try {
            if (masterdataRepository.deleteCountryMasterData(countryMasterDataId)) {
                logger.info("Deleting country master data record for id {}", countryMasterDataId);
                return Response.status(HttpStatus.OK_200).entity(buildSuccessResponse("COUNTRY_MASTER_DATA_DELETE", "Country master data deleted successfully!", null)).build();
            } else {
                logger.info("Unable to delete country master data record for id {} ", countryMasterDataId);
                return Response.status(HttpStatus.BAD_REQUEST_400).entity(buildErrorResponse(ERROR, "Error occurred while deleting country master data record!")).build();
            }
        } catch (Exception exception) {
            throw new DatabasePersistException(DATA_PERSIST_ERROR_CODE, "Error occurred while deleting country master data record. Please contact the system administrator!", exception);
        }
    }

}
