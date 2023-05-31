package com.yjjjwww.yunmarket.question.service;

import com.yjjjwww.yunmarket.question.model.AnswerRegisterServiceForm;
import com.yjjjwww.yunmarket.question.model.QuestionDto;
import com.yjjjwww.yunmarket.question.model.QuestionRegisterServiceForm;
import java.util.List;

public interface QuestionService {

  /**
   * 문의 등록하기
   */
  void registerQuestion(String token, QuestionRegisterServiceForm form);

  /**
   * 문의 등록하기
   */
  void registerAnswer(String token, AnswerRegisterServiceForm form);

  /**
   * 문의 조회하기
   */
  List<QuestionDto> getQuestions(Long productId, Integer page, Integer size);
}
