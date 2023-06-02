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
public class AnswerRegisterForm {

  @ApiModelProperty(example = "문의 아이디")
  private Long questionId;
  @ApiModelProperty(example = "답변 내용")
  private String contents;

  public AnswerRegisterServiceForm toServiceForm() {
    return AnswerRegisterServiceForm.builder()
        .questionId(questionId)
        .contents(contents)
        .build();
  }
}
