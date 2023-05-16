package com.yjjjwww.yunmarket.product.repository;

import com.yjjjwww.yunmarket.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

  @Query("SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.createdDate DESC")
  List<Product> findAllByOrderByCreatedDateDesc(Pageable pageable);

  @Query("SELECT p FROM Product p JOIN FETCH p.category ORDER BY p.price ASC, p.createdDate DESC")
  List<Product> findAllByOrderByPriceAscAndCreatedDateDesc(Pageable pageable);
}
