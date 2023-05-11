package com.yjjjwww.yunmarket.customer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignInForm {

  private String email;
  private String password;

  public CustomerSignInServiceForm toServiceForm() {
    return CustomerSignInServiceForm.builder()
        .email(email)
        .password(password)
        .build();
  }
}
