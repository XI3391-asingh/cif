package com.finx.onboardingservice.resources;

import ch.qos.logback.classic.Level;
import com.codahale.metrics.MetricRegistry;
import com.finx.onboardingservice.api.MobileNumberCmd;
import com.finx.onboardingservice.core.onboarding.services.CustomerService;
import com.finx.onboardingservice.resources.exceptionmappers.FailedToStartWorkflowExceptionMapper;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(DropwizardExtensionsSupport.class)
class CustomerResourceTests {

    private final CustomerService mockCustomerService = mock(CustomerService.class);
    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new CustomerResource(mockCustomerService))
                    .addProvider(() ->
                            new FailedToStartWorkflowExceptionMapper(new MetricRegistry()))
                    .build();

    static {
        BootstrapLogging.bootstrap(Level.INFO);
    }

    @Test
    void verifyMobile_withPhoneNotExist_returnNoContentResponse() {
        MobileNumberCmd mobileNumberCmd = getMobileNumber();

        when(mockCustomerService.verifyMobileNumber(mobileNumberCmd)).thenReturn(Response.noContent().build());

        final Response response = resourceTestClient.target("/customer/verify-mobile")
                .request()
                .post(Entity.json(mobileNumberCmd));

        assertThat(response.getStatus()).isEqualTo(Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void verifyMobile_withPhoneExist_returnConflictResponse() {
        MobileNumberCmd mobileNumberCmd = getMobileNumber();

        when(mockCustomerService.verifyMobileNumber(mobileNumberCmd)).thenReturn(Response.status(Status.CONFLICT).build());

        final Response response = resourceTestClient.target("/customer/verify-mobile")
                .request()
                .post(Entity.json(mobileNumberCmd));

        assertThat(response.getStatus()).isEqualTo(Status.CONFLICT.getStatusCode());
    }

    private MobileNumberCmd getMobileNumber() {
        return MobileNumberCmd.builder()
                .number("0791234567")
                .countryCode("+84")
                .build();
    }

}