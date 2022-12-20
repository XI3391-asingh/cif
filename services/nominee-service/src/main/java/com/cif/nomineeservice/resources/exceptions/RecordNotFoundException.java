package com.cif.nomineeservice.resources.exceptions;

import javax.ws.rs.core.Response;

public class RecordNotFoundException extends RuntimeException {

    public static final Response.Status HTTP_ERROR_CODE = Response.Status.NO_CONTENT;

    public RecordNotFoundException(String message, Exception exception) {
        super(message, exception);
    }

}
