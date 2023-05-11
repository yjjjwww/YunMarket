package com.yjjjwww.yunmarket.customer.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CustomerSignUpForm {

  private String email;
  private String password;
  private String phone;
  private String address;
}
