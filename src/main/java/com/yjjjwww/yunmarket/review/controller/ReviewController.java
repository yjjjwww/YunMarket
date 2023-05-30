package com.yjjjwww.yunmarket.review.controller;

import com.yjjjwww.yunmarket.review.model.ReviewDto;
import com.yjjjwww.yunmarket.review.model.ReviewRegisterForm;
import com.yjjjwww.yunmarket.review.service.ReviewService;
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

  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> registerReview(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestBody ReviewRegisterForm reviewRegisterForm) {
    reviewService.register(token, reviewRegisterForm.toServiceForm());
    return ResponseEntity.ok(REGISTER_REVIEW_SUCCESS);
  }

  @GetMapping
  public ResponseEntity<List<ReviewDto>> getReviews(
      @RequestParam("productId") Long productId,
      @RequestParam("page") Integer page,
      @RequestParam("size") Integer size
  ) {
    return ResponseEntity.ok(reviewService.getReviews(productId, page, size));
  }
}
