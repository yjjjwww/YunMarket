package com.yjjjwww.yunmarket.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegisterServiceForm {

  private Long orderId;
  private Integer rating;
  private String contents;
}
