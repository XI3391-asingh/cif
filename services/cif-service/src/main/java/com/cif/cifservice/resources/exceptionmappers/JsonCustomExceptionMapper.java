package com.cif.cifservice.resources.exceptionmappers;

import com.cif.cifservice.api.ErrorResponseApi;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import io.dropwizard.jersey.jackson.JsonProcessingExceptionMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;
import java.util.List;

import static com.cif.cifservice.core.party.helper.ResponseHelper.buildResponseForErrors;

@Provider
public class JsonCustomExceptionMapper extends JsonProcessingExceptionMapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericExceptionMapper.class);

    private final Meter exceptions;

    public JsonCustomExceptionMapper(MetricRegistry metricRegistry, String serviceName) {
        exceptions = metricRegistry.meter(MetricRegistry.name(getClass(), serviceName));
    }

    @Override
    public Response toResponse(JsonProcessingException exception) {
        Response response;
        if (!(exception instanceof JsonGenerationException) && !(exception instanceof InvalidDefinitionException)) {
            LOGGER.debug("Unable to process JSON", exception);
            ErrorResponseApi payLoad = buildResponseForErrors("ERROR", List.of("Unable to process request, check request payload"));
            response = Response.status(Response.Status.BAD_REQUEST).type(MediaType.APPLICATION_JSON_TYPE).entity(payLoad).build();
        } else {
            response = super.toResponse(exception);
        }
        exceptions.mark();
        return response;
    }


}