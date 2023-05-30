package com.yjjjwww.yunmarket.review.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCommentRegisterForm {

  private Long reviewId;
  private String contents;

  public ReviewCommentRegisterServiceForm toServiceForm() {
    return ReviewCommentRegisterServiceForm.builder()
        .reviewId(reviewId)
        .contents(contents)
        .build();
  }
}
