package com.finx.onboardingservice.resources;

import com.finx.onboardingservice.api.OnboardingApi;
import com.finx.onboardingservice.api.OnboardingResumeCmd;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class OnboardingResource implements OnboardingApi {
    @Override
    public Response resume(OnboardingResumeCmd onboardingResumeCmd) {
        return null;
    }
}
