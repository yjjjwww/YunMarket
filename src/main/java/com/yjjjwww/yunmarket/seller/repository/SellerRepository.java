package com.yjjjwww.yunmarket.seller.repository;

import com.yjjjwww.yunmarket.seller.entity.Seller;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SellerRepository extends JpaRepository<Seller, Long> {

  Optional<Seller> findByEmail(String email);
}
