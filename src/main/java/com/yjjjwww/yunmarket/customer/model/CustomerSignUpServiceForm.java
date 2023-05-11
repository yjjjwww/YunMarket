package com.yjjjwww.yunmarket.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignUpServiceForm {

  private String email;
  private String password;
  private String phone;
  private String address;
}
