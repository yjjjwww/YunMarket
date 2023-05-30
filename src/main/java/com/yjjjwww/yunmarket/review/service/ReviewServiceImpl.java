package com.yjjjwww.yunmarket.review.service;

import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.review.entity.Review;
import com.yjjjwww.yunmarket.review.model.ReviewRegisterServiceForm;
import com.yjjjwww.yunmarket.review.repository.ReviewRepository;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.transaction.entity.Ordered;
import com.yjjjwww.yunmarket.transaction.repository.OrderedRepository;
import java.util.Objects;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements ReviewService {

  private final JwtTokenProvider provider;
  private final CustomerRepository customerRepository;
  private final OrderedRepository orderedRepository;
  private final ReviewRepository reviewRepository;

  @Override
  public void register(String token, ReviewRegisterServiceForm form) {
    Customer customer = getCustomerFromToken(token);

    if (!checkReviewRegisterForm(form)) {
      throw new CustomException(ErrorCode.INVALID_REVIEW_FORM);
    }

    Ordered ordered = orderedRepository.findById(form.getOrderId())
        .orElseThrow(() -> new CustomException(ErrorCode.ORDERED_NOT_FOUND));

    Optional<Review> optionalReview = reviewRepository.findByOrderedId(ordered.getId());
    if (optionalReview.isPresent()) {
      throw new CustomException(ErrorCode.REVIEW_ALREADY_EXIST);
    }

    if (!Objects.equals(ordered.getCustomer().getId(), customer.getId())) {
      throw new CustomException(ErrorCode.CUSTOMER_NOT_MATCH);
    }

    Product product = ordered.getProduct();
    Seller seller = product.getSeller();

    Review review = Review.builder()
        .product(product)
        .customer(customer)
        .seller(seller)
        .ordered(ordered)
        .rating(form.getRating())
        .contents(form.getContents())
        .deletedYn(false)
        .build();

    reviewRepository.save(review);
  }

  private Customer getCustomerFromToken(String token) {
    UserVo vo = provider.getUserVo(token);

    Customer customer = customerRepository.findById(vo.getId())
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    return customer;
  }

  private boolean checkReviewRegisterForm(ReviewRegisterServiceForm form) {
    return !isStringEmpty(form.getContents()) && form.getRating() >= 0 && form.getRating() <= 10;
  }

  private static boolean isStringEmpty(String str) {
    return str == null || str.isBlank();
  }
}
