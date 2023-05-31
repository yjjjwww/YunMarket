package com.yjjjwww.yunmarket.review.controller;

import static org.junit.jupiter.api.Assertions.*;
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
import com.yjjjwww.yunmarket.review.model.ReviewCommentRegisterForm;
import com.yjjjwww.yunmarket.review.model.ReviewRegisterForm;
import com.yjjjwww.yunmarket.review.service.ReviewService;
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
class ReviewControllerTest {

  @MockBean
  private ReviewService reviewService;

  @MockBean
  private JwtTokenProvider provider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerReviewSuccess() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    ReviewRegisterForm form = ReviewRegisterForm.builder()
        .orderId(1L)
        .rating(5)
        .contents("리뷰 내용")
        .build();

    //when
    //then
    mockMvc.perform(post("/review")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void registerReviewFail_SELLER_CANNOT_REGISTER() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);
    ReviewRegisterForm form = ReviewRegisterForm.builder()
        .orderId(1L)
        .rating(5)
        .contents("리뷰 내용")
        .build();

    //when
    //then
    mockMvc.perform(post("/review")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerReviewFail_INVALID_REVIEW_FORM() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    ReviewRegisterForm form = ReviewRegisterForm.builder()
        .orderId(1L)
        .rating(15)
        .contents("")
        .build();

    doThrow(new CustomException(ErrorCode.INVALID_REVIEW_FORM))
        .when(reviewService)
        .register(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/review")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("INVALID_REVIEW_FORM", code);
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerReviewFail_ORDERED_NOT_FOUND() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    ReviewRegisterForm form = ReviewRegisterForm.builder()
        .orderId(1L)
        .rating(5)
        .contents("리뷰")
        .build();

    doThrow(new CustomException(ErrorCode.ORDERED_NOT_FOUND))
        .when(reviewService)
        .register(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/review")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("ORDERED_NOT_FOUND", code);
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerReviewFail_REVIEW_ALREADY_EXIST() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    ReviewRegisterForm form = ReviewRegisterForm.builder()
        .orderId(1L)
        .rating(5)
        .contents("리뷰")
        .build();

    doThrow(new CustomException(ErrorCode.REVIEW_ALREADY_EXIST))
        .when(reviewService)
        .register(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/review")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("REVIEW_ALREADY_EXIST", code);
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerReviewFail_CUSTOMER_NOT_MATCH() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    ReviewRegisterForm form = ReviewRegisterForm.builder()
        .orderId(1L)
        .rating(5)
        .contents("리뷰")
        .build();

    doThrow(new CustomException(ErrorCode.CUSTOMER_NOT_MATCH))
        .when(reviewService)
        .register(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/review")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("CUSTOMER_NOT_MATCH", code);
  }

  @Test
  void getReviewsSuccess() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    //when
    //then
    mockMvc.perform(get("/review?productId=1&page=1&size=1"))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  void getReviewsFail_REVIEW_NOT_FOUND() throws Exception {
    //given
    doThrow(new CustomException(ErrorCode.REVIEW_NOT_FOUND))
        .when(reviewService)
        .getReviews(anyLong(), anyInt(), anyInt());

    //when
    //then
    ResultActions result = mockMvc.perform(get("/review?productId=1&page=1&size=1"));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("REVIEW_NOT_FOUND", code);
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void registerReviewCommentSuccess() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);
    ReviewCommentRegisterForm form = ReviewCommentRegisterForm.builder()
        .reviewId(1L)
        .contents("댓글")
        .build();

    //when
    //then
    mockMvc.perform(post("/review/comment")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void registerReviewCommentFail_CUSTOMER_CANNOT_REGISTER_COMMENT() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);
    ReviewCommentRegisterForm form = ReviewCommentRegisterForm.builder()
        .reviewId(1L)
        .contents("댓글")
        .build();

    //when
    //then
    mockMvc.perform(post("/review/comment")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void registerReviewCommentFail_INVALID_REVIEW_FORM() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);
    ReviewCommentRegisterForm form = ReviewCommentRegisterForm.builder()
        .reviewId(1L)
        .contents("")
        .build();

    doThrow(new CustomException(ErrorCode.INVALID_REVIEW_FORM))
        .when(reviewService)
        .registerReviewComment(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/review/comment")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("INVALID_REVIEW_FORM", code);
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void registerReviewCommentFail_REVIEW_NOT_FOUND() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);
    ReviewCommentRegisterForm form = ReviewCommentRegisterForm.builder()
        .reviewId(1L)
        .contents("댓글")
        .build();

    doThrow(new CustomException(ErrorCode.REVIEW_NOT_FOUND))
        .when(reviewService)
        .registerReviewComment(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/review/comment")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("REVIEW_NOT_FOUND", code);
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void registerReviewCommentFail_SELLER_NOT_MATCH() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);
    ReviewCommentRegisterForm form = ReviewCommentRegisterForm.builder()
        .reviewId(1L)
        .contents("댓글")
        .build();

    doThrow(new CustomException(ErrorCode.SELLER_NOT_MATCH))
        .when(reviewService)
        .registerReviewComment(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/review/comment")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("SELLER_NOT_MATCH", code);
  }
}