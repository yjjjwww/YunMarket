package com.yjjjwww.yunmarket.question.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuestionRegisterForm {

  @ApiModelProperty(example = "상품 아이디")
  private Long productId;
  @ApiModelProperty(example = "문의 내용")
  private String contents;

  public QuestionRegisterServiceForm toServiceForm() {
    return QuestionRegisterServiceForm.builder()
        .productId(productId)
        .contents(contents)
        .build();
  }
}
