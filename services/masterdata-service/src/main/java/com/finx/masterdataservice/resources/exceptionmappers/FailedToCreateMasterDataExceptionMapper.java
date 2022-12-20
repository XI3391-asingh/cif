package com.finx.masterdataservice.resources.exceptionmappers;

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.finx.masterdataservice.resources.exceptions.FailedToCreateMasterData;
import io.dropwizard.jersey.errors.ErrorMessage;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class FailedToCreateMasterDataExceptionMapper implements ExceptionMapper<FailedToCreateMasterData> {

    private final Meter exceptions;

    public FailedToCreateMasterDataExceptionMapper(MetricRegistry metricRegistry) {
        exceptions = metricRegistry.meter(MetricRegistry.name(getClass(), "exceptions"));
    }

    @Override
    public Response toResponse(FailedToCreateMasterData e) {
        exceptions.mark();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(new ErrorMessage(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), e.getMessage()))
                .build();
    }
}
