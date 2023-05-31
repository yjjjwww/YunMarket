package com.yjjjwww.yunmarket.question.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjjjwww.yunmarket.common.UserType;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.question.model.AnswerRegisterForm;
import com.yjjjwww.yunmarket.question.model.QuestionRegisterForm;
import com.yjjjwww.yunmarket.question.service.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class QuestionControllerTest {

  @MockBean
  private QuestionService questionService;

  @MockBean
  private JwtTokenProvider provider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerQuestionSuccess() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    QuestionRegisterForm form = QuestionRegisterForm.builder()
        .productId(1L)
        .contents("문의 내용")
        .build();

    //when
    //then
    mockMvc.perform(post("/question")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void registerQuestionFail_SELLER_CANNOT_REGISTER() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);
    QuestionRegisterForm form = QuestionRegisterForm.builder()
        .productId(1L)
        .contents("문의 내용")
        .build();

    //when
    //then
    mockMvc.perform(post("/question")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerQuestionFail_PRODUCT_NOT_FOUND() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    QuestionRegisterForm form = QuestionRegisterForm.builder()
        .productId(1L)
        .contents("문의 내용")
        .build();

    doThrow(new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
        .when(questionService)
        .registerQuestion(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/question")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("PRODUCT_NOT_FOUND", code);
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void registerAnswerSuccess() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);
    AnswerRegisterForm form = AnswerRegisterForm.builder()
        .questionId(1L)
        .contents("답변 내용")
        .build();

    //when
    //then
    mockMvc.perform(post("/question/answer")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerAnswerFail_CUSTOMER_CANNOT_REGISTER() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    AnswerRegisterForm form = AnswerRegisterForm.builder()
        .questionId(1L)
        .contents("답변 내용")
        .build();

    //when
    //then
    mockMvc.perform(post("/question/answer")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void registerAnswerFail_QUESTION_NOT_FOUND() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);
    AnswerRegisterForm form = AnswerRegisterForm.builder()
        .questionId(1L)
        .contents("답변 내용")
        .build();

    doThrow(new CustomException(ErrorCode.QUESTION_NOT_FOUND))
        .when(questionService)
        .registerAnswer(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/question/answer")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("QUESTION_NOT_FOUND", code);
  }

  @Test
  void getQuestionsSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(get("/question?productId=1&page=0&size=5"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void getQuestionsFail_QUESTION_NOT_FOUND() throws Exception {
    //given
    doThrow(new CustomException(ErrorCode.QUESTION_NOT_FOUND))
        .when(questionService)
        .getQuestions(anyLong(), anyInt(), anyInt());

    //when
    //then
    ResultActions result = mockMvc.perform(get("/question?productId=1&page=0&size=5"));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("QUESTION_NOT_FOUND", code);
  }
}