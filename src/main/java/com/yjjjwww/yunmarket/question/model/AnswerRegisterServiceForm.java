package com.yjjjwww.yunmarket.question.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerRegisterServiceForm {

  private Long questionId;
  private String contents;
}
