package com.yjjjwww.yunmarket.transaction.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yjjjwww.yunmarket.cart.entity.Cart;
import com.yjjjwww.yunmarket.cart.repository.CartRepository;
import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.transaction.entity.Transaction;
import com.yjjjwww.yunmarket.transaction.repository.OrderedRepository;
import com.yjjjwww.yunmarket.transaction.repository.TransactionRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

  @Mock
  private JwtTokenProvider provider;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private CartRepository cartRepository;

  @Mock
  private OrderedRepository orderedRepository;

  @Mock
  private TransactionRepository transactionRepository;

  @InjectMocks
  private OrderServiceImpl orderService;

  @Test
  void orderItemsSuccess() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    List<Cart> cartList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Seller seller = Seller.builder()
          .id((long) i)
          .build();

      Product product = Product.builder()
          .id((long) i)
          .seller(seller)
          .price(1000 + i)
          .build();

      Cart cart = Cart.builder()
          .product(product)
          .quantity(i + 1)
          .build();

      cartList.add(cart);
    }

    given(cartRepository.findByCustomerId(anyLong()))
        .willReturn(cartList);

    //when
    orderService.orderItems("jwt");

    //then
    verify(transactionRepository, times(1)).save(any(Transaction.class));
    verify(orderedRepository, times(1)).saveAll(any());
    verify(cartRepository, times(1)).deleteByCustomerId(anyLong());
  }

  @Test
  void orderItemsFail_CART_NOT_FOUND() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    List<Cart> cartList = new ArrayList<>();

    given(cartRepository.findByCustomerId(anyLong()))
        .willReturn(cartList);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> orderService.orderItems("token"));

    //then
    assertEquals(ErrorCode.CART_NOT_FOUND, exception.getErrorCode());
  }
}