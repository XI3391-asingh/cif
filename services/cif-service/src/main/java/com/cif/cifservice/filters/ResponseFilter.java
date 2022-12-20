package com.cif.cifservice.filters;

import com.cif.cifservice.api.ErrorResponseApi;
import com.cif.cifservice.api.ResponseApi;
import com.cif.cifservice.api.SuccessResponseApi;
import com.cif.cifservice.core.party.domain.Event;
import com.cif.cifservice.db.elasticsearch.ElasticsearchClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.time.LocalDateTime;

@Provider
public class ResponseFilter implements ContainerResponseFilter {
    private Logger logger = LoggerFactory.getLogger(ResponseFilter.class);

    private ElasticsearchClientRepository clientRepository;

    @Context
    private ResourceInfo resourceInfo;

    public ResponseFilter(ElasticsearchClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) {
        String eventType = null;
        String requestId = MDC.get("X-Request-ID");
        String methodName = "";
        requestContext.getRequest();
        if (responseContext != null && responseContext.getEntity() instanceof SuccessResponseApi) {
            eventType = ((SuccessResponseApi) responseContext.getEntity()).getStatus().getCode();
        } else if (responseContext != null && responseContext.getEntity() instanceof ErrorResponseApi) {
            eventType = ((ErrorResponseApi) responseContext.getEntity()).getStatus().getCode();
        } else if (responseContext != null && responseContext.getEntity() instanceof ResponseApi) {
            eventType = ((ResponseApi) responseContext.getEntity()).getStatus().getCode();
        }
        Event event = new Event("Onboarding-System", requestId, LocalDateTime.now().toString(), eventType);
        boolean response = clientRepository.postActivityLog(event);
        logger.debug("Activity Posting Response {}", response);
    }
}