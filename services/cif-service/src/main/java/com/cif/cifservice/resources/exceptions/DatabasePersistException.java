package com.cif.cifservice.resources.exceptions;

public class DatabasePersistException extends ServerSideException {

    private final String code;

    public DatabasePersistException(String code, String message, Exception exception) {
        super(code, message, exception);
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }
}
