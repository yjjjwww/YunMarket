package com.yjjjwww.yunmarket.customer.model;

import lombok.Data;

@Data
public class CustomerSignUpForm {

  private String email;
  private String password;
  private String phone;
  private String address;
}
