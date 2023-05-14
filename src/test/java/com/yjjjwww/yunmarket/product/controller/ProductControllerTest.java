package com.yjjjwww.yunmarket.product.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjjjwww.yunmarket.common.UserType;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.model.ProductRegisterForm;
import com.yjjjwww.yunmarket.product.service.ProductService;
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
class ProductControllerTest {

  @MockBean
  private ProductService productService;

  @MockBean
  private JwtTokenProvider provider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "SELLER")
  void sellerProductRegisterSuccess() throws Exception {
    ProductRegisterForm form = ProductRegisterForm.builder()
        .name("판매상품")
        .categoryId(4L)
        .price(10000)
        .description("판매상품입니다.")
        .quantity(1000)
        .image("이미지주소")
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);

    //when
    //then
    mockMvc.perform(post("/product")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void sellerProductRegisterFail_CUSTOMER_CANNOT_REGISTER() throws Exception {
    ProductRegisterForm form = ProductRegisterForm.builder()
        .name("판매상품")
        .categoryId(4L)
        .price(10000)
        .description("판매상품입니다.")
        .quantity(1000)
        .image("이미지주소")
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    //when
    //then
    mockMvc.perform(post("/product")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void sellerProductRegisterFail_INVALID_PRODUCT_REGISTER() throws Exception {
    ProductRegisterForm form = ProductRegisterForm.builder()
        .name("")
        .categoryId(4L)
        .price(0)
        .description("")
        .quantity(0)
        .image("이미지주소")
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);

    doThrow(new CustomException(ErrorCode.INVALID_PRODUCT_REGISTER))
        .when(productService)
        .register(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/product")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("INVALID_PRODUCT_REGISTER", code);
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void sellerProductRegisterFail_USER_NOT_FOUND() throws Exception {
    ProductRegisterForm form = ProductRegisterForm.builder()
        .name("")
        .categoryId(4L)
        .price(0)
        .description("")
        .quantity(0)
        .image("이미지주소")
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);

    doThrow(new CustomException(ErrorCode.USER_NOT_FOUND))
        .when(productService)
        .register(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/product")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("USER_NOT_FOUND", code);
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void sellerProductRegisterFail_CATEGORY_NOT_FOUND() throws Exception {
    ProductRegisterForm form = ProductRegisterForm.builder()
        .name("")
        .categoryId(4L)
        .price(0)
        .description("")
        .quantity(0)
        .image("이미지주소")
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);

    doThrow(new CustomException(ErrorCode.CATEGORY_NOT_FOUND))
        .when(productService)
        .register(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/product")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("CATEGORY_NOT_FOUND", code);
  }
}