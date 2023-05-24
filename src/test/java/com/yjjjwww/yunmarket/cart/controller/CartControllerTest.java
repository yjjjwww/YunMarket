package com.yjjjwww.yunmarket.cart.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjjjwww.yunmarket.cart.model.AddCartForm;
import com.yjjjwww.yunmarket.cart.service.CartService;
import com.yjjjwww.yunmarket.common.UserType;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
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
class CartControllerTest {

  @MockBean
  private CartService cartService;

  @MockBean
  private JwtTokenProvider provider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void addCartSuccess() throws Exception {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    //when
    //then
    mockMvc.perform(post("/cart")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void addCartFail_SELLER_CANNOT_ADD_CART() throws Exception {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);

    //when
    //then
    mockMvc.perform(post("/cart")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(form)))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void addCartFail_USER_NOT_FOUND() throws Exception {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    doThrow(new CustomException(ErrorCode.USER_NOT_FOUND))
        .when(cartService)
        .addCart(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/cart")
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
  @WithMockUser(roles = "CUSTOMER")
  void addCartFail_PRODUCT_NOT_FOUND() throws Exception {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    doThrow(new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
        .when(cartService)
        .addCart(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/cart")
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
  @WithMockUser(roles = "CUSTOMER")
  void addCartFail_NOT_ENOUGH_QUANTITY() throws Exception {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    doThrow(new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY))
        .when(cartService)
        .addCart(anyString(), any());

    //when
    //then
    ResultActions result = mockMvc.perform(post("/cart")
        .header("Authorization", "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(form)));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("NOT_ENOUGH_QUANTITY", code);
  }
}