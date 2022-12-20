package com.masterdata.masterdataservice.resources;

import com.masterdata.masterdataservice.core.masterdata.services.MasterDataService;
import com.masterdata.masterdataservice.api.*;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;


public class MasterDataResource implements MasterdataApi {
    private final Logger logger = getLogger(MasterDataResource.class);

    private MasterDataService masterdataService;

    public MasterDataResource(MasterDataService masterdataService) {
        this.masterdataService = masterdataService;
    }

    @Override
    public Response create(List<CreateMasterCmd> createMasterCmds) {
        return masterdataService.saveMasterData(createMasterCmds);
    }

    @Override
    public Response update(Long masterDataId, UpdateMasterDataRequestCmd updateMasterDataRequestCmd) {
        logger.info("Updating master data record for {}", masterDataId);
        return masterdataService.updateMasterData(masterDataId, updateMasterDataRequestCmd);
    }

    @Override
    public Response fetchMasterData(String masterDataType) {
        logger.info("Fetch all master data for {} ", masterDataType);
        return masterdataService.fetchAllMasterData(masterDataType);
    }

    @Override
    public Response deleteMasterData(Long masterDataId, String masterDataType) {
        logger.info("Performing master data delete for {} ", masterDataType);
        return masterdataService.deleteMasterData(masterDataId, masterDataType);
    }

    @Override
    public Response createCountryMasterData(List<CreateCountryMasterCmd> createCountryMasterCmd) {
        return masterdataService.saveCountryMasterData(createCountryMasterCmd);
    }

    @Override
    public Response updateCountryMasterData(Long countryMasterDataId, UpdateCountryMasterCmd updateCountryMasterCmd) {
        logger.info("Updating country master data record for id{}", countryMasterDataId);
        return masterdataService.updateCountryMasterData(countryMasterDataId, updateCountryMasterCmd);
    }

    @Override
    public Response fetchCountryMasterData() {
        return masterdataService.fetchAllCountryMasterData();
    }

    @Override
    public Response deleteCountryMasterData(Long countryMasterDataId) {
        logger.info("Performing country master data delete for id{} ", countryMasterDataId);
        return masterdataService.deleteCountryMasterData(countryMasterDataId);
    }


}