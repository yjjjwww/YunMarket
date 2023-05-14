package com.yjjjwww.yunmarket.product.service;

import com.yjjjwww.yunmarket.product.model.ProductRegisterServiceForm;

public interface ProductService {

  /**
   * 상품 등록
   */
  void register(String token, ProductRegisterServiceForm productRegisterServiceForm);
}
