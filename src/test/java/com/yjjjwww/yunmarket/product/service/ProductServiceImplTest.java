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
import com.yjjjwww.yunmarket.product.entity.ProductDocument;
import com.yjjjwww.yunmarket.product.model.ProductInfo;
import com.yjjjwww.yunmarket.product.model.ProductRegisterServiceForm;
import com.yjjjwww.yunmarket.product.repository.CategoryRepository;
import com.yjjjwww.yunmarket.product.repository.ElasticSearchProductRepository;
import com.yjjjwww.yunmarket.product.repository.ProductRepository;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.seller.repository.SellerRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

  @Mock
  private SellerRepository sellerRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private ElasticSearchProductRepository elasticSearchProductRepository;

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

  @Test
  void getLatestProductsSuccess() {
    //given
    Pageable pageable = PageRequest.of(1, 3, Sort.by("createdDate").descending());

    List<Product> productList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productList.add(Product.builder()
          .name("상품" + i)
          .price(i + 1)
          .description("상품" + "i" + "설명")
          .quantity(100 + i)
          .image("상품" + "i" + "이미지")
          .category(Category.builder()
              .name("카테고리" + i)
              .build())
          .build());
    }

    Page<Product> productPage = new PageImpl<>(productList);

    given(productRepository.findAll(pageable)).willReturn(productPage);

    //when
    List<ProductInfo> result = productService.getProducts(pageable);

    //then
    assertEquals(3, result.size());
    assertEquals("상품1", result.get(1).getName());
    assertEquals("카테고리1", result.get(1).getCategoryName());
  }

  @Test
  void getLowestPriceProductsSuccess() {
    //given
    Pageable pageable = PageRequest.of(1, 3, Sort.by("price").ascending());

    List<Product> productList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productList.add(Product.builder()
          .name("상품" + i)
          .price(i + 1)
          .description("상품" + "i" + "설명")
          .quantity(100 + i)
          .image("상품" + "i" + "이미지")
          .category(Category.builder()
              .name("카테고리" + i)
              .build())
          .build());
    }

    Page<Product> productPage = new PageImpl<>(productList);

    given(productRepository.findAll(pageable)).willReturn(productPage);

    //when
    List<ProductInfo> result = productService.getProducts(pageable);

    //then
    assertEquals(3, result.size());
    assertEquals("상품1", result.get(1).getName());
    assertEquals("카테고리1", result.get(1).getCategoryName());
  }

  @Test
  void getMostOrderedProductsSuccess() {
    //given
    Pageable pageable = PageRequest.of(1, 3, Sort.by("ordered").descending());

    List<Product> productList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productList.add(Product.builder()
          .name("상품" + i)
          .price(i + 1)
          .description("상품" + "i" + "설명")
          .quantity(100 + i)
          .image("상품" + "i" + "이미지")
          .category(Category.builder()
              .name("카테고리" + i)
              .build())
          .build());
    }

    Page<Product> productPage = new PageImpl<>(productList);

    given(productRepository.findAll(pageable)).willReturn(productPage);

    //when
    List<ProductInfo> result = productService.getProducts(pageable);

    //then
    assertEquals(3, result.size());
    assertEquals("상품1", result.get(1).getName());
    assertEquals("카테고리1", result.get(1).getCategoryName());
  }

  @Test
  void searchProductsSuccess() {
    //given
    List<ProductDocument> productList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      productList.add(ProductDocument.builder()
          .name("상품" + i)
          .price(i + 1)
          .description("상품" + i + "설명")
          .quantity(100 + i)
          .image("상품" + i + "이미지")
          .categoryName("카테고리" + i)
          .build());
    }

    given(elasticSearchProductRepository.findByNameOrDescription(anyString(), any())).willReturn(
        productList);

    //when
    List<ProductInfo> result = productService.searchProducts("상품", 1, 3);

    //then
    assertEquals(3, result.size());
    assertEquals("상품1", result.get(1).getName());
    assertEquals(2, result.get(1).getPrice());
    assertEquals("카테고리1", result.get(1).getCategoryName());
  }

  @Test
  void getProductInfoSuccess() {
    //given
    Product product = Product.builder()
        .id(1L)
        .name("상품 이름")
        .price(1000)
        .description("상품 설명")
        .quantity(30)
        .image("이미지 주소")
        .category(Category.builder()
            .id(1L)
            .name("카테고리 이름")
            .build())
        .build();

    given(productRepository.findById(anyLong())).willReturn(Optional.ofNullable(product));

    //when
    ProductInfo result = productService.getProductInfo(1L);

    //then
    assertEquals("상품 이름", result.getName());
    assertEquals(1000, result.getPrice());
    assertEquals("카테고리 이름", result.getCategoryName());
  }

  @Test
  void getProductInfoFail_PRODUCT_NOT_FOUND() {
    //given
    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> productService.getProductInfo(1L));

    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
  }
}