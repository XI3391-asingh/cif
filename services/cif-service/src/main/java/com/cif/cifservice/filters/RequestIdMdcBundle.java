package com.cif.cifservice.filters;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Environment;

import javax.servlet.DispatcherType;
import javax.ws.rs.ext.Provider;
import java.util.EnumSet;

@Provider
public class RequestIdMdcBundle implements ConfiguredBundle<Configuration> {

    @Override
    public void run(Configuration configuration, Environment environment) {
        ServletEnvironment servletEnvironment = environment.servlets();

        var filterRegistration = servletEnvironment.addFilter(
                "RequestIdMdcFilter", new RequestIdMdcFilter());

        filterRegistration.addMappingForUrlPatterns(
                EnumSet.allOf(DispatcherType.class),
                true,
                "/*");
    }
}