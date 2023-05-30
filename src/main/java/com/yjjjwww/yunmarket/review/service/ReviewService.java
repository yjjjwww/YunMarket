package com.yjjjwww.yunmarket.review.service;

import com.yjjjwww.yunmarket.review.model.ReviewRegisterServiceForm;

public interface ReviewService {

  /**
   * 리뷰 등록하기
   */
  void register(String token, ReviewRegisterServiceForm form);
}
