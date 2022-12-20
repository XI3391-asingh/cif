package com.cif.cifservice.resources.exceptions;

import javax.ws.rs.core.Response;

public class DuplicateRecordException extends ServerSideException {

    private final String code;

    private Response.Status httpCode = Response.Status.CONFLICT;


    public DuplicateRecordException(String code, String message) {
        super(code, message, new RuntimeException(message));
        this.code = code;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public Response.Status getHttpCode() {
        return httpCode;
    }
}
