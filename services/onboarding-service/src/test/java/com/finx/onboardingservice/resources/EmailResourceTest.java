package com.finx.onboardingservice.resources;

import com.finx.onboardingservice.api.EmailCmd;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
class EmailResourceTest {

    public static final String PATH_VALIDATE_EMAIL = "/customer/validate-email";
    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new EmailResource())
                    .build();

    @Test
    void should_send_200_if_email_id_is_valid() {
        final Response response = resourceTestClient.target(PATH_VALIDATE_EMAIL)
                .request()
                .post(buildValidEmail());
        assertThat(response.getStatus()).isEqualTo(Response.Status.OK.getStatusCode());
    }

    @Test
    void should_send_400_if_email_id_is_invalid() {
        final Response response = resourceTestClient.target(PATH_VALIDATE_EMAIL)
                .request()
                .post(buildInValidEmail());
        assertThat(response.getStatus()).isEqualTo(Response.Status.BAD_REQUEST.getStatusCode());
    }

    private Entity<EmailCmd> buildValidEmail() {
        Entity<EmailCmd> entity = Entity.json(EmailCmd.builder()
                .emailId("test@gmail.com")
                .build());
        return entity;
    }

    private Entity<EmailCmd> buildInValidEmail() {
        Entity<EmailCmd> entity = Entity.json(EmailCmd.builder()
                .emailId("a@b.c")
                .build());
        return entity;
    }
}
