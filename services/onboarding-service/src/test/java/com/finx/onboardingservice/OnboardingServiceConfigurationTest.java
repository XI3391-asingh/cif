package com.finx.onboardingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

class OnboardingServiceConfigurationTest {

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<OnboardingServiceConfiguration> factory =
            new YamlConfigurationFactory<>(OnboardingServiceConfiguration.class, validator, objectMapper, "dw");

    @Test
    void should_return_valid_data_source_when_properties_present() throws Exception {
        final OnboardingServiceConfiguration configuration = factory.build(new ResourceConfigurationSourceProvider(), "unit-test-config.yml");
        assertThat(configuration).isInstanceOf(Configuration.class);
        assertThat(configuration.getDataSourceFactory().getDriverClass()).isEqualTo("org.h2.Driver");
    }
}
