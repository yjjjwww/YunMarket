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
public class CustomerSignUpForm {

  @ApiModelProperty(example = "Customer 이메일")
  private String email;
  @ApiModelProperty(example = "Customer 비밀번호")
  private String password;
  @ApiModelProperty(example = "Customer 연락처")
  private String phone;
  @ApiModelProperty(example = "Customer 주소")
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
