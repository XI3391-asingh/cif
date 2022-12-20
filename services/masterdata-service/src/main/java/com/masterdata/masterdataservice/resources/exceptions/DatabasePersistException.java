package com.masterdata.masterdataservice.resources.exceptions;

public class DatabasePersistException extends ServerSideException {

    private final String code;

    public DatabasePersistException(String code, String message, Exception exception) {
        super(code, message, exception);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
