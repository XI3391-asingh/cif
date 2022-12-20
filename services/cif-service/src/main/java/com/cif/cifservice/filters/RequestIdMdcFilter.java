package com.cif.cifservice.filters;

import org.slf4j.MDC;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

public class RequestIdMdcFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String headerValue = httpRequest.getHeader("X-Request-ID");

        String requestId = headerValue != null ? headerValue : UUID.randomUUID().toString();

        MDC.put("X-Request-ID", requestId);
        try {
            filterChain.doFilter(httpRequest, response);
        } finally {
            MDC.clear();
        }

    }
}