package com.finx.commonservice.resources.exceptions;

public class OtpException extends RuntimeException {

    public OtpException(String message, Exception exception) {
        super(message, exception);
    }
}
