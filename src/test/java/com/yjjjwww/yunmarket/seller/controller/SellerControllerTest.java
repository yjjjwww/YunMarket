package com.yjjjwww.yunmarket.seller.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.seller.model.SellerSignInForm;
import com.yjjjwww.yunmarket.seller.model.SellerSignUpForm;
import com.yjjjwww.yunmarket.seller.service.SellerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class SellerControllerTest {

  @MockBean
  private SellerService sellerService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void sellerSignUpSuccess() throws Exception {
    //given
    SellerSignUpForm form = SellerSignUpForm.builder()
        .email("yjjjwww@naver.com")
        .password("zero1234@")
        .phone("01022223333")
        .build();

    //when
    //then
    mockMvc.perform(post("/seller/signUp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void sellerSignUpFail_ALREADY_SIGNUP_EMAIL() throws Exception {
    //given
    SellerSignUpForm form = SellerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .phone("01011112222")
        .build();

    doThrow(new CustomException(ErrorCode.ALREADY_SIGNUP_EMAIL))
        .when(sellerService)
        .signUp(form.toServiceForm());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/seller/signUp")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("ALREADY_SIGNUP_EMAIL", code);
  }

  @Test
  void sellerSignUpFail_INVALID_PASSWORD() throws Exception {
    //given
    SellerSignUpForm form = SellerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234")
        .phone("01011112222")
        .build();

    doThrow(new CustomException(ErrorCode.INVALID_PASSWORD))
        .when(sellerService)
        .signUp(form.toServiceForm());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/seller/signUp")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("INVALID_PASSWORD", code);
  }

  @Test
  void sellerSignUpFail_INVALID_PHONE() throws Exception {
    //given
    SellerSignUpForm form = SellerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234")
        .phone("01011112222124352353462436")
        .build();

    doThrow(new CustomException(ErrorCode.INVALID_PHONE))
        .when(sellerService)
        .signUp(form.toServiceForm());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/seller/signUp")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("INVALID_PHONE", code);
  }

  @Test
  void sellerSignInSuccess() throws Exception {
    //given
    SellerSignInForm form = SellerSignInForm.builder()
        .email("yjjjwww@naver.com")
        .password("zero1234@")
        .build();

    //when
    //then
    mockMvc.perform(post("/seller/signIn")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void sellerSignInFail_LOGIN_CHECK_FAIL() throws Exception {
    //given
    SellerSignInForm form = SellerSignInForm.builder()
        .email("yjjjwww@naver.com")
        .password("zero1234@")
        .build();

    doThrow(new CustomException(ErrorCode.LOGIN_CHECK_FAIL))
        .when(sellerService)
        .signIn(form.toServiceForm());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/seller/signIn")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("LOGIN_CHECK_FAIL", code);
  }
}