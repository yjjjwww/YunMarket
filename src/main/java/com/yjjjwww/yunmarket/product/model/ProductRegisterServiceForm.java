package com.yjjjwww.yunmarket.product.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRegisterServiceForm {

  String name;
  Long categoryId;
  Integer price;
  String description;
  Integer quantity;
  String image;
}
