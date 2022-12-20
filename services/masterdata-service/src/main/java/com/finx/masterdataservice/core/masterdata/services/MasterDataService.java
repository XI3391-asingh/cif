package com.finx.masterdataservice.core.masterdata.services;

import com.finx.masterdataservice.api.common.CustomResponseApi;
import com.finx.masterdataservice.core.masterdata.helpers.IdentityFetcher;
import com.finx.masterdataservice.core.masterdata.helpers.JobPositionFetcher;
import com.finx.masterdataservice.core.masterdata.helpers.OccupationFetcher;
import java.util.HashMap;
import java.util.Map;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MasterDataService {

    private static final String IDENTITY = "identity";
    private static final String OCCUPATION = "occupation";
    private static final String JOBPOSITION = "jobposition";
    private IdentityFetcher identityFetcher;
    private OccupationFetcher occupationFetcher;
    private JobPositionFetcher jobPositionFetcher;

    public MasterDataService(final IdentityFetcher identityFetcher,
                             final OccupationFetcher occupationFetcher,
                             final JobPositionFetcher jobPositionFetcher) {
        this.identityFetcher = identityFetcher;
        this.occupationFetcher = occupationFetcher;
        this.jobPositionFetcher = jobPositionFetcher;
    }

    public Response getMasterDataByEntity(String entityName, String acceptLanguage) {
        Map<String, Object> results = new HashMap<>();
        String[] entities = entityName.split(",");
        for (String item : entities) {
            switch (item.strip().toLowerCase()) {
                case IDENTITY -> results.put(item, identityFetcher.getMasterDataByEntity(acceptLanguage));
                case OCCUPATION -> results.put(item, occupationFetcher.getMasterDataByEntity(acceptLanguage));
                case JOBPOSITION -> results.put(item, jobPositionFetcher.getMasterDataByEntity(acceptLanguage));
                default -> {
                }
            }
        }

        log.info("List master data : ", results);
        return CustomResponseApi.buildResponse(Response.Status.OK, true, "Get master data successful", results);
    }
}
