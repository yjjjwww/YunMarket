package com.yjjjwww.yunmarket.review.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRegisterForm {

  @ApiModelProperty(example = "ordered 아이디")
  private Long orderId;
  @ApiModelProperty(example = "리뷰 평점")
  private Integer rating;
  @ApiModelProperty(example = "리뷰 내용")
  private String contents;

  public ReviewRegisterServiceForm toServiceForm() {
    return ReviewRegisterServiceForm.builder()
        .orderId(orderId)
        .rating(rating)
        .contents(contents)
        .build();
  }
}
