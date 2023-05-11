package com.yjjjwww.yunmarket.customer.service;

import com.yjjjwww.yunmarket.customer.model.CustomerSignInServiceForm;
import com.yjjjwww.yunmarket.customer.model.CustomerSignUpServiceForm;

public interface CustomerService {

  /**
   * Customer 회원가입
   */
  void signUp(CustomerSignUpServiceForm customerSignUpServiceForm);

  /**
   * Customer 로그인
   */
  String signIn(CustomerSignInServiceForm customerSignInServiceForm);
}
