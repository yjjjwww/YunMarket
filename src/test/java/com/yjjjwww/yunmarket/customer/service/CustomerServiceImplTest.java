package com.yjjjwww.yunmarket.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.model.CustomerSignUpForm;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {
  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private CustomerServiceImpl customerService;

  @Test
  void customerSignUpSuccess() {
    //given
    CustomerSignUpForm form = CustomerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .phone("01011112222")
        .address("대한민국 경기도 안양시")
        .build();

    //when
    String result = customerService.signUp(form);

    //then
    assertEquals("회원가입 성공", result);
  }

  @Test
  void customerSignUpFail_ALREADY_SIGNUP_EMAIL() {
    //given
    CustomerSignUpForm form = CustomerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .phone("01011112222")
        .address("대한민국 경기도 안양시")
        .build();

    given(customerRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(Customer.builder().build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> customerService.signUp(form));

    //then
    assertEquals(ErrorCode.ALREADY_SIGNUP_EMAIL, exception.getErrorCode());
  }

  @Test
  void customerSignUpFail_INVALID_PASSWORD() {
    //given
    CustomerSignUpForm form = CustomerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234")
        .phone("01011112222")
        .address("대한민국 경기도 안양시")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> customerService.signUp(form));

    //then
    assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
  }

  @Test
  void customerSignUpFail_INVALID_PHONE() {
    //given
    CustomerSignUpForm form = CustomerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .phone("010111122221231242151353253241342141")
        .address("대한민국 경기도 안양시")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> customerService.signUp(form));

    //then
    assertEquals(ErrorCode.INVALID_PHONE, exception.getErrorCode());
  }
}