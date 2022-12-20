package com.finx.commonservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

class CommonServiceConfigurationTest {

    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<CommonServiceConfiguration> factory =
            new YamlConfigurationFactory<>(CommonServiceConfiguration.class, validator, objectMapper, "dw");

    @Test
    void should_return_valid_data_source_when_properties_present() throws Exception {
        final CommonServiceConfiguration configuration = factory.build(new ResourceConfigurationSourceProvider(), "unit-test-config.yml");
        assertThat(configuration).isInstanceOf(Configuration.class);
        assertThat(configuration.getDataSourceFactory().getDriverClass()).isEqualTo("org.h2.Driver");
        assertThat(configuration.getBlockingPeriod()).isEqualTo(10);
        assertThat(configuration.getCodeExpirationTime()).isEqualTo(90);
        assertThat(configuration.getMaxValidationAttempt()).isEqualTo(5);
        assertThat(configuration.getOtpAttemptsReset()).isEqualTo(24);
        assertThat(configuration.getMaxResendOtp()).isEqualTo(5);
    }
}
