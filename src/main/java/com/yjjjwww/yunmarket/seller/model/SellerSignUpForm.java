package com.yjjjwww.yunmarket.seller.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SellerSignUpForm {

  @ApiModelProperty(example = "Seller 이메일")
  private String email;
  @ApiModelProperty(example = "Seller 비밀번호")
  private String password;
  @ApiModelProperty(example = "Seller 연락처")
  private String phone;

  public SellerSignUpServiceForm toServiceForm() {
    return SellerSignUpServiceForm.builder()
        .email(email)
        .password(password)
        .phone(phone)
        .build();
  }
}
