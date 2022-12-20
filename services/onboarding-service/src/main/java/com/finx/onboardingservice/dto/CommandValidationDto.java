package com.finx.onboardingservice.dto;

import javax.ws.rs.core.Response.Status;

public class CommandValidationDto {

  public static final CommandValidationDto SUCCESS = new CommandValidationDto(true, Status.OK, "Success");
  private boolean isSuccess;
  private Status status;
  private String message;
  public CommandValidationDto(boolean isSuccess, Status status, String message) {
    this.isSuccess = isSuccess;
    this.status = status;
    this.message = message;
  }

  public boolean isSuccess() {
    return isSuccess;
  }

  public void setSuccess(boolean success) {
    isSuccess = success;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
