package com.yjjjwww.yunmarket.review.service;

import com.yjjjwww.yunmarket.review.model.ReviewCommentRegisterServiceForm;
import com.yjjjwww.yunmarket.review.model.ReviewDto;
import com.yjjjwww.yunmarket.review.model.ReviewRegisterServiceForm;
import java.util.List;

public interface ReviewService {

  /**
   * 리뷰 등록하기
   */
  void register(String token, ReviewRegisterServiceForm form);

  /**
   * 리뷰 조회하기
   */
  List<ReviewDto> getReviews(Long productId, Integer page, Integer size);

  /**
   * 리뷰 댓글 달기
   */
  void registerReviewComment(String token, ReviewCommentRegisterServiceForm form);
}
