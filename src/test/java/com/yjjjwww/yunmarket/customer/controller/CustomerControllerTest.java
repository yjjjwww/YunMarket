package com.yjjjwww.yunmarket.customer.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjjjwww.yunmarket.customer.model.CustomerSignInForm;
import com.yjjjwww.yunmarket.customer.model.CustomerSignUpForm;
import com.yjjjwww.yunmarket.customer.service.CustomerService;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
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
class CustomerControllerTest {

  @MockBean
  private CustomerService customerService;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  void customerSignUpSuccess() throws Exception {
    //given
    CustomerSignUpForm form = CustomerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .phone("01011112222")
        .address("대한민국 경기도 안양시")
        .build();

    //when
    //then
    mockMvc.perform(post("/customer/signUp")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void customerSignUpFail_ALREADY_SIGNUP_EMAIL() throws Exception {
    //given
    CustomerSignUpForm form = CustomerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .phone("01011112222")
        .address("대한민국 경기도 안양시")
        .build();

    doThrow(new CustomException(ErrorCode.ALREADY_SIGNUP_EMAIL))
        .when(customerService)
        .signUp(form.toServiceForm());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/customer/signUp")
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
  void customerSignUpFail_INVALID_PASSWORD() throws Exception {
    //given
    CustomerSignUpForm form = CustomerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234")
        .phone("01011112222")
        .address("대한민국 경기도 안양시")
        .build();

    doThrow(new CustomException(ErrorCode.INVALID_PASSWORD))
        .when(customerService)
        .signUp(form.toServiceForm());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/customer/signUp")
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
  void customerSignUpFail_INVALID_PHONE() throws Exception {
    //given
    CustomerSignUpForm form = CustomerSignUpForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234")
        .phone("01011112222124352353462436")
        .address("대한민국 경기도 안양시")
        .build();

    doThrow(new CustomException(ErrorCode.INVALID_PHONE))
        .when(customerService)
        .signUp(form.toServiceForm());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/customer/signUp")
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
  void customerSignInSuccess() throws Exception {
    //given
    CustomerSignInForm form = CustomerSignInForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .build();

    //when
    //then
    mockMvc.perform(post("/customer/signIn")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void customerSignInFail_LOGIN_CHECK_FAIL() throws Exception {
    //given
    CustomerSignInForm form = CustomerSignInForm.builder()
        .email("yjjjwww123@naver.com")
        .password("zero1234@")
        .build();

    doThrow(new CustomException(ErrorCode.LOGIN_CHECK_FAIL))
        .when(customerService)
        .signIn(form.toServiceForm());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/customer/signIn")
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