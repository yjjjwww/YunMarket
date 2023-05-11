package com.yjjjwww.yunmarket.seller.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.seller.model.SellerSignUpServiceForm;
import com.yjjjwww.yunmarket.seller.repository.SellerRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class SellerServiceImplTest {

  @Mock
  private SellerRepository sellerRepository;

  @InjectMocks
  private SellerServiceImpl sellerService;

  @Test
  void sellerSignUpSuccess() {
    //given
    SellerSignUpServiceForm form = SellerSignUpServiceForm.builder()
        .email("yjjwww@naver.com")
        .password("zero1234@")
        .phone("01022223333")
        .build();

    //when
    sellerService.signUp(form);

    //then
    verify(sellerRepository, times(1)).save(any(Seller.class));
  }

  @Test
  void sellerSignUpFail_ALREADY_SIGNUP_EMAIL() {
    //given
    SellerSignUpServiceForm form = SellerSignUpServiceForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .phone("01011112222")
        .build();

    given(sellerRepository.findByEmail(anyString()))
        .willReturn(Optional.ofNullable(Seller.builder().build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> sellerService.signUp(form));

    //then
    assertEquals(ErrorCode.ALREADY_SIGNUP_EMAIL, exception.getErrorCode());
  }

  @Test
  void sellerSignUpFail_INVALID_PASSWORD() {
    //given
    SellerSignUpServiceForm form = SellerSignUpServiceForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234")
        .phone("01011112222")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> sellerService.signUp(form));

    //then
    assertEquals(ErrorCode.INVALID_PASSWORD, exception.getErrorCode());
  }

  @Test
  void sellerSignUpFail_INVALID_PHONE() {
    //given
    SellerSignUpServiceForm form = SellerSignUpServiceForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .phone("010111122221231242151353253241342141")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> sellerService.signUp(form));

    //then
    assertEquals(ErrorCode.INVALID_PHONE, exception.getErrorCode());
  }
}