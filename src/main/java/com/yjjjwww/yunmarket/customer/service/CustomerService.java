package com.yjjjwww.yunmarket.customer.service;

import com.yjjjwww.yunmarket.customer.model.CustomerSignUpForm;

public interface CustomerService {

  /**
   * Customer 회원가입
   */
  String signUp(CustomerSignUpForm customerSignUpForm);
}
