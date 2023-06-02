package com.yjjjwww.yunmarket.product.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegisterForm {

  @ApiModelProperty(example = "상품 이름")
  private String name;
  @ApiModelProperty(example = "카테고리 아이디")
  private Long categoryId;
  @ApiModelProperty(example = "상품 가격")
  private Integer price;
  @ApiModelProperty(example = "상품 설명")
  private String description;
  @ApiModelProperty(example = "상품 수량")
  private Integer quantity;
  @ApiModelProperty(example = "상품 이미지")
  private String image;

  public ProductRegisterServiceForm toServiceForm() {
    return ProductRegisterServiceForm.builder()
        .name(name)
        .categoryId(categoryId)
        .price(price)
        .description(description)
        .quantity(quantity)
        .image(image)
        .build();
  }
}
