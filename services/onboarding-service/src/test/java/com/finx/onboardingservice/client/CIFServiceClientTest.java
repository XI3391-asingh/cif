package com.finx.onboardingservice.client;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.ws.rs.InternalServerErrorException;

class CIFServiceClientTest {

  @Test
  void isPhoneNumberExist_3rdPartyReturnDataNotEmpty_returnTrue() throws IOException, InterruptedException {
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
      when(mockHttpResponse.body()).thenReturn("{\"data\":[{\"dummy\":\"dummy\"}]}");

      var result = new CIFServiceClient("localhost:8080").isPhoneNumberExist("0791234567");

      assertTrue(result);
      mockedStaticHttpClient.closeOnDemand();
    }
  }

  @Test
  void isPhoneNumberExist_3rdPartyReturnDataEmpty_returnFalse() throws IOException, InterruptedException {
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
      when(mockHttpResponse.body()).thenReturn("{\"data\":[]}");

      var result = new CIFServiceClient("localhost:8080").isPhoneNumberExist("0791234567");

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
      CIFServiceClient cifServiceClient = new CIFServiceClient("localhost:8080");

      assertThrows(InternalServerErrorException.class, () -> {
        cifServiceClient.isPhoneNumberExist("0791234567");
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
      CIFServiceClient cifServiceClient = new CIFServiceClient("localhost:8080");

      assertThrows(InternalServerErrorException.class, () -> {
        cifServiceClient.isPhoneNumberExist("0791234567");
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
}