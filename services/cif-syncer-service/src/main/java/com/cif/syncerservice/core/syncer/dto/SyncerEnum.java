package com.cif.syncerservice.core.syncer.dto;

public enum SyncerEnum {
    MISS("CUSTOMER_TITLE_MISS"),
    MR("CUSTOMER_TITLE_MR"),
    MRS("CUSTOMER_TITLE_MRS"),
    MS("CUSTOMER_TITLE_MS"),

    MALE("CUSTOMER_GENDER_MALE"),
    FEMALE("CUSTOMER_GENDER_FEMALE"),
    OTHER("CUSTOMER_GENDER_UNKNOWN"),

    ACTIVE("CUSTOMER_STATUS_ACTIVE"),
    INACTIVE("CUSTOMER_STATUS_FROZEN"),
    CLOSED("CUSTOMER_STATUS_DECEASED"),
    DORMANT("CUSTOMER_STATUS_UNKNOWN"),
    PENDING("CUSTOMER_STATUS_PROSPECT"),
    IDENTIFIER_TYPE_USERNAME("IDENTIFIER_TYPE_USERNAME"),
    CUSTOMER_CONTACT_METHOD_EMAIL("CUSTOMER_CONTACT_METHOD_EMAIL"),
    CUSTOMER_CONTACT_METHOD_SMS("CUSTOMER_CONTACT_METHOD_SMS"),
    CUSTOMER_CONTACT_METHOD_NOTIFICATION("CUSTOMER_CONTACT_METHOD_NOTIFICATION"),
    CUSTOMER_ACCESSIBILITY_UNKNOWN("CUSTOMER_ACCESSIBILITY_UNKNOWN");
    private final String label;

    SyncerEnum(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
