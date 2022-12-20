package com.finx.masterdataservice.core.masterdata.helpers;

import com.finx.masterdataservice.core.masterdata.domain.MasterData;
import com.finx.masterdataservice.db.OccupationRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class OccupationFetcher {

    private static final String BASE_NAME_RESOURCE_MASTERDATA = "masterdata";

    private OccupationRepositoryImpl occupationRepository;

    public <R> OccupationFetcher(final R repository) {
        this.occupationRepository = (OccupationRepositoryImpl) repository;
    }

    public List<MasterData> getMasterDataByEntity(String acceptLanguage) {
        ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME_RESOURCE_MASTERDATA, new Locale(acceptLanguage));
        List<MasterData> occupations = this.occupationRepository.getOccupations();
        List<MasterData> results = new ArrayList<>();
        if (!occupations.isEmpty()) {
            for (MasterData item : occupations
            ) {
                MasterData data = new MasterData(item.getCode(), bundle.getString(item.getCode()));
                results.add(data);
            }
        }
        return results;
    }
}
