package com.finx.onboardingservice.resources.exceptions;

public class FailedToStartWorkflow extends RuntimeException {

    public FailedToStartWorkflow(String message, Exception exception) {
        super(message, exception);
    }
}
