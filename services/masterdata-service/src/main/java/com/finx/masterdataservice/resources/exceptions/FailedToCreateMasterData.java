package com.finx.masterdataservice.resources.exceptions;

public class FailedToCreateMasterData extends RuntimeException {

    public FailedToCreateMasterData(String message, Exception exception) {
        super(message, exception);
    }
}
