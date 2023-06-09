package com.yjjjwww.yunmarket.product.service;

import com.yjjjwww.yunmarket.product.model.ProductInfo;
import com.yjjjwww.yunmarket.product.model.ProductRegisterServiceForm;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface ProductService {

  /**
   * 상품 등록
   */
  void register(String token, ProductRegisterServiceForm productRegisterServiceForm);

  /**
   * 상품 조회
   */
  List<ProductInfo> getProducts(Pageable pageable);

  /**
   * 상품 검색
   */
  List<ProductInfo> searchProducts(String name, Integer page, Integer size);

  /**
   * 상품 상세 정보 불러오기
   */
  ProductInfo getProductInfo(Long id, String userIp);

  /**
   * IP 기준 최근 본 상품과 관련 인기 상품들 가져오기
   */
  List<ProductInfo> getRecentViewedProducts(String userIp, Integer page, Integer size);
}
