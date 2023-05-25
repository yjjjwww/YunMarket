package com.yjjjwww.yunmarket.cart.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EditCartForm {

  private Long productId;
  private Integer quantity;
}
