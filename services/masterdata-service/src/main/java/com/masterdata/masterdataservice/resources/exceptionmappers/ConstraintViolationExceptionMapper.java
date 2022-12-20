package com.masterdata.masterdataservice.resources.exceptionmappers;


import com.masterdata.masterdataservice.core.masterdata.helper.ResponseHelper;
import com.masterdata.masterdataservice.core.masterdata.util.ReadProperties;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Collectors;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {
    private final Meter exceptions;

    public ConstraintViolationExceptionMapper(MetricRegistry metricRegistry, String serviceName) {
        exceptions = metricRegistry.meter(MetricRegistry.name(getClass(), serviceName));
    }

    @Override
    public Response toResponse(final ConstraintViolationException exception) {
        exceptions.mark();
        return Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE)
                .entity(ResponseHelper.buildResponseForErrors("VALIDATION_ERROR", prepareMessage(exception))).build();

    }

    private List<Object> prepareMessage(ConstraintViolationException exception) {
        var readProperties = ReadProperties.getInstance();
        var errorMessages = new LinkedHashSet<>();
        for (ConstraintViolation<?> cv : exception.getConstraintViolations()) {
            errorMessages.add(readProperties.getValue(cv.getPropertyPath().toString()));
        }
        return errorMessages.stream().collect(Collectors.toList());
    }


}
