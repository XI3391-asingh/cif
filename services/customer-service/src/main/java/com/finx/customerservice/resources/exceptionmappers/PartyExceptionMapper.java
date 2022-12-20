package com.finx.customerservice.resources.exceptionmappers;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.finx.customerservice.resources.exceptions.PartyException;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class PartyExceptionMapper implements ExceptionMapper<PartyException> {

    private final Meter exceptions;

    public PartyExceptionMapper(MetricRegistry metricRegistry) {
        exceptions = metricRegistry.meter(MetricRegistry.name(getClass(), "exceptions"));
    }

    @Override
    public Response toResponse(PartyException e) {
        exceptions.mark();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getMessage()))
                .build();
    }
}
