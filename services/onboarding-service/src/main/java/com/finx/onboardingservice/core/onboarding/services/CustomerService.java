package com.finx.onboardingservice.core.onboarding.services;

import com.finx.onboardingservice.api.MobileNumberCmd;
import com.finx.onboardingservice.api.common.CustomResponseApi;
import com.finx.onboardingservice.client.CIFServiceClient;
import com.finx.onboardingservice.client.CommonServiceClient;
import com.finx.onboardingservice.core.onboarding.domain.WorkflowOnboarding;
import com.finx.onboardingservice.db.WorkflowOnboardingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class CustomerService {

    public static final String MOBILE_NUMBER_BLOCKED = "Mobile number is blocked";
    private final WorkflowOnboardingRepository workflowOnboardingRepository;
    private final CIFServiceClient cifServiceClient;
    private final CommonServiceClient commonServiceClient;
    private static final String VALID_MOBILE_NUMBER_REGEX = "^((0[1-9])|([1-9]))([0-9]{8})$";
    private static final String MOBILE_NUMBER_INVALID = "Mobile number is invalid";
    private static final String MOBILE_NUMBER_EXISTED_IN_CIF = "Mobile number existed in CIF System";
    private static final String MOBILE_NUMBER_NOT_EXISTED_IN_CIF = "Mobile number not existed in CIF System";

    private enum STATUS {
        FAILED, COMPLETED
    }

    public CustomerService(CIFServiceClient cifServiceClient, WorkflowOnboardingRepository workflowOnboardingRepository,
                           CommonServiceClient commonServiceClient) {
        this.workflowOnboardingRepository = workflowOnboardingRepository;
        this.cifServiceClient = cifServiceClient;
        this.commonServiceClient = commonServiceClient;
    }

    public Response verifyMobileNumber(MobileNumberCmd mobileNumberCmd) {
        String phoneNumber = mobileNumberCmd.getNumber();
        String reason;
        String status;
        Response response;
        if (isMobileNumberNotValid(phoneNumber)) {
            log.error("Mobile number :: {} is invalid", phoneNumber);
            response = CustomResponseApi.buildResponse(Status.BAD_REQUEST, false, MOBILE_NUMBER_INVALID, null);
        } else {

            if (cifServiceClient.isPhoneNumberExist(phoneNumber)) {
                if (!commonServiceClient.isBlockingPeriodExpired(mobileNumberCmd)) {
                    status = String.valueOf(STATUS.FAILED);
                    reason = MOBILE_NUMBER_BLOCKED;
                    response = CustomResponseApi.buildResponse(Status.FORBIDDEN, false, MOBILE_NUMBER_BLOCKED, null);
                } else {
                    status = String.valueOf(STATUS.FAILED);
                    reason = MOBILE_NUMBER_EXISTED_IN_CIF;
                    log.info("Mobile number :: {} existed in CIF System", phoneNumber);
                    response = CustomResponseApi.buildResponse(Status.CONFLICT, false,
                            MOBILE_NUMBER_EXISTED_IN_CIF, null);
                }
            } else {
                status = String.valueOf(STATUS.COMPLETED);
                reason = MOBILE_NUMBER_NOT_EXISTED_IN_CIF;
                log.info("Mobile number :: {} not existed in CIF System", phoneNumber);
                response = CustomResponseApi.buildResponse(Status.OK, false,
                        MOBILE_NUMBER_NOT_EXISTED_IN_CIF, null);
            }
            WorkflowOnboarding workflowOnboarding = new WorkflowOnboarding();
            workflowOnboarding.setWfId(RandomUtils.nextInt());
            workflowOnboarding.setMobileNumber(phoneNumber);
            workflowOnboarding.setWfDefId(101);
            workflowOnboarding.setWfStageId(1);
            workflowOnboarding.setStageStatus(status);
            workflowOnboarding.setReason(reason);
            workflowOnboarding.setCreatedBy("SYSTEM");
            workflowOnboarding.setCreatedOn(Timestamp.valueOf(LocalDateTime.now()));
            workflowOnboardingRepository.save(workflowOnboarding);

        }
        return response;
    }

    private boolean isMobileNumberNotValid(String number) {
        Pattern pattern = Pattern.compile(VALID_MOBILE_NUMBER_REGEX);
        Matcher matcher = pattern.matcher(number);
        return !matcher.find();
    }
}
