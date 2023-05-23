package com.yjjjwww.yunmarket.product.model;

import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.product.entity.ProductDocument;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductInfo {

  private Long id;
  private String name;
  private int price;
  private String description;
  private Integer quantity;
  private String image;
  private String categoryName;

  public static List<ProductInfo> toList(Page<Product> products) {
    return products.stream()
        .map(product -> ProductInfo.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .description(product.getDescription())
            .quantity(product.getQuantity())
            .image(product.getImage())
            .categoryName(product.getCategory().getName())
            .build())
        .collect(Collectors.toList());
  }

  public static List<ProductInfo> toListFromDocument(List<ProductDocument> products) {
    return products.stream()
        .map(product -> ProductInfo.builder()
            .id(product.getId())
            .name(product.getName())
            .price(product.getPrice())
            .description(product.getDescription())
            .quantity(product.getQuantity())
            .image(product.getImage())
            .categoryName(product.getCategoryName())
            .build())
        .collect(Collectors.toList());
  }

  public static ProductInfo from(Product product) {
    return ProductInfo.builder()
        .id(product.getId())
        .name(product.getName())
        .price(product.getPrice())
        .description(product.getDescription())
        .quantity(product.getQuantity())
        .image(product.getImage())
        .categoryName(product.getCategory().getName())
        .build();
  }
}
