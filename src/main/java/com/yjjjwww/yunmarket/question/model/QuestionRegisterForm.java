package com.yjjjwww.yunmarket.question.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRegisterForm {

  private Long productId;
  private String contents;

  public QuestionRegisterServiceForm toServiceForm() {
    return QuestionRegisterServiceForm.builder()
        .productId(productId)
        .contents(contents)
        .build();
  }
}
