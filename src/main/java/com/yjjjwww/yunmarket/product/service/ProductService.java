package com.yjjjwww.yunmarket.product.service;

import com.yjjjwww.yunmarket.product.model.ProductInfo;
import com.yjjjwww.yunmarket.product.model.ProductRegisterServiceForm;
import java.util.List;

public interface ProductService {

  /**
   * 상품 등록
   */
  void register(String token, ProductRegisterServiceForm productRegisterServiceForm);

  /**
   * 상품 최신순 조회
   */
  List<ProductInfo> getLatestProducts(Integer page, Integer size);

  /**
   * 상품 낮은 가격순 조회
   */
  List<ProductInfo> getLowestPriceProducts(Integer page, Integer size);

  /**
   * 상품 낮은 가격순 조회
   */
  List<ProductInfo> getMostOrderedProducts(Integer page, Integer size);

  /**
   * 상품 검색
   */
  List<ProductInfo> searchProducts(String name, Integer page, Integer size);
}
