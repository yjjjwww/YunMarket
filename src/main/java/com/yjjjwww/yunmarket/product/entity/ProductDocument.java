package com.yjjjwww.yunmarket.product.entity;

import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Setting;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Document(indexName = "products")
@Setting(settingPath = "elastic/product-setting.json")
public class ProductDocument {

  @Id
  private Long id;

  @Field(type = FieldType.Text, analyzer = "ngram_analyzer")
  private String name;

  private String categoryName;
  private int price;

  @Field(type = FieldType.Text, analyzer = "ngram_analyzer")
  private String description;
  private Integer quantity;
  private String image;
  private Integer orderedCnt;
  private boolean deletedYn;

  public static ProductDocument from(Product product) {
    return ProductDocument.builder()
        .id(product.getId())
        .categoryName(product.getCategory().getName())
        .name(product.getName())
        .price(product.getPrice())
        .description(product.getDescription())
        .quantity(product.getQuantity())
        .image(product.getImage())
        .orderedCnt(product.orderedCnt)
        .deletedYn(product.isDeletedYn())
        .build();
  }
}
