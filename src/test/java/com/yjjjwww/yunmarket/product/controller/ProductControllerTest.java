package com.yjjjwww.yunmarket.product.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yjjjwww.yunmarket.common.UserType;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.model.ProductInfo;
import com.yjjjwww.yunmarket.product.model.ProductRegisterForm;
import com.yjjjwww.yunmarket.product.service.ProductService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
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

  @Test
  void getLatestProductsSuccess() throws Exception {
    // given
    List<ProductInfo> productInfos = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productInfos.add(ProductInfo.builder().build());
    }

    Pageable pageable = PageRequest.of(1, 3, Sort.by("createdDate").descending());

    // when
    given(productService.getProducts(pageable)).willReturn(productInfos);

    // then
    MvcResult result = mockMvc.perform(get("/product/list?page=1&size=3&sort=createdDate,desc"))
        .andExpect(status().isOk())
        .andReturn();

    String response = result.getResponse().getContentAsString();
    List<ProductInfo> actualProductInfos = new ObjectMapper().readValue(response,
        new TypeReference<>() {
        });

    assertEquals(3, actualProductInfos.size());
  }

  @Test
  void getLowestPriceProductsSuccess() throws Exception {
    // given
    List<ProductInfo> productInfos = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productInfos.add(ProductInfo.builder().build());
    }

    Pageable pageable = PageRequest.of(1, 3, Sort.by("price").ascending());

    // when
    given(productService.getProducts(pageable)).willReturn(productInfos);

    // then
    MvcResult result = mockMvc.perform(get("/product/list?page=1&size=3&sort=price,asc"))
        .andExpect(status().isOk())
        .andReturn();

    String response = result.getResponse().getContentAsString();
    List<ProductInfo> actualProductInfos = new ObjectMapper().readValue(response,
        new TypeReference<>() {
        });

    assertEquals(3, actualProductInfos.size());
  }

  @Test
  void getMostOrderedProductsSuccess() throws Exception {
    // given
    List<ProductInfo> productInfos = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productInfos.add(ProductInfo.builder().build());
    }

    Pageable pageable = PageRequest.of(1, 3, Sort.by("ordered").descending());

    // when
    given(productService.getProducts(pageable)).willReturn(productInfos);

    // then
    MvcResult result = mockMvc.perform(get("/product/list?page=1&size=3&sort=ordered,desc"))
        .andExpect(status().isOk())
        .andReturn();

    String response = result.getResponse().getContentAsString();
    List<ProductInfo> actualProductInfos = new ObjectMapper().readValue(response,
        new TypeReference<>() {
        });

    assertEquals(3, actualProductInfos.size());
  }

  @Test
  void searchProductsSuccess() throws Exception {
    // given
    List<ProductInfo> productInfos = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productInfos.add(ProductInfo.builder().build());
    }

    // when
    given(productService.searchProducts(anyString(), anyInt(), anyInt())).willReturn(productInfos);

    // then
    MvcResult result = mockMvc.perform(get("/product/search?keyword=product&page=1&size=3"))
        .andExpect(status().isOk())
        .andReturn();

    String response = result.getResponse().getContentAsString();
    List<ProductInfo> actualProductInfos = new ObjectMapper().readValue(response,
        new TypeReference<>() {
        });

    assertEquals(3, actualProductInfos.size());
  }

  @Test
  void getProductInfoSuccess() throws Exception {
    //given
    ProductInfo product = ProductInfo.builder()
        .id(1L)
        .name("상품 이름")
        .price(1000)
        .description("상품 설명")
        .quantity(30)
        .image("이미지 주소")
        .categoryName("상품 카테고리")
        .build();

    //when
    given(productService.getProductInfo(anyLong(), any())).willReturn(product);

    //then
    mockMvc.perform(get("/product/info/1"))
        .andExpect(status().isOk());
  }

  @Test
  void getProductInfoFail_PRODUCT_NOT_FOUND() throws Exception {
    //given

    //when
    doThrow(new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
        .when(productService)
        .getProductInfo(anyLong(), anyString());
    //when
    //then
    ResultActions result = mockMvc.perform(get("/product/info/1"));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("PRODUCT_NOT_FOUND", code);
  }

  @Test
  void getRecentViewedProductsSuccess() throws Exception {
    //given
    List<ProductInfo> productInfoList = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      ProductInfo product = ProductInfo.builder()
          .id((long) i)
          .name("상품 이름" + i)
          .price(1000 + i)
          .description("상품" + i + " 설명")
          .quantity(30 + i)
          .image("이미지 주소")
          .categoryName("상품 카테고리")
          .build();

      productInfoList.add(product);
    }

    //when
    given(productService.getRecentViewedProducts(anyString())).willReturn(productInfoList);

    //then
    mockMvc.perform(get("/product/recent"))
        .andExpect(status().isOk());
  }

  @Test
  void getRecentViewedProductsFail_PRODUCT_NOT_FOUND() throws Exception {
    //given
    doThrow(new CustomException(ErrorCode.PRODUCT_NOT_FOUND))
        .when(productService)
        .getRecentViewedProducts(anyString());
    //when
    //then
    ResultActions result = mockMvc.perform(get("/product/recent"));
    result.andExpect(status().isBadRequest())
        .andDo(print());
    String responseBody = result.andReturn().getResponse().getContentAsString();
    JsonNode responseJson = objectMapper.readTree(responseBody);
    String code = responseJson.get("code").asText();

    assertEquals("PRODUCT_NOT_FOUND", code);
  }
}