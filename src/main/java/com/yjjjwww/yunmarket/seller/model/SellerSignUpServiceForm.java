package com.yjjjwww.yunmarket.seller.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerSignUpServiceForm {

  private String email;
  private String password;
  private String phone;
}
