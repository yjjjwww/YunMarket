package com.yjjjwww.yunmarket.customer.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSignInForm {

  @ApiModelProperty(example = "Customer 이메일")
  private String email;
  @ApiModelProperty(example = "Customer 비밀번호")
  private String password;

  public CustomerSignInServiceForm toServiceForm() {
    return CustomerSignInServiceForm.builder()
        .email(email)
        .password(password)
        .build();
  }
}
