package com.finx.sampleservice.resources.exceptions;

public class FailedToCreateTask extends RuntimeException {

    public FailedToCreateTask(String message, Exception exception) {
        super(message, exception);
    }
}
