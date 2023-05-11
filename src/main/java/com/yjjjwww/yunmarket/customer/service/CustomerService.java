package com.yjjjwww.yunmarket.customer.service;

import com.yjjjwww.yunmarket.customer.model.CustomerSignUpServiceForm;

public interface CustomerService {

  /**
   * Customer 회원가입
   */
  void signUp(CustomerSignUpServiceForm customerSignUpServiceForm);
}
