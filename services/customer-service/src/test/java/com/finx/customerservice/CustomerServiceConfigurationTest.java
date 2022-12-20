package com.finx.customerservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.Configuration;
import io.dropwizard.configuration.ResourceConfigurationSourceProvider;
import io.dropwizard.configuration.YamlConfigurationFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jersey.validation.Validators;
import org.junit.jupiter.api.Test;

import javax.validation.Validator;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerServiceConfigurationTest {
    private final ObjectMapper objectMapper = Jackson.newObjectMapper();
    private final Validator validator = Validators.newValidator();
    private final YamlConfigurationFactory<CustomerServiceConfiguration> factory =
            new YamlConfigurationFactory<>(CustomerServiceConfiguration.class, validator, objectMapper, "dw");

    @Test
     void shouldReturnValidDataSourceWhenPropertiesPresent() throws Exception {
        final CustomerServiceConfiguration wid = factory.build(new ResourceConfigurationSourceProvider(), "unit-test-config.yml");
        assertThat(wid).isInstanceOf(Configuration.class);
        assertThat(wid.getDataSourceFactory().getDriverClass()).isEqualTo("org.h2.Driver");
    }

}