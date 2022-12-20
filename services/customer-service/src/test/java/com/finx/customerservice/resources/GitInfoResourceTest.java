package com.finx.customerservice.resources;

import com.codahale.metrics.MetricRegistry;
import com.finx.customerservice.resources.exceptionmappers.PartyExceptionMapper;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import io.dropwizard.testing.junit5.ResourceExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.core.Response;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(DropwizardExtensionsSupport.class)
class GitInfoResourceTest {

    private final ResourceExtension resourceTestClient =
            ResourceExtension.builder()
                    .addResource(new GitInfoResource())
                    .addProvider(() ->
                            new PartyExceptionMapper(new MetricRegistry()))
                    .build();

    @Test
    void shouldReturnResponseWhenGitPropertiesFound() {
        Response response = resourceTestClient.target("git-info")
                .request()
                .get();

        assertThat(response.readEntity(Map.class).size()).isEqualTo(1);
    }

}