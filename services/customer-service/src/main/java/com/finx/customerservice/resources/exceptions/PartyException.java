package com.finx.customerservice.resources.exceptions;

public class PartyException extends RuntimeException {

    public PartyException(String message, Exception exception) {
        super(message, exception);
    }
}
