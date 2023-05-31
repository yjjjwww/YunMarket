package com.yjjjwww.yunmarket.product.repository;

import com.yjjjwww.yunmarket.product.entity.ProductViewHistory;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductViewHistoryRepository extends JpaRepository<ProductViewHistory, Long> {

  Optional<ProductViewHistory> findByUserIp(String userIp);
}
