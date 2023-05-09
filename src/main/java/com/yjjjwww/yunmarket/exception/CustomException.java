package com.yjjjwww.yunmarket.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class CustomException extends RuntimeException {

  private final ErrorCode errorCode;
  private final int status;

  public CustomException(ErrorCode errorCode) {
    super(errorCode.getMessage());
    this.errorCode = errorCode;
    this.status = errorCode.getHttpStatus().value();
  }

  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  @Builder
  public static class CustomExceptionResponse {

    private int status;
    private String code;
    private String message;
  }
}
