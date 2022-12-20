package com.finx.onboardingservice.core.onboarding.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockitoSession;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.qos.logback.classic.Level;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.onboardingservice.api.KycDetailRequest;
import com.finx.onboardingservice.client.CIFServiceClient;
import com.finx.onboardingservice.core.onboarding.domain.KycDetail;
import com.finx.onboardingservice.db.KycDetailRepository;
import com.finx.onboardingservice.db.WorkflowOnboardingRepository;
import io.dropwizard.logging.BootstrapLogging;
import javax.ws.rs.core.Response.Status;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class KycServiceTest {

  private final KycDetailRepository kycDetailRepository = mock(KycDetailRepository.class);
  private final WorkflowOnboardingRepository workflowOnboardingRepository = mock(WorkflowOnboardingRepository.class);
  private final ObjectMapper objectMapper = mock(ObjectMapper.class);
  private final KycService kycService = new KycService(kycDetailRepository, workflowOnboardingRepository, objectMapper);

  static {
    BootstrapLogging.bootstrap(Level.INFO);
  }

  @Test
  void saveKycDetail_withPhoneNumberNotFoundInKycTable_return200ResponseAndRepoSaveData() {
    var kycDetailRequest = KycDetailRequest.builder().build();
    when(kycDetailRepository.getKycDetailByPhoneNumber(kycDetailRequest.getPhoneNumber())).thenReturn(null);

    var result = kycService.saveKycDetail(kycDetailRequest);

    assertEquals(Status.OK.getStatusCode(), result.getStatus());
    verify(kycDetailRepository).save(Mockito.any());
  }


  @Test
  void saveKycDetail_withPhoneNumberFoundInKycTable_return200ResponseAndRepoUpdateData() {
    var kycDetailRequest = KycDetailRequest.builder().build();
    when(kycDetailRepository.getKycDetailByPhoneNumber(kycDetailRequest.getPhoneNumber())).thenReturn(new KycDetail());

    var result = kycService.saveKycDetail(kycDetailRequest);

    assertEquals(Status.OK.getStatusCode(), result.getStatus());
    verify(kycDetailRepository).update(Mockito.any());
  }

//  @Test
//  void saveKycDetail_withMapperThrowException_return500Response() throws JsonProcessingException {
//    var kycDetailRequest = KycDetailRequest.builder().build();
//    when(objectMapper.writeValueAsString(Mockito.any())).thenThrow(new JsonProcessingException(""));
//
//    var result = kycService.saveKycDetail(kycDetailRequest);
//
//    assertEquals(Status.OK.getStatusCode(), result.getStatus());
//    verify(kycDetailRepository).update(Mockito.any());
//  }
}