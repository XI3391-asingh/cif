package com.finx.onboardingservice.resources;

import com.finx.onboardingservice.api.EmailApi;
import com.finx.onboardingservice.api.EmailCmd;
import com.finx.onboardingservice.api.common.CustomResponseApi;
import javax.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.finx.onboardingservice.util.EmailUtil.isEmailNotValid;

public class EmailResource implements EmailApi {

    private static final String EMAIL_ID_VALID = "Email Id is valid";
    private static final String EMAIL_ID_INVALID = "Email Id is invalid";

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailResource.class);

    @Override
    public Response validateEmail(EmailCmd emailCmd) {
        if (isEmailNotValid(emailCmd.getEmailId())) {
            LOGGER.info(EMAIL_ID_INVALID);
            return CustomResponseApi.buildResponse(Response.Status.BAD_REQUEST, false, EMAIL_ID_INVALID, emailCmd);
        }

        LOGGER.info(EMAIL_ID_VALID);
        return CustomResponseApi.buildResponse(Response.Status.OK, true, EMAIL_ID_VALID, emailCmd);
    }
}
