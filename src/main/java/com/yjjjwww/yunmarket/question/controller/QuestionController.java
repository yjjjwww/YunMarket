package com.yjjjwww.yunmarket.question.controller;

import com.yjjjwww.yunmarket.question.model.AnswerRegisterForm;
import com.yjjjwww.yunmarket.question.model.QuestionDto;
import com.yjjjwww.yunmarket.question.model.QuestionRegisterForm;
import com.yjjjwww.yunmarket.question.service.QuestionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/question")
public class QuestionController {

  private final QuestionService questionService;

  private static final String REGISTER_QUESTION_SUCCESS = "문의 등록 완료";
  private static final String REGISTER_ANSWER_SUCCESS = "답변 등록 완료";

  @ApiOperation(value = "Customer 상품 문의 등록")
  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> registerQuestion(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody QuestionRegisterForm form) {
    questionService.registerQuestion(token, form.toServiceForm());
    return ResponseEntity.ok(REGISTER_QUESTION_SUCCESS);
  }

  @ApiOperation(value = "Seller 문의 답변 등록")
  @PostMapping("/answer")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<String> registerAnswer(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody AnswerRegisterForm form) {
    questionService.registerAnswer(token, form.toServiceForm());
    return ResponseEntity.ok(REGISTER_ANSWER_SUCCESS);
  }

  @ApiOperation(value = "상품에 대한 문의 조회")
  @GetMapping
  public ResponseEntity<List<QuestionDto>> getQuestions(
      @Parameter(name = "productId", description = "상품 아이디")
      @RequestParam("productId") Long productId,
      @Parameter(name = "page", description = "페이지")
      @RequestParam("page") Integer page,
      @Parameter(name = "size", description = "페이지 크기")
      @RequestParam("size") Integer size
  ) {
    return ResponseEntity.ok(questionService.getQuestions(productId, page, size));
  }
}
