package com.cif.nomineeservice.resources.exceptions;

import javax.ws.rs.core.Response;

public class RecordPersistException extends RuntimeException {

    public static final Response.Status HTTP_ERROR_CODE = Response.Status.INTERNAL_SERVER_ERROR;

    public RecordPersistException(String message, Exception exception) {
        super(message, exception);
    }
}
