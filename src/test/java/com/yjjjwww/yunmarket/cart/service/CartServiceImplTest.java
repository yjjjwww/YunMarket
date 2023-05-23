package com.yjjjwww.yunmarket.cart.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yjjjwww.yunmarket.cart.entity.Cart;
import com.yjjjwww.yunmarket.cart.model.AddCartForm;
import com.yjjjwww.yunmarket.cart.model.EditCartForm;
import com.yjjjwww.yunmarket.cart.repository.CartRepository;
import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.product.repository.ProductRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

  @Mock
  private CartRepository cartRepository;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private ProductRepository productRepository;

  @Mock
  private JwtTokenProvider provider;

  @InjectMocks
  private CartServiceImpl cartService;

  @Test
  void addCartSuccess_NO_EXIST_CART() {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    given(productRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(Product.builder()
            .quantity(100)
            .build()));

    //when
    cartService.addCart("jwt", form);

    //then
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  void addCartSuccess_EXIST_CART() {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    given(productRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(Product.builder()
            .id(1L)
            .quantity(100)
            .build()));

    given(cartRepository.findByCustomerIdAndProductId(anyLong(), anyLong())).willReturn(
        Optional.ofNullable(Cart.builder()
            .quantity(30)
            .build()));

    //when
    cartService.addCart("jwt", form);

    //then
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  void addCartFail_USER_NOT_FOUND() {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> cartService.addCart("token", form));

    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void addCartFail_PRODUCT_NOT_FOUND() {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> cartService.addCart("token", form));

    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void addCartFail_NOT_ENOUGH_QUANTITY() {
    //given
    AddCartForm form = AddCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    given(productRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(Product.builder()
            .id(1L)
            .quantity(10)
            .build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> cartService.addCart("token", form));

    //then
    assertEquals(ErrorCode.NOT_ENOUGH_QUANTITY, exception.getErrorCode());
  }

  @Test
  void editCartSuccess() {
    //given
    EditCartForm form = EditCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    Customer customer = Customer.builder()
        .id(1L)
        .build();

    Product product = Product.builder()
        .id(1L)
        .quantity(100)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(customer));

    given(productRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(product));

    given(cartRepository.findByCustomerIdAndProductId(anyLong(), anyLong())).willReturn(
        Optional.ofNullable(Cart.builder()
            .customer(customer)
            .product(product)
            .quantity(100)
            .build()));

    //when
    cartService.editCart("jwt", form);

    //then
    verify(cartRepository, times(1)).save(any(Cart.class));
  }

  @Test
  void editCartFail_USER_NOT_FOUND() {
    //given
    EditCartForm form = EditCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> cartService.editCart("token", form));

    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void editCartFail_PRODUCT_NOT_FOUND() {
    //given
    EditCartForm form = EditCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    Customer customer = Customer.builder()
        .id(1L)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(customer));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> cartService.editCart("token", form));

    //then
    assertEquals(ErrorCode.PRODUCT_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void editCartFail_CART_NOT_FOUND() {
    //given
    EditCartForm form = EditCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    Customer customer = Customer.builder()
        .id(1L)
        .build();

    Product product = Product.builder()
        .id(1L)
        .quantity(100)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(customer));

    given(productRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(product));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> cartService.editCart("token", form));

    //then
    assertEquals(ErrorCode.CART_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void editCartFail_NOT_ENOUGH_QUANTITY() {
    //given
    EditCartForm form = EditCartForm.builder()
        .productId(1L)
        .quantity(30)
        .build();

    Customer customer = Customer.builder()
        .id(1L)
        .build();

    Product product = Product.builder()
        .id(1L)
        .quantity(20)
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(customer));

    given(productRepository.findById(anyLong())).willReturn(
        Optional.ofNullable(product));

    given(cartRepository.findByCustomerIdAndProductId(anyLong(), anyLong())).willReturn(
        Optional.ofNullable(Cart.builder()
            .customer(customer)
            .product(product)
            .quantity(20)
            .build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> cartService.editCart("token", form));

    //then
    assertEquals(ErrorCode.NOT_ENOUGH_QUANTITY, exception.getErrorCode());
  }
}