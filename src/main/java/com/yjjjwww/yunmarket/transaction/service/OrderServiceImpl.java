package com.yjjjwww.yunmarket.transaction.service;

import com.yjjjwww.yunmarket.cart.entity.Cart;
import com.yjjjwww.yunmarket.cart.repository.CartRepository;
import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.product.repository.ProductRepository;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.seller.repository.SellerRepository;
import com.yjjjwww.yunmarket.transaction.entity.Ordered;
import com.yjjjwww.yunmarket.transaction.entity.Transaction;
import com.yjjjwww.yunmarket.transaction.repository.OrderedRepository;
import com.yjjjwww.yunmarket.transaction.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService{

  private final JwtTokenProvider provider;
  private final SellerRepository sellerRepository;
  private final CustomerRepository customerRepository;
  private final CartRepository cartRepository;
  private final ProductRepository productRepository;
  private final OrderedRepository orderedRepository;
  private final TransactionRepository transactionRepository;

  @Override
  @Transactional
  public void orderItems(String token) {
    Customer customer = getCustomerFromToken(token);

    List<Cart> cartList = cartRepository.findByCustomerId(customer.getId());

    if (cartList.size() == 0) {
      throw new CustomException(ErrorCode.CART_NOT_FOUND);
    }

    List<Ordered> orderedList = new ArrayList<>();

    int total = 0;

    for (Cart cart : cartList) {
      Product product = cart.getProduct();
      Seller seller = cart.getProduct().getSeller();
      int totalPrice = product.getPrice() * cart.getQuantity();

      Ordered ordered = Ordered.builder()
          .customer(customer)
          .product(product)
          .seller(seller)
          .price(product.getPrice())
          .quantity(cart.getQuantity())
          .total(totalPrice)
          .build();

      orderedList.add(ordered);
      total += totalPrice;
    }

    Transaction transaction = Transaction.builder()
        .customer(customer)
        .orderedListList(orderedList)
        .transactionDate(LocalDateTime.now())
        .transactionPrice(total)
        .pointUse(0)
        .build();

    for (Ordered ordered : orderedList) {
      ordered.setTransaction(transaction);
    }

    transactionRepository.save(transaction);
    orderedRepository.saveAll(orderedList);
    cartRepository.deleteByCustomerId(customer.getId());
  }

  private Customer getCustomerFromToken(String token) {
    UserVo vo = provider.getUserVo(token);

    Customer customer = customerRepository.findById(vo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return customer;
  }
}
