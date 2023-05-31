package com.yjjjwww.yunmarket.product.repository;

import com.yjjjwww.yunmarket.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

  Page<Product> findAll(Pageable pageable);

  Page<Product> findAllByCategoryId(Long categoryId, Pageable pageable);
}
