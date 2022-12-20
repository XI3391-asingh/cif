package com.finx.masterdataservice.resources;

import com.finx.masterdataservice.api.MasterdataApi;
import com.finx.masterdataservice.core.masterdata.services.MasterDataService;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MasterDataResource implements MasterdataApi {
    private MasterDataService masterDataService;

    public MasterDataResource(final MasterDataService masterDataService) {
        this.masterDataService = masterDataService;
    }

    @Override
    public Response masterdata(String entityName, String acceptLanguage) {
        return this.masterDataService.getMasterDataByEntity(entityName, acceptLanguage);
    }
}
