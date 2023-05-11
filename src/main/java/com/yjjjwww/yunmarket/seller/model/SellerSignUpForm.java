package com.yjjjwww.yunmarket.seller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerSignUpForm {

  private String email;
  private String password;
  private String phone;

  public SellerSignUpServiceForm toServiceForm() {
    return SellerSignUpServiceForm.builder()
        .email(email)
        .password(password)
        .phone(phone)
        .build();
  }
}
