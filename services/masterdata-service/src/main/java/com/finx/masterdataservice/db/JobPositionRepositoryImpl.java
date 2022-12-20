package com.finx.masterdataservice.db;

import com.finx.masterdataservice.core.masterdata.domain.MasterData;
import com.finx.masterdataservice.core.masterdata.enums.JobPositionIdEnum;
import com.finx.masterdataservice.core.masterdata.enums.OccupationIdEnum;
import java.util.List;

public class JobPositionRepositoryImpl implements JobPositionRepository {

    @Override
    public List<MasterData> getJobPositions() {
        return List.of(
                new MasterData(
                        OccupationIdEnum.AGRICULTURE.getValue()),
                new MasterData(
                        OccupationIdEnum.EXTRACTIVE.getValue()),
                new MasterData(
                        OccupationIdEnum.MANUFACTURING.getValue()),
                new MasterData(
                        OccupationIdEnum.PRODUCTION_DISTRIBUTION.getValue()),
                new MasterData(
                        OccupationIdEnum.WATER_SUPPLY.getValue()),
                new MasterData(
                        OccupationIdEnum.CONSTRUCT.getValue()),
                new MasterData(
                        OccupationIdEnum.WHOLESALE_RETAIL.getValue()),
                new MasterData(
                        OccupationIdEnum.TRANSPORTATION.getValue()),
                new MasterData(
                        OccupationIdEnum.STAYING_FOOD.getValue()),
                new MasterData(
                        OccupationIdEnum.INFORMATION_COMMUNICATION.getValue()),
                new MasterData(
                        OccupationIdEnum.FINANCIAL_BANKING_INSURANCE.getValue()),
                new MasterData(
                        OccupationIdEnum.REAL_ESTALE.getValue()),
                new MasterData(
                        OccupationIdEnum.PROFESSIONAL_SCIENCE_TECHNOLOGY.getValue()),
                new MasterData(
                        OccupationIdEnum.ADMINISTRATIVE.getValue()),
                new MasterData(
                        OccupationIdEnum.ACTIVITIES.getValue()),
                new MasterData(
                        OccupationIdEnum.EDUCATION_TRAINING.getValue()),
                new MasterData(
                        OccupationIdEnum.HEALTH_SOCIAL.getValue()),
                new MasterData(
                        OccupationIdEnum.ART_PLAY_ENTERTAINMENT.getValue()),
                new MasterData(
                        JobPositionIdEnum.OTHERS.getValue()));
    }
}
