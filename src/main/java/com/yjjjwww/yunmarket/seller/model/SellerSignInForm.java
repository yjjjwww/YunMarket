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
public class SellerSignInForm {

  @ApiModelProperty(example = "Seller 이메일")
  private String email;
  @ApiModelProperty(example = "Seller 비밀번호")
  private String password;

  public SellerSignInServiceForm toServiceForm() {
    return SellerSignInServiceForm.builder()
        .email(email)
        .password(password)
        .build();
  }
}
