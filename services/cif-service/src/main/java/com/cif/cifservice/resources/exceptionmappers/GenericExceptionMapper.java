package com.cif.cifservice.resources.exceptionmappers;

import com.cif.cifservice.api.ErrorResponseApi;
import com.cif.cifservice.resources.exceptions.DatabasePersistException;
import com.cif.cifservice.resources.exceptions.DuplicateRecordException;
import com.cif.cifservice.resources.exceptions.InvalidParameterException;
import com.cif.cifservice.resources.exceptions.ServerSideException;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.List;

import static com.cif.cifservice.core.party.helper.ResponseHelper.buildResponseForErrors;

public class GenericExceptionMapper implements ExceptionMapper<RuntimeException> {
    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionMapper.class);
    private final Meter exceptions;

    public GenericExceptionMapper(MetricRegistry metricRegistry, String serviceName) {
        exceptions = metricRegistry.meter(MetricRegistry.name(getClass(), serviceName));
    }

    @Override
    public Response toResponse(RuntimeException exception) {
        Response response = null;
        if (exception instanceof ServerSideException serverSideException) {
            LOGGER.error("Server Side Exception caught: {}", serverSideException.getCause());
            ErrorResponseApi payLoad = buildResponseForErrors(serverSideException.getCode(), List.of(serverSideException.getMessage()));
            response = Response.status(serverSideException.getHttpCode()).type(MediaType.APPLICATION_JSON_TYPE).entity(payLoad).build();
        } else if (exception instanceof DatabasePersistException databasePersistException) {
            LOGGER.error("Database Persist Exception: {}", databasePersistException.getCause());
            ErrorResponseApi payLoad = buildResponseForErrors(databasePersistException.getCode(), List.of(databasePersistException.getMessage()));
            response = Response.status(databasePersistException.getHttpCode()).type(MediaType.APPLICATION_JSON_TYPE).entity(payLoad).build();
        } else if (exception instanceof DuplicateRecordException duplicateRecordException) {
            LOGGER.error("Duplicate Record Exception: {}", duplicateRecordException.getMessage());
            ErrorResponseApi payLoad = buildResponseForErrors(duplicateRecordException.getCode(), List.of(duplicateRecordException.getMessage()));
            response = Response.status(duplicateRecordException.getHttpCode()).entity(payLoad).build();
        } else if (exception instanceof InvalidParameterException invalidParameterException) {
            LOGGER.error("Invalid Parameter Exception: {}", invalidParameterException.getMessage());
            ErrorResponseApi payLoad = buildResponseForErrors(invalidParameterException.getCode(), List.of(invalidParameterException.getMessage()));
            response = Response.status(invalidParameterException.getHttpCode()).entity(payLoad).build();
        } else if (exception instanceof WebApplicationException webApplicationException) {
            LOGGER.error("Web application exception: {}", webApplicationException.getMessage());
            ErrorResponseApi payLoad = buildResponseForErrors(webApplicationException.getMessage(), List.of("WEB_APPLICATION_EXCEPTION"));
            response = Response.status(webApplicationException.getResponse().getStatus()).type(MediaType.APPLICATION_JSON_TYPE)
                    .entity(payLoad)
                    .build();
        } else {
            LOGGER.error("Exception caught: {}", exception);
            ErrorResponseApi payLoad = buildResponseForErrors("TECHNICAL_ERROR", List.of("TECHNICAL ERROR"));
            response = Response.serverError().type(MediaType.APPLICATION_JSON_TYPE).entity(payLoad).build();
        }
        exceptions.mark();
        return response;
    }


}

