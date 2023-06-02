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
public class ReviewCommentRegisterForm {

  @ApiModelProperty(example = "리뷰 아이디")
  private Long reviewId;
  @ApiModelProperty(example = "답변 내용")
  private String contents;

  public ReviewCommentRegisterServiceForm toServiceForm() {
    return ReviewCommentRegisterServiceForm.builder()
        .reviewId(reviewId)
        .contents(contents)
        .build();
  }
}
