package com.yjjjwww.yunmarket.seller.service;

import com.yjjjwww.yunmarket.seller.model.SellerSignUpServiceForm;

public interface SellerService {

  /**
   * Seller 회원가입
   */
  void signUp(SellerSignUpServiceForm sellerSignUpServiceForm);
}
