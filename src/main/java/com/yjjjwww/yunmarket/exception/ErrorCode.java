package com.yjjjwww.yunmarket.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
  INVALID_PRODUCT_REGISTER(HttpStatus.BAD_REQUEST, "상품 등록 내용을 확인해주세요."),
  USER_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용자를 찾을 수 없습니다."),
  PRODUCT_NOT_FOUND(HttpStatus.BAD_REQUEST, "상품을 찾을 수 없습니다."),
  NOT_ENOUGH_QUANTITY(HttpStatus.BAD_REQUEST, "수량이 부족합니다."),
  CATEGORY_NOT_FOUND(HttpStatus.BAD_REQUEST, "카테고리를 찾을 수 없습니다."),
  LOGIN_CHECK_FAIL(HttpStatus.BAD_REQUEST, "아이디나 패스워드를 확인해 주세요."),
  INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호는 최소 8자리에 숫자, 문자, 특수문자 각각 1개 이상 포함해야 합니다."),
  INVALID_PHONE(HttpStatus.BAD_REQUEST, "핸드폰 번호 형식을 확인해주세요."),
  ALREADY_SIGNUP_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일입니다.");

  private final HttpStatus httpStatus;
  private final String message;
}
