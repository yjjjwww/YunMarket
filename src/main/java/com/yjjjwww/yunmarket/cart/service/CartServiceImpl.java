package com.yjjjwww.yunmarket.cart.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final CartRepository cartRepository;
  private final JwtTokenProvider provider;

  @Override
  public void addCart(String token, AddCartForm form) {
    Customer customer = getCustomerFromToken(token);

    Product product = productRepository.findById(form.getProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Optional<Cart> optionalExistCart = cartRepository.findByCustomerIdAndProductId(
        customer.getId(),
        form.getProductId());

    if (optionalExistCart.isPresent()) {
      Cart existCart = optionalExistCart.get();

      Integer totalQuantity = existCart.getQuantity() + form.getQuantity();

      if (!checkQuantity(product.getQuantity(), totalQuantity)) {
        throw new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY);
      }

      existCart.setQuantity(totalQuantity);

      cartRepository.save(existCart);
    } else {

      if (!checkQuantity(product.getQuantity(), form.getQuantity())) {
        throw new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY);
      }

      Cart cart = Cart.builder()
          .customer(customer)
          .product(product)
          .quantity(form.getQuantity())
          .build();

      cartRepository.save(cart);
    }
  }

  @Override
  public void editCart(String token, EditCartForm form) {
    Customer customer = getCustomerFromToken(token);

    Product product = productRepository.findById(form.getProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Cart cart = cartRepository.findByCustomerIdAndProductId(customer.getId(), product.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

    if (!checkQuantity(product.getQuantity(), form.getQuantity())) {
      throw new CustomException(ErrorCode.NOT_ENOUGH_QUANTITY);
    }

    cart.setQuantity(form.getQuantity());

    cartRepository.save(cart);
  }

  @Override
  public void deleteCartItem(String token, Long productId) {
    Customer customer = getCustomerFromToken(token);

    Product product = productRepository.findById(productId)
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Cart cart = cartRepository.findByCustomerIdAndProductId(customer.getId(), product.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.CART_NOT_FOUND));

    cartRepository.delete(cart);
  }

  @Override
  @Transactional
  public long deleteAllCart(String token) {
    Customer customer = getCustomerFromToken(token);

    Long cnt = cartRepository.deleteByCustomerId(customer.getId());

    return cnt;
  }

  private static boolean checkQuantity(Integer productQuantity, Integer cartQuantity) {
    return productQuantity >= cartQuantity;
  }

  private Customer getCustomerFromToken(String token) {
    UserVo vo = provider.getUserVo(token);

    Customer customer = customerRepository.findById(vo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return customer;
  }
}
