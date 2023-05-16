package com.yjjjwww.yunmarket.product.model;

import com.yjjjwww.yunmarket.product.entity.Product;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

  private String name;
  private Integer price;
  private String description;
  private Integer quantity;
  private String image;
  private String categoryName;

  public static List<ProductInfo> toList(List<Product> products) {
    return products.stream()
        .map(product -> ProductInfo.builder()
            .name(product.getName())
            .price(product.getPrice())
            .description(product.getDescription())
            .quantity(product.getQuantity())
            .image(product.getImage())
            .categoryName(product.getCategory().getName())
            .build())
        .collect(Collectors.toList());
  }
}
