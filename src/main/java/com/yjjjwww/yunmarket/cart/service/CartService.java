package com.yjjjwww.yunmarket.cart.service;

import com.yjjjwww.yunmarket.cart.model.AddCartForm;
import com.yjjjwww.yunmarket.cart.model.EditCartForm;

public interface CartService {

  /**
   * 장바구니 추가
   */
  void addCart(String token, AddCartForm form);

  /**
   * 장바구니 수정
   */
  void editCart(String token, EditCartForm form);
}
