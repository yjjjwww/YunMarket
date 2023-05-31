package com.yjjjwww.yunmarket.question.model;

import com.yjjjwww.yunmarket.question.entity.Question;
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
public class QuestionDto {

  private String customerEmail;
  private String contents;
  private List<AnswerDto> answerDtoList;

  public static List<QuestionDto> toDtoList(List<Question> questions) {
    return questions.stream()
        .map(question -> QuestionDto.builder()
            .customerEmail(question.getCustomer().getEmail())
            .contents(question.getContents())
            .answerDtoList(AnswerDto.toDtoList(question.getAnswerList()))
            .build())
        .collect(Collectors.toList());
  }
}
