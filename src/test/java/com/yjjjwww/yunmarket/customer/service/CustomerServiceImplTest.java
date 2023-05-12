package com.yjjjwww.yunmarket.customer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.model.CustomerSignInServiceForm;
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
import org.springframework.security.crypto.bcrypt.BCrypt;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private JwtTokenProvider provider;

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
    customerService.signUp(form.toServiceForm());

    //then
    verify(customerRepository, times(1)).save(any(Customer.class));
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
        () -> customerService.signUp(form.toServiceForm()));

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
        () -> customerService.signUp(form.toServiceForm()));

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
        () -> customerService.signUp(form.toServiceForm()));

    //then
    assertEquals(ErrorCode.INVALID_PHONE, exception.getErrorCode());
  }

  @Test
  void customerSignInSuccess() {
    //given
    CustomerSignInServiceForm form = CustomerSignInServiceForm.builder()
        .email("yjjjwww@naver.com")
        .password("zero1111")
        .build();

    Customer customer = Customer.builder()
        .id(1L)
        .email("yjjjwww@naver.com")
        .password(BCrypt.hashpw("zero1111", BCrypt.gensalt()))
        .build();

    given(customerRepository.findByEmail(anyString())).willReturn(Optional.ofNullable(customer));
    given(provider.createToken(anyString(), anyLong(), any())).willReturn("JWT Token");

    //when
    String token = customerService.signIn(form);

    //then
    assertNotNull(token);
  }

  @Test
  void customerSignInFail_LOGIN_CHECK_FAIL_No_Email() {
    //given
    CustomerSignInServiceForm form = CustomerSignInServiceForm.builder()
        .email("yjjjwww@naver.com")
        .password("zero1111")
        .build();

    given(customerRepository.findByEmail(anyString())).willReturn(
        Optional.ofNullable(Customer.builder().build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> customerService.signIn(form));

    //then
    assertEquals(ErrorCode.LOGIN_CHECK_FAIL, exception.getErrorCode());
  }

  @Test
  void customerSignInFail_LOGIN_CHECK_Invalid_Password() {
    //given
    CustomerSignInServiceForm form = CustomerSignInServiceForm.builder()
        .email("yjjjwww@naver.com")
        .password("zero1111")
        .build();

    Customer customer = Customer.builder()
        .id(1L)
        .email("yjjjwww@naver.com")
        .password("Wrong Password")
        .build();

    given(customerRepository.findByEmail(anyString())).willReturn(
        Optional.ofNullable(customer));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> customerService.signIn(form));

    //then
    assertEquals(ErrorCode.LOGIN_CHECK_FAIL, exception.getErrorCode());
  }
}