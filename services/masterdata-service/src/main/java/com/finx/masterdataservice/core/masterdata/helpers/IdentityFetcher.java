package com.finx.masterdataservice.core.masterdata.helpers;

import com.finx.masterdataservice.core.masterdata.domain.MasterData;
import com.finx.masterdataservice.db.IdentityRepositoryImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class IdentityFetcher {

    private static final String BASE_NAME_RESOURCE_MASTERDATA = "masterdata";

    private IdentityRepositoryImpl identityRepository;

    public <R> IdentityFetcher(final R repository) {
        this.identityRepository = (IdentityRepositoryImpl) repository;
    }

    public List<MasterData> getMasterDataByEntity(String acceptLanguage) {
        ResourceBundle bundle = ResourceBundle.getBundle(BASE_NAME_RESOURCE_MASTERDATA, new Locale(acceptLanguage));
        List<MasterData> identities = this.identityRepository.getIdentities();
        List<MasterData> results = new ArrayList<>();
        if (!identities.isEmpty()) {
            for (MasterData item : identities
            ) {
                MasterData data = new MasterData(item.getCode(), bundle.getString(item.getCode()));
                results.add(data);
            }
        }
        return results;
    }
}
