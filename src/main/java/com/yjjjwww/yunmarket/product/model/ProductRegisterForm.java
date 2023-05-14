package com.yjjjwww.yunmarket.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegisterForm {

  String name;
  Long categoryId;
  Integer price;
  String description;
  Integer quantity;
  String image;

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
