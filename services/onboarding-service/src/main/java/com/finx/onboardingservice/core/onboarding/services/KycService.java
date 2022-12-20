package com.finx.onboardingservice.core.onboarding.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.onboardingservice.api.ErrorResponseApi;
import com.finx.onboardingservice.api.KycDetailRequest;
import com.finx.onboardingservice.api.SuccessResponseApi;
import com.finx.onboardingservice.core.onboarding.domain.KycDetail;
import com.finx.onboardingservice.core.onboarding.enums.KYCSystem;
import com.finx.onboardingservice.db.KycDetailRepository;
import com.finx.onboardingservice.db.WorkflowOnboardingRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.regex.Pattern;

@Slf4j
public class KycService {

  private final KycDetailRepository kycDetailRepository;
  private final WorkflowOnboardingRepository workflowOnboardingRepository;
  private final ObjectMapper objectMapper;

  private static final Pattern CITIZEN_ID_PATTERN = Pattern.compile("^\\d{12}$");
  private static final Map<String, Pattern> ID_TYPE_PATTERN = Map.of(
      "NATIONAL_ID", Pattern.compile("^\\d{9,12}$"),
      "CITIZEN_ID_WITH_CHIP", CITIZEN_ID_PATTERN,
      "CITIZEN_ID_WITHOUT_CHIP", CITIZEN_ID_PATTERN,
      "VIETNAM_PASSPORT", Pattern.compile("^[a-zA-Z]\\d{6}$"));

  public KycService(KycDetailRepository kycDetailRepository,
      WorkflowOnboardingRepository workflowOnboardingRepository,
      ObjectMapper objectMapper) {
    this.kycDetailRepository = kycDetailRepository;
    this.workflowOnboardingRepository = workflowOnboardingRepository;
    this.objectMapper = objectMapper;
  }

  private String validateKycDetailRequest(KycDetailRequest kycDetailRequest) {
//    workflowOnboardingRepository.countByPhoneNumber(kycDetailRequest.getPhoneNumber());
    String documentType = kycDetailRequest.getDocumentType();
    if (StringUtils.isNotEmpty(documentType)) {
      var pattern = ID_TYPE_PATTERN.get(documentType);
      if (pattern == null || !pattern.matcher(kycDetailRequest.getIdNumber()).find()) {
        return "message";
      }
    }
    return "";
  }

  public Response saveKycDetail(KycDetailRequest kycDetailRequest) {
    try {
//      NTOE: temporary skip the validate request
//      var validateMessage = validateKycDetailRequest(kycDetailRequest);
//
//      if (StringUtils.isNotBlank(validateMessage)) {
//        return Response.ok(ErrorResponseApi.builder()
//                .statusCode(Status.BAD_REQUEST.getStatusCode())
//                .message(validateMessage)
//                .build())
//            .build();
//      }

      String phoneNumber = kycDetailRequest.getPhoneNumber();
      var kycDetail = kycDetailRepository.getKycDetailByPhoneNumber(phoneNumber);
      final String userName = kycDetailRequest.getFullName();
      Consumer<KycDetail> saveToDb;
      if (null == kycDetail) {
        log.info("Not found kyc detail with phone number {} - persist new kyc details", phoneNumber);
        kycDetail = new KycDetail();
        saveToDb = i -> saveKycDetail(i, userName);
        kycDetail.setOriginalKycDetails(objectMapper.writeValueAsString(kycDetailRequest));
      } else {
        log.info("Found kyc detail with phone number {} - update kyc details", phoneNumber);
        saveToDb = i -> updateKycDetail(i, userName);
      }

      Optional.ofNullable(phoneNumber).ifPresent(kycDetail::setMobileNumber);
      kycDetail.setMobileNumberHashcode("dummy");
      Optional.ofNullable(kycDetailRequest.getFullName()).ifPresent(kycDetail::setProspectFullName);
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      Optional.ofNullable(kycDetailRequest.getDateOfBirth()).map(e -> LocalDate.parse(e, formatter))
              .ifPresent(kycDetail::setDateOfBirth);
      kycDetail.setKycSystem(KYCSystem.HYPERVERGE.name());
      Optional.ofNullable(kycDetailRequest.getDocumentType()).ifPresent(kycDetail::setKycType);
      kycDetail.setKycStatus("OCR_SUCCESS");
      Optional.ofNullable(kycDetailRequest.getDocumentType()).ifPresent(kycDetail::setDocumentType);
      Optional.ofNullable(kycDetailRequest.getIdNumber()).ifPresent(kycDetail::setDocumentIdNumber);
      kycDetail.setKycDetails(objectMapper.writeValueAsString(kycDetailRequest));
      Optional.ofNullable(kycDetailRequest.getIsManualVerificationNeeded())
          .ifPresent(kycDetail::setManualVerificationNeeded);
      Optional.ofNullable(kycDetailRequest.getManualVerificationNeededReason())
          .ifPresent(kycDetail::setManualVerificationNeededReason);
      Optional.ofNullable(kycDetailRequest.getIsKycDataModified()).ifPresent(kycDetail::setKycDataModified);

      saveToDb.accept(kycDetail);
      log.info("Save kyc details data success for " + kycDetail.getMobileNumber());
      return Response.ok(SuccessResponseApi.builder()
              .success(true)
              .statusCode(Status.OK.getStatusCode())
              .message("Kyc details save success")
              .build())
          .build();
    } catch (JsonProcessingException e) {
      log.error("Could not save kyc details", e);
      return Response.ok(ErrorResponseApi.builder()
              .statusCode(Status.INTERNAL_SERVER_ERROR.getStatusCode())
              .message("Internal Server Error")
              .build())
          .build();
    }
  }

  private Integer updateKycDetail(KycDetail kycDetail, String userName) {
    kycDetail.setLastUpdatedBy(userName);
    kycDetail.setLastUpdatedDate(LocalDateTime.now());
    return this.kycDetailRepository.update(kycDetail);
  }

  private KycDetail saveKycDetail(KycDetail kycDetail, String userName) {
    kycDetail.setCreatedBy(userName);
    kycDetail.setCreatedDate(LocalDateTime.now());
    kycDetail.setLastUpdatedBy(userName);
    kycDetail.setLastUpdatedDate(LocalDateTime.now());
    return this.kycDetailRepository.save(kycDetail);
  }
}
