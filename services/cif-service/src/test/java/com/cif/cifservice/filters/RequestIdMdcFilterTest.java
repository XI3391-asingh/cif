package com.cif.cifservice.filters;

import io.dropwizard.Configuration;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.slf4j.MDC;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RequestIdMdcFilterTest {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    private Filter subject;

    private final FilterChain mockFilterChain = mock(FilterChain.class);
    private final HttpServletRequest mockRequest = mock(HttpServletRequest.class);
    private final ServletResponse mockResponse = mock(ServletResponse.class);
    private final FilterRegistration.Dynamic dynamic = mock(FilterRegistration.Dynamic.class);

    @BeforeEach
    public void setup() {
        Environment environment = mock(Environment.class);
        Configuration accountServiceConfiguration = mock(Configuration.class);
        ServletEnvironment servletEnvironment = mock(ServletEnvironment.class);
        ArgumentCaptor<Filter> filterCaptor = ArgumentCaptor.forClass(Filter.class);
        doReturn(servletEnvironment).when(environment).servlets();
        doReturn(dynamic).when(servletEnvironment).addFilter(anyString(), filterCaptor.capture());
        new RequestIdMdcBundle().run(accountServiceConfiguration, environment);
        subject = filterCaptor.getValue();
    }

    @AfterEach
    public void cleanup() {
        MDC.clear();
    }


    @Test
    public void shouldAddGivenRequestIdToMDC() throws IOException, ServletException {
        String expectedValue = UUID.randomUUID().toString();
        doReturn(expectedValue).when(mockRequest).getHeader(REQUEST_ID_HEADER);

        doAnswer(ignored -> {
            assertThat(MDC.get(REQUEST_ID_HEADER)).isEqualTo(expectedValue);
            return null;
        }).when(mockFilterChain).doFilter(any(), any());

        subject.doFilter(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

    @Test
    public void shouldAddGeneratedRequestIdToMDC() throws IOException, ServletException {
        doReturn(null).when(mockRequest).getHeader(REQUEST_ID_HEADER);

        doAnswer(ignored -> {
            assertThat(MDC.get(REQUEST_ID_HEADER)).isNotEmpty();
            return null;
        }).when(mockFilterChain).doFilter(any(), any());

        subject.doFilter(mockRequest, mockResponse, mockFilterChain);

        verify(mockFilterChain).doFilter(mockRequest, mockResponse);
    }

}