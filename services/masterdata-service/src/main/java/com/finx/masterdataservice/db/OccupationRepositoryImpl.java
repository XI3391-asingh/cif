package com.finx.masterdataservice.db;

import com.finx.masterdataservice.core.masterdata.domain.MasterData;
import com.finx.masterdataservice.core.masterdata.enums.JobPositionIdEnum;
import java.util.List;

public class OccupationRepositoryImpl implements OccupationRepository {

    @Override
    public List<MasterData> getOccupations() {
        return List.of(
                new MasterData(
                        JobPositionIdEnum.SENIOR_MANAGER.getValue()),
                new MasterData(
                        JobPositionIdEnum.MIDDLE_MANAGER.getValue()),
                new MasterData(
                        JobPositionIdEnum.SPECIALIST.getValue()),
                new MasterData(
                        JobPositionIdEnum.OTHERS.getValue()));
    }
}
