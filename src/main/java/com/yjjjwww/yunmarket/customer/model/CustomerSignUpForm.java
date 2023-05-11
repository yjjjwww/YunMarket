package com.yjjjwww.yunmarket.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignUpForm {

  private String email;
  private String password;
  private String phone;
  private String address;

  public CustomerSignUpServiceForm toServiceForm() {
    return CustomerSignUpServiceForm.builder()
        .email(email)
        .password(password)
        .phone(phone)
        .address(address)
        .build();
  }
}
