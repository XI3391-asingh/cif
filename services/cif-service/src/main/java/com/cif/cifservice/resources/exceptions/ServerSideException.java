package com.cif.cifservice.resources.exceptions;

import javax.ws.rs.core.Response;

public class ServerSideException extends RuntimeException {
    private final String code;
    private Response.Status httpCode = Response.Status.INTERNAL_SERVER_ERROR;

    public ServerSideException(String code, String message, Exception exception) {
        super(message, exception);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
    public Response.Status getHttpCode() {
        return httpCode;
    }

}
