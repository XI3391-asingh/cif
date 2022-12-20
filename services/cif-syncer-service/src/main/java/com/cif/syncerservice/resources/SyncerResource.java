package com.cif.syncerservice.resources;

import com.cif.syncerservice.api.*;
import com.cif.syncerservice.core.syncer.services.SyncerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;

public class SyncerResource implements SyncerApi {

    private Logger logger = LoggerFactory.getLogger(SyncerResource.class);

    private final SyncerService syncerService;

    public SyncerResource(SyncerService syncerService) {
        this.syncerService = syncerService;
    }

    @Override
    public Response createConfigData(CreateConfigRequest createConfigRequest) {
        logger.info("Creating config data for [system = {}]");
        return syncerService.createConfigData(createConfigRequest);
    }

    @Override
    public Response getConfigData(FetchRequest configRequest) {
        logger.info("Get config data for [system = {}]", configRequest);
        return syncerService.fetchConfigDetailsBySystemCode(configRequest.getSystemCode());
    }

    @Override
    public Response updateConfigData(UpdateConfigRequest updateConfigRequest) {
        logger.info("Updating config data for [system = {}]");
        return syncerService.updateConfigData(updateConfigRequest);
    }

    @Override
    public Response createAdapterData(CreateAdapterRequest createAdapterRequest) {
        logger.info("Creating config data for [system = {}]");
        return syncerService.createAdapterData(createAdapterRequest);
    }

    @Override
    public Response getAdapterData(FetchAdapterRequest adapterRequest) {
        logger.info("Get config data for [adapterId = {}]", adapterRequest.getAdapterId());
        return syncerService.fetchAdapterDetailsBySystemCodeOrSystemId(adapterRequest.getAdapterId());
    }

    @Override
    public Response updateAdapterData(UpdateAdapterRequest updateAdapterRequest) {
        logger.info("Updating adapter data for [system = {}]", updateAdapterRequest.getSourceSystemCode());
        return syncerService.updateAdapterData(updateAdapterRequest);
    }

    @Override
    public Response getSystemDetails() {
        return syncerService.fetchSystemDetails();
    }

    @Override
    public Response updateSystemDetail(SystemRequestCmd systemRequestCmd) {
        return syncerService.updateSystemData(systemRequestCmd);
    }


}
