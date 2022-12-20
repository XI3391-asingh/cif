package com.finx.customerservice.resources.exceptionmappers;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.finx.customerservice.resources.exceptions.DatabaseException;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class DatabaseExceptionMapper implements ExceptionMapper<DatabaseException> {

    private final Meter exceptions;

    public DatabaseExceptionMapper(MetricRegistry metricRegistry) {
        exceptions = metricRegistry.meter(MetricRegistry.name(getClass(), "exceptions"));
    }

    @Override
    public Response toResponse(DatabaseException e) {
        exceptions.mark();
        return Response.status(Response.Status.NOT_FOUND)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorMessage(Response.Status.NOT_FOUND.getStatusCode(), e.getMessage()))
                .build();
    }
}
