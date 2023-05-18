package com.yjjjwww.yunmarket.product.repository;

import com.yjjjwww.yunmarket.product.entity.ProductDocument;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ElasticSearchProductRepository extends ElasticsearchRepository<ProductDocument, Long> {

  @Query("{\"bool\":{\"must\":{\"multi_match\":{\"query\":\"?0\",\"fields\":[\"name\", \"description\"]}}}}")
  List<ProductDocument> findByNameOrDescription(String keyword, Pageable pageable);
}
