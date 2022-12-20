package com.finx.onboardingservice.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.finx.onboardingservice.api.MobileNumberCmd;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.ws.rs.InternalServerErrorException;
import java.io.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CommonServiceClientTest {

    @Test
    void should_return_true_when_mobile_number_is_blocked() throws IOException, InterruptedException {
        try (MockedStatic<HttpRequest> mockedStaticHttpRequest = mockStatic(HttpRequest.class)) {
            MockedStatic<HttpClient> mockedStaticHttpClient = mockStatic(HttpClient.class);

            var mockBuilder = mock(HttpRequest.Builder.class);
            var mockHttpClient = mock(HttpClient.class);
            var mockHttpResponse = mock(HttpResponse.class);
            // Mocking
            mockedStaticHttpRequest.when(HttpRequest::newBuilder).thenReturn(mockBuilder);
            mockedStaticHttpClient.when(HttpClient::newHttpClient).thenReturn(mockHttpClient);
            when(mockBuilder.uri(Mockito.any())).thenReturn(mockBuilder);
            when(mockBuilder.headers(Mockito.any())).thenReturn(mockBuilder);
            when(mockBuilder.POST(Mockito.any())).thenReturn(mockBuilder);

            when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockHttpResponse);
            when(mockHttpResponse.body()).thenReturn("{\"statusCode\":200,\"message\":\"OK\",\"data\":{\"isBlockingPeriodExpired\":true}}");

            MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

            var result = new CommonServiceClient("localhost:8080").isBlockingPeriodExpired(mobileNumberCmd);

            assertTrue(result);
            mockedStaticHttpClient.closeOnDemand();
        }
    }

    @Test
    void should_return_false_when_mobile_number_is_not_blocked() throws IOException, InterruptedException {
        try (MockedStatic<HttpRequest> mockedStaticHttpRequest = mockStatic(HttpRequest.class)) {
            MockedStatic<HttpClient> mockedStaticHttpClient = mockStatic(HttpClient.class);

            var mockBuilder = mock(HttpRequest.Builder.class);
            var mockHttpClient = mock(HttpClient.class);
            var mockHttpResponse = mock(HttpResponse.class);
            // Mocking
            mockedStaticHttpRequest.when(HttpRequest::newBuilder).thenReturn(mockBuilder);
            mockedStaticHttpClient.when(HttpClient::newHttpClient).thenReturn(mockHttpClient);
            when(mockBuilder.uri(Mockito.any())).thenReturn(mockBuilder);
            when(mockBuilder.headers(Mockito.any())).thenReturn(mockBuilder);
            when(mockBuilder.POST(Mockito.any())).thenReturn(mockBuilder);

            when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenReturn(mockHttpResponse);
            when(mockHttpResponse.body()).thenReturn("{\"statusCode\":200,\"message\":\"OK\",\"data\":{\"isBlockingPeriodExpired\":false}}");

            MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

            var result = new CommonServiceClient("localhost:8080").isBlockingPeriodExpired(mobileNumberCmd);

            assertFalse(result);
            mockedStaticHttpClient.closeOnDemand();
        }
    }

    @Test
    void should_throw_internal_server_exception_when_IOException_is_thrown_from_client() throws IOException, InterruptedException {
        try (MockedStatic<HttpRequest> mockedStaticHttpRequest = mockStatic(HttpRequest.class)) {
            MockedStatic<HttpClient> mockedStaticHttpClient = mockStatic(HttpClient.class);

            HttpClient mockHttpClient = createMocks(mockedStaticHttpRequest, mockedStaticHttpClient);

            when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenThrow(IOException.class);
            CommonServiceClient commonServiceClient = new CommonServiceClient("localhost:8080");
            MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

            assertThrows(InternalServerErrorException.class, () -> {
                commonServiceClient.isBlockingPeriodExpired(mobileNumberCmd);
            });
            mockedStaticHttpClient.closeOnDemand();
        }
    }

    @Test
    void should_throw_internal_server_exception_when_InterruptedException_is_thrown_from_client() throws IOException, InterruptedException {
        try (MockedStatic<HttpRequest> mockedStaticHttpRequest = mockStatic(HttpRequest.class)) {
            MockedStatic<HttpClient> mockedStaticHttpClient = mockStatic(HttpClient.class);

            HttpClient mockHttpClient = createMocks(mockedStaticHttpRequest, mockedStaticHttpClient);

            when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenThrow(InterruptedException.class);
            CommonServiceClient commonServiceClient = new CommonServiceClient("localhost:8080");
            MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

            assertThrows(InternalServerErrorException.class, () -> {
                commonServiceClient.isBlockingPeriodExpired(mobileNumberCmd);
            });
            mockedStaticHttpClient.closeOnDemand();
        }
    }

    @Test
    void should_throw_internal_server_exception_when_JsonProcessingException_is_thrown_from_client() throws IOException, InterruptedException {
        try (MockedStatic<HttpRequest> mockedStaticHttpRequest = mockStatic(HttpRequest.class)) {
            MockedStatic<HttpClient> mockedStaticHttpClient = mockStatic(HttpClient.class);

            HttpClient mockHttpClient = createMocks(mockedStaticHttpRequest, mockedStaticHttpClient);

            when(mockHttpClient.send(Mockito.any(), Mockito.any())).thenThrow(JsonProcessingException.class);
            CommonServiceClient commonServiceClient = new CommonServiceClient("localhost:8080");
            MobileNumberCmd mobileNumberCmd = getMobileNumberCmd();

            assertThrows(InternalServerErrorException.class, () -> {
                commonServiceClient.isBlockingPeriodExpired(mobileNumberCmd);
            });
            mockedStaticHttpClient.closeOnDemand();
        }
    }

    private HttpClient createMocks(MockedStatic<HttpRequest> mockedStaticHttpRequest, MockedStatic<HttpClient> mockedStaticHttpClient) {
        var mockBuilder = mock(HttpRequest.Builder.class);
        var mockHttpClient = mock(HttpClient.class);
        // Mocking
        mockedStaticHttpRequest.when(HttpRequest::newBuilder).thenReturn(mockBuilder);
        mockedStaticHttpClient.when(HttpClient::newHttpClient).thenReturn(mockHttpClient);
        when(mockBuilder.uri(Mockito.any())).thenReturn(mockBuilder);
        when(mockBuilder.headers(Mockito.any())).thenReturn(mockBuilder);
        when(mockBuilder.POST(Mockito.any())).thenReturn(mockBuilder);
        return mockHttpClient;
    }

    private MobileNumberCmd getMobileNumberCmd() {
        MobileNumberCmd mobileNumberCmd = MobileNumberCmd.builder()
                .countryCode("+84")
                .number("0791234567").build();
        return mobileNumberCmd;
    }

}

