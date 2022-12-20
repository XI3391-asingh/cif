package com.finx.onboardingservice.resources;

import com.finx.onboardingservice.api.CustomerApi;
import com.finx.onboardingservice.api.MobileNumberCmd;
import com.finx.onboardingservice.core.onboarding.services.CustomerService;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Slf4j
public class CustomerResource implements CustomerApi {

    private final CustomerService customerService;

    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public Response verifyMobileNumber(MobileNumberCmd mobileNumberCmd) {
        log.info("workflow details :: {}", mobileNumberCmd);

        return customerService.verifyMobileNumber(mobileNumberCmd);
    }
}
