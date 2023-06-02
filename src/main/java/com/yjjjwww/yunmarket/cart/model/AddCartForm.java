package com.yjjjwww.yunmarket.cart.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddCartForm {

  @ApiModelProperty(example = "상품 아이디")
  private Long productId;
  @ApiModelProperty(example = "상품 수량")
  private Integer quantity;
}
