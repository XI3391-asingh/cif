package com.finx.masterdataservice.core.masterdata.enums;

public enum OccupationIdEnum {

    AGRICULTURE("agriculture"),
    EXTRACTIVE("extractive"),
    MANUFACTURING("manufacturing"),
    PRODUCTION_DISTRIBUTION("productionDistribution"),
    WATER_SUPPLY("waterSupply"),
    CONSTRUCT("construct"),
    WHOLESALE_RETAIL("wholesaleRetail"),
    TRANSPORTATION("transportation"),
    STAYING_FOOD("stayingFood"),
    INFORMATION_COMMUNICATION("informationCommunication"),
    FINANCIAL_BANKING_INSURANCE("financialBankingInsurance"),
    REAL_ESTALE("realEstate"),
    PROFESSIONAL_SCIENCE_TECHNOLOGY("professionalScienceTechnology"),
    ADMINISTRATIVE("administrative"),
    ACTIVITIES("activities"),
    EDUCATION_TRAINING("educationTraining"),
    HEALTH_SOCIAL("healthSocial"),
    ART_PLAY_ENTERTAINMENT("artPlayEntertainment"),
    OTHERS("others");
    private String value;

    OccupationIdEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
