package com.cif.syncerservice.resources.exceptionmappers;

import com.cif.syncerservice.core.syncer.helper.ResponseHelper;
import com.cif.syncerservice.resources.exceptions.RecordNotFoundException;
import com.cif.syncerservice.resources.exceptions.RecordPersistException;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

public class GenericExceptionMapper implements ExceptionMapper<RuntimeException> {
    public static final Response.Status HTTP_ERROR_CODE = Response.Status.BAD_REQUEST;
    public static final String TECHNICAL_ERROR = "TECHNICAL_ERROR";
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionMapper.class);
    private final Meter exceptions;

    public GenericExceptionMapper(MetricRegistry metricRegistry, String serviceName) {
        exceptions = metricRegistry.meter(MetricRegistry.name(getClass(), serviceName));
    }


    @Override
    public Response toResponse(RuntimeException e) {
        Response response = null;
        LOGGER.error("Exception caught:{}", e.getCause());
        if (e instanceof RecordNotFoundException recordNotFoundException) {
            response = Response.status(RecordPersistException.HTTP_ERROR_CODE.getStatusCode()).entity(
                    ResponseHelper.buildErrorResponse(RecordNotFoundException.HTTP_ERROR_CODE.getReasonPhrase(), e.getMessage())).build();
        } else if (e instanceof RecordPersistException recordPersistException) {
            response = Response.status(RecordPersistException.HTTP_ERROR_CODE.getStatusCode()).entity(
                    ResponseHelper.buildErrorResponse(RecordPersistException.HTTP_ERROR_CODE.getReasonPhrase(), e.getMessage())).build();
        } else {
            response = Response.status(HTTP_ERROR_CODE.getStatusCode()).entity(
                    ResponseHelper.buildErrorResponse(HTTP_ERROR_CODE.getReasonPhrase(), TECHNICAL_ERROR)).build();
        }
        exceptions.mark();
        return response;
    }

}
