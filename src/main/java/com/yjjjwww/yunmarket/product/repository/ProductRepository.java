package com.yjjjwww.yunmarket.product.repository;

import com.yjjjwww.yunmarket.product.entity.Product;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

  List<Product> findAllBy(Pageable pageable);
}
