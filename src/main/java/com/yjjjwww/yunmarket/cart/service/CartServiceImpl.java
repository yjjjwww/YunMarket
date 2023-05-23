package com.yjjjwww.yunmarket.cart.service;

import com.yjjjwww.yunmarket.cart.entity.Cart;
import com.yjjjwww.yunmarket.cart.model.AddCartForm;
import com.yjjjwww.yunmarket.cart.repository.CartRepository;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.product.repository.ProductRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartServiceImpl implements CartService {

  private final CustomerRepository customerRepository;
  private final ProductRepository productRepository;
  private final CartRepository cartRepository;

  @Override
  public void addCart(AddCartForm form) {

    Customer customer = customerRepository.findById(form.getCustomerId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    Product product = productRepository.findById(form.getProductId())
        .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));

    Optional<Cart> optionalExistCart = cartRepository.findByCustomerIdAndProductId(
        form.getCustomerId(),
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

  private static boolean checkQuantity(Integer productQuantity, Integer cartQuantity) {
    return productQuantity >= cartQuantity;
  }
}
