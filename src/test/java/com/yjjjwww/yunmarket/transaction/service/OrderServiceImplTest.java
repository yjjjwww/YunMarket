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
import com.yjjjwww.yunmarket.transaction.entity.Ordered;
import com.yjjjwww.yunmarket.transaction.entity.Transaction;
import com.yjjjwww.yunmarket.transaction.model.OrderedItemsForm;
import com.yjjjwww.yunmarket.transaction.model.TransactionsForm;
import com.yjjjwww.yunmarket.transaction.repository.OrderedRepository;
import com.yjjjwww.yunmarket.transaction.repository.TransactionRepository;
import java.time.LocalDateTime;
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

  @Test
  void getTotalOrderedItemsSuccess() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    List<Ordered> orderedList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Product product = Product.builder()
          .name("상품" + i)
          .image("이미지")
          .build();
      Transaction transaction = Transaction.builder()
          .id((long) i)
          .transactionDate(LocalDateTime.now())
          .deletedYn(false)
          .build();
      Ordered ordered = Ordered.builder()
          .price(i + 1000)
          .quantity(i + 1)
          .product(product)
          .transaction(transaction)
          .build();
      orderedList.add(ordered);
    }

    given(orderedRepository.findByCustomerId(anyLong()))
        .willReturn(orderedList);

    //when
    List<OrderedItemsForm> result = orderService.getTotalOrderedItems("token");

    //then
    assertEquals(3, result.size());
  }

  @Test
  void getTotalOrderedItemsFail_ORDERED_NOT_FOUND() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    List<Ordered> orderedList = new ArrayList<>();

    given(orderedRepository.findByCustomerId(anyLong()))
        .willReturn(orderedList);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> orderService.getTotalOrderedItems("token"));

    //then
    assertEquals(ErrorCode.ORDERED_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void getTotalTransactionsSuccess() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    List<Transaction> transactionList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Transaction transaction = Transaction.builder()
          .id((long) i)
          .transactionPrice(i + 1)
          .pointUse(i)
          .transactionDate(LocalDateTime.now())
          .deletedYn(false)
          .build();

      transactionList.add(transaction);
    }

    given(transactionRepository.findByCustomerId(anyLong()))
        .willReturn(transactionList);

    //when
    List<TransactionsForm> result = orderService.getTotalTransactions("token");

    //then
    assertEquals(3, result.size());
  }

  @Test
  void getTotalTransactionsFail_TRANSACTION_NOT_FOUND() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    List<Transaction> transactionList = new ArrayList<>();

    given(transactionRepository.findByCustomerId(anyLong()))
        .willReturn(transactionList);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> orderService.getTotalTransactions("token"));

    //then
    assertEquals(ErrorCode.TRANSACTION_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void getOrderedItemsByTransactionSuccess() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    List<Ordered> orderedList = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      Product product = Product.builder()
          .name("상품" + i)
          .image("이미지")
          .build();
      Transaction transaction = Transaction.builder()
          .id((long) i)
          .transactionDate(LocalDateTime.now())
          .deletedYn(false)
          .build();
      Ordered ordered = Ordered.builder()
          .price(i + 1000)
          .quantity(i + 1)
          .product(product)
          .transaction(transaction)
          .build();
      orderedList.add(ordered);
    }

    given(orderedRepository.findByCustomerIdAndTransactionId(anyLong(), anyLong()))
        .willReturn(orderedList);

    //when
    List<OrderedItemsForm> result = orderService.getOrderedItemsByTransaction("token", 1L);

    //then
    assertEquals(3, result.size());
  }

  @Test
  void getOrderedItemsByTransactionFail_ORDERED_NOT_FOUND() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(Customer.builder()
            .id(1L)
            .build()));

    List<Ordered> orderedList = new ArrayList<>();

    given(orderedRepository.findByCustomerIdAndTransactionId(anyLong(), anyLong()))
        .willReturn(orderedList);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> orderService.getOrderedItemsByTransaction("token", 1L));

    //then
    assertEquals(ErrorCode.ORDERED_NOT_FOUND, exception.getErrorCode());
  }
}