package com.finx.masterdataservice.db;

import com.finx.masterdataservice.core.masterdata.domain.MasterData;
import com.finx.masterdataservice.core.masterdata.enums.IdentityIdEnum;
import java.util.List;

public class IdentityRepositoryImpl implements IdentityRepository {

    @Override
    public List<MasterData> getIdentities() {
        return List.of(
                new MasterData(
                        IdentityIdEnum.NATIONAL_ID.getValue()),
                new MasterData(
                        IdentityIdEnum.CITIZEN_ID_WITH_CHIP.getValue()),
                new MasterData(
                        IdentityIdEnum.CITIZEN_ID_WITHOUT_CHIP.getValue()),
                new MasterData(
                        IdentityIdEnum.VIETNAM_PASSPORT.getValue()));
    }
}
