package com.yjjjwww.yunmarket.cart.service;

import com.yjjjwww.yunmarket.cart.model.AddCartForm;

public interface CartService {

  /**
   * 장바구니 추가
   */
  void addCart(AddCartForm form);
}
