package com.yjjjwww.yunmarket.cart.repository;

import com.yjjjwww.yunmarket.cart.entity.Cart;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

  Optional<Cart> findByCustomerIdAndProductId(Long customerId, Long productId);
}
