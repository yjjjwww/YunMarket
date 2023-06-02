package com.yjjjwww.yunmarket.review.controller;

import com.yjjjwww.yunmarket.review.model.ReviewCommentRegisterForm;
import com.yjjjwww.yunmarket.review.model.ReviewDto;
import com.yjjjwww.yunmarket.review.model.ReviewRegisterForm;
import com.yjjjwww.yunmarket.review.service.ReviewService;
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
@RequestMapping("/review")
public class ReviewController {

  private final ReviewService reviewService;

  private static final String REGISTER_REVIEW_SUCCESS = "리뷰 등록 완료";
  private static final String REGISTER_REVIEW_COMMENT_SUCCESS = "리뷰 댓글 등록 완료";

  @ApiOperation(value = "Customer 리뷰 등록")
  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> registerReview(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody ReviewRegisterForm reviewRegisterForm) {
    reviewService.register(token, reviewRegisterForm.toServiceForm());
    return ResponseEntity.ok(REGISTER_REVIEW_SUCCESS);
  }

  @ApiOperation(value = "리뷰 페이지 가져오기")
  @GetMapping
  public ResponseEntity<List<ReviewDto>> getReviews(
      @Parameter(name = "productId", description = "상품 아이디")
      @RequestParam("productId") Long productId,
      @Parameter(name = "page", description = "페이지")
      @RequestParam("page") Integer page,
      @Parameter(name = "size", description = "페이지 크기")
      @RequestParam("size") Integer size
  ) {
    return ResponseEntity.ok(reviewService.getReviews(productId, page, size));
  }

  @ApiOperation(value = "리뷰 답변 등록")
  @PostMapping("/comment")
  @PreAuthorize("hasRole('SELLER')")
  public ResponseEntity<String> registerReviewComment(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody ReviewCommentRegisterForm reviewCommentRegisterForm) {
    reviewService.registerReviewComment(token, reviewCommentRegisterForm.toServiceForm());
    return ResponseEntity.ok(REGISTER_REVIEW_COMMENT_SUCCESS);
  }
}
