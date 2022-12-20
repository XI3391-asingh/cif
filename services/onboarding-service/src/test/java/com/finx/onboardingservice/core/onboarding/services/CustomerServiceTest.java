package com.finx.onboardingservice.core.onboarding.services;

import ch.qos.logback.classic.Level;
import com.finx.onboardingservice.api.MobileNumberCmd;
import com.finx.onboardingservice.client.CIFServiceClient;
import com.finx.onboardingservice.client.CommonServiceClient;
import com.finx.onboardingservice.db.WorkflowOnboardingRepository;
import io.dropwizard.logging.BootstrapLogging;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerServiceTest {

    private final CIFServiceClient cifServiceClient = mock(CIFServiceClient.class);
    private final CommonServiceClient commonServiceClient = mock(CommonServiceClient.class);
    private final WorkflowOnboardingRepository workflowOnboardingRepository = mock(WorkflowOnboardingRepository.class);

    private final CustomerService customerService = new CustomerService(cifServiceClient, workflowOnboardingRepository, commonServiceClient);

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    @Test
    void verifyMobileNumber_withCifReturnPhoneExist_returnConflictResponse() {
        when(cifServiceClient.isPhoneNumberExist(Mockito.anyString())).thenReturn(true);
        when(commonServiceClient.isBlockingPeriodExpired(Mockito.any())).thenReturn(true);

        var result = customerService.verifyMobileNumber(MobileNumberCmd.builder().number("791234567").build());

        verify(workflowOnboardingRepository).save(Mockito.any());
        assertEquals(Status.CONFLICT.getStatusCode(), result.getStatus());
    }

    @Test
    void verifyMobileNumber_withCifReturnPhoneNotExist_returnNoContentResponse() {
        when(cifServiceClient.isPhoneNumberExist(Mockito.anyString())).thenReturn(false);
        when(commonServiceClient.isBlockingPeriodExpired(Mockito.any())).thenReturn(true);

        var result = customerService.verifyMobileNumber(MobileNumberCmd.builder().number("791234567").build());

        verify(workflowOnboardingRepository).save(Mockito.any());
        assertEquals(Status.OK.getStatusCode(), result.getStatus());
    }
    @Test
    void should_return_403_is_mobile_number_is_blocked() {
        when(cifServiceClient.isPhoneNumberExist(Mockito.anyString())).thenReturn(true);
        when(commonServiceClient.isBlockingPeriodExpired(Mockito.any())).thenReturn(false);
        var result = customerService.verifyMobileNumber(MobileNumberCmd.builder().number("791234567").build());
        assertEquals(Status.FORBIDDEN.getStatusCode(), result.getStatus());
    }

    @Test
    void should_send_400_is_mobile_number_is_invalid() {
        var result = customerService.verifyMobileNumber(MobileNumberCmd.builder().number("9991234567").build());
        assertEquals(Status.BAD_REQUEST.getStatusCode(), result.getStatus());
    }
}
