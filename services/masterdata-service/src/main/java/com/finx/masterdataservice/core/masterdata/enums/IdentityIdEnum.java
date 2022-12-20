package com.finx.masterdataservice.core.masterdata.enums;

public enum IdentityIdEnum {

    NATIONAL_ID("nationalId"),
    CITIZEN_ID_WITH_CHIP("citizenIdWithChip"),
    CITIZEN_ID_WITHOUT_CHIP("citizenIdWithoutChip"),
    VIETNAM_PASSPORT("vietnamPassport");
    private String value;

    IdentityIdEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
