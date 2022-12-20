package com.cif.cifservice.resources;

import com.cif.cifservice.api.AdminApi;
import com.cif.cifservice.core.party.domain.Config;
import com.cif.cifservice.core.party.services.AdminService;
import org.slf4j.Logger;

import javax.ws.rs.core.Response;


import java.io.InputStream;

import static org.slf4j.LoggerFactory.getLogger;

public class AdminResource implements AdminApi {
    private final AdminService service;

    private final Logger logger = getLogger(AdminResource.class);

    public AdminResource(AdminService service) {
        this.service = service;
    }

    @Override
    public Response addConfigRecord(Config config) {
        logger.info("Creating config for type:{}", config.getType());
        return service.save(config);
    }

    @Override
    public Response fetchConfigByType(String type) {
        logger.info("Fetching config for type:{}", type);
        return service.fetchConfigByRecord(type);
    }

    @Override
    public Response bulkImport(InputStream fileData) {
        logger.info("Step1 : Inside bulkImport method");
        return service.saveBulkImportData(fileData);
    }
}
