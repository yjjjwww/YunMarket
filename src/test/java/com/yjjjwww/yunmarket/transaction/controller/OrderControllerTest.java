package com.yjjjwww.yunmarket.transaction.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
import com.yjjjwww.yunmarket.transaction.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

  @MockBean
  private OrderService orderService;

  @MockBean
  private JwtTokenProvider provider;

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void orderItemsSuccess() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    //when
    //then
    mockMvc.perform(post("/order?point=300")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void orderItemsFail_SELLER_CANNOT_ORDER() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);

    //when
    //then
    mockMvc.perform(post("/order?point=300")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void orderItemsFail_CART_NOT_FOUND() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    doThrow(new CustomException(ErrorCode.CART_NOT_FOUND))
        .when(orderService)
        .orderItems(anyString(), anyInt());
    //when
    //then
    ResultActions result = mockMvc.perform(post("/order?point=100")
        .header("Authorization", "Bearer " + token));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("CART_NOT_FOUND", code);
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void getTotalOrderedItemsSuccess() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    //when
    //then
    mockMvc.perform(get("/order")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void getTotalOrderedItemsFail_SELLER_CANNOT_GET() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);

    //when
    //then
    mockMvc.perform(get("/order")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void getTotalOrderedItemsFail_ORDERED_NOT_FOUND() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    doThrow(new CustomException(ErrorCode.ORDERED_NOT_FOUND))
        .when(orderService)
        .getTotalOrderedItems(anyString());
    //when
    //then
    ResultActions result = mockMvc.perform(get("/order")
        .header("Authorization", "Bearer " + token));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("ORDERED_NOT_FOUND", code);
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void getOrderedItemsByTransactionSuccess() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    //when
    //then
    mockMvc.perform(get("/order/transaction/1")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isOk())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "SELLER")
  void getOrderedItemsByTransactionFail_SELLER_CANNOT_GET() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.SELLER);

    //when
    //then
    mockMvc.perform(get("/order/transaction/1")
            .header("Authorization", "Bearer " + token))
        .andExpect(status().isForbidden())
        .andDo(print());
  }

  @Test
  @WithMockUser(roles = "CUSTOMER")
  void getOrderedItemsByTransactionFail_ORDERED_NOT_FOUND() throws Exception {
    //given
    String token = provider.createToken("yjjjwww@naver.com", 1L, UserType.CUSTOMER);

    doThrow(new CustomException(ErrorCode.ORDERED_NOT_FOUND))
        .when(orderService)
        .getOrderedItemsByTransaction(anyString(), anyLong());
    //when
    //then
    ResultActions result = mockMvc.perform(get("/order/transaction/1")
        .header("Authorization", "Bearer " + token));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("ORDERED_NOT_FOUND", code);
  }
}