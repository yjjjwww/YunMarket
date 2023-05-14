package com.yjjjwww.yunmarket.product.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Category;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.product.model.ProductRegisterServiceForm;
import com.yjjjwww.yunmarket.product.repository.CategoryRepository;
import com.yjjjwww.yunmarket.product.repository.ProductRepository;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.seller.repository.SellerRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @Mock
  private SellerRepository sellerRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private CategoryRepository categoryRepository;

  @Mock
  private JwtTokenProvider provider;

  @InjectMocks
  private ProductServiceImpl productService;

  @Test
  void sellerProductRegisterSuccess() {
    //given
    ProductRegisterServiceForm form = ProductRegisterServiceForm.builder()
        .name("판매상품")
        .categoryId(1L)
        .price(1000)
        .description("상품 설명")
        .quantity(400)
        .image("이미지 주소")
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(sellerRepository.findByEmail(anyString())).willReturn(
        Optional.ofNullable(Seller.builder().build()));

    given(categoryRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(Category.builder().build()));

    //when
    productService.register("jwt", form);

    //then
    verify(productRepository, times(1)).save(any(Product.class));
  }

  @Test
  void sellerProductRegisterFail_INVALID_PRODUCT_REGISTER() {
    //given
    ProductRegisterServiceForm form = ProductRegisterServiceForm.builder()
        .name("")
        .categoryId(1L)
        .price(1000)
        .description("상품 설명")
        .quantity(400)
        .image("이미지 주소")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> productService.register("token", form));

    //then
    assertEquals(ErrorCode.INVALID_PRODUCT_REGISTER, exception.getErrorCode());
  }

  @Test
  void sellerProductRegisterFail_USER_NOT_FOUND() {
    //given
    ProductRegisterServiceForm form = ProductRegisterServiceForm.builder()
        .name("판매 상품")
        .categoryId(1L)
        .price(1000)
        .description("상품 설명")
        .quantity(400)
        .image("이미지 주소")
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> productService.register("token", form));

    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void sellerProductRegisterFail_CATEGORY_NOT_FOUND() {
    //given
    ProductRegisterServiceForm form = ProductRegisterServiceForm.builder()
        .name("판매 상품")
        .categoryId(1L)
        .price(1000)
        .description("상품 설명")
        .quantity(400)
        .image("이미지 주소")
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(sellerRepository.findByEmail(anyString())).willReturn(
        Optional.ofNullable(Seller.builder().build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> productService.register("token", form));

    //then
    assertEquals(ErrorCode.CATEGORY_NOT_FOUND, exception.getErrorCode());
  }
}