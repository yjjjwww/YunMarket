package com.yjjjwww.yunmarket.question.model;

import com.yjjjwww.yunmarket.question.entity.Answer;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

  private String sellerEmail;
  private String contents;

  public static List<AnswerDto> toDtoList(List<Answer> answerList) {
    return answerList.stream()
        .map(answer -> AnswerDto.builder()
            .sellerEmail(answer.getSeller().getEmail())
            .contents(answer.getContents())
            .build())
        .collect(Collectors.toList());
  }
}
