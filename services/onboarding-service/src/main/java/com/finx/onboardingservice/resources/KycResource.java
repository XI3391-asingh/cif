package com.finx.onboardingservice.resources;

import com.finx.onboardingservice.api.KycApi;
import com.finx.onboardingservice.api.KycDetailRequest;
import com.finx.onboardingservice.core.onboarding.services.KycService;
import javax.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KycResource implements KycApi {

  private final KycService kycService;

  public KycResource(KycService kycService) {
    this.kycService = kycService;
  }

  @Override
  public Response uploadKycDetails(KycDetailRequest kycDetailRequest) {
    log.info("Upload kycDetailRequest for " + kycDetailRequest.getPhoneNumber());
    return kycService.saveKycDetail(kycDetailRequest);
  }
}
