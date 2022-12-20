package com.finx.masterdataservice.core.masterdata.enums;

public enum JobPositionIdEnum {

    SENIOR_MANAGER("seniorManager"),
    MIDDLE_MANAGER("middleManager"),
    SPECIALIST("specialist"),
    OTHERS("others");
    private String value;

    JobPositionIdEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
