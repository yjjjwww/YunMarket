package com.yjjjwww.yunmarket.review.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.yjjjwww.yunmarket.common.UserVo;
import com.yjjjwww.yunmarket.config.JwtTokenProvider;
import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.customer.repository.CustomerRepository;
import com.yjjjwww.yunmarket.exception.CustomException;
import com.yjjjwww.yunmarket.exception.ErrorCode;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.review.entity.Review;
import com.yjjjwww.yunmarket.review.entity.ReviewComment;
import com.yjjjwww.yunmarket.review.model.ReviewCommentRegisterServiceForm;
import com.yjjjwww.yunmarket.review.model.ReviewDto;
import com.yjjjwww.yunmarket.review.model.ReviewRegisterServiceForm;
import com.yjjjwww.yunmarket.review.repository.ReviewCommentRepository;
import com.yjjjwww.yunmarket.review.repository.ReviewRepository;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.seller.repository.SellerRepository;
import com.yjjjwww.yunmarket.transaction.entity.Ordered;
import com.yjjjwww.yunmarket.transaction.repository.OrderedRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

  @Mock
  private JwtTokenProvider provider;

  @Mock
  private CustomerRepository customerRepository;

  @Mock
  private SellerRepository sellerRepository;

  @Mock
  private OrderedRepository orderedRepository;

  @Mock
  private ReviewRepository reviewRepository;

  @Mock
  private ReviewCommentRepository reviewCommentRepository;

  @InjectMocks
  private ReviewServiceImpl reviewService;

  @Test
  void registerReviewSuccess() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Customer customer = Customer.builder()
        .id(1L)
        .build();
    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(customer));

    Seller seller = Seller.builder()
        .id(1L)
        .build();

    Product product = Product.builder()
        .seller(seller)
        .build();

    Ordered ordered = Ordered.builder()
        .customer(customer)
        .product(product)
        .build();

    given(orderedRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(ordered));

    ReviewRegisterServiceForm form = ReviewRegisterServiceForm.builder()
        .orderId(1L)
        .rating(4)
        .contents("리뷰")
        .build();

    //when
    reviewService.register("jwt", form);

    //then
    verify(reviewRepository, times(1)).save(any(Review.class));
  }

  @Test
  void registerReviewFail_INVALID_REVIEW_FORM() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Customer customer = Customer.builder()
        .id(1L)
        .build();
    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(customer));

    ReviewRegisterServiceForm form = ReviewRegisterServiceForm.builder()
        .orderId(1L)
        .rating(41)
        .contents("")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> reviewService.register("token", form));

    //then
    assertEquals(ErrorCode.INVALID_REVIEW_FORM, exception.getErrorCode());
  }

  @Test
  void registerReviewFail_ORDERED_NOT_FOUND() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Customer customer = Customer.builder()
        .id(1L)
        .build();
    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(customer));

    ReviewRegisterServiceForm form = ReviewRegisterServiceForm.builder()
        .orderId(1L)
        .rating(4)
        .contents("리뷰")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> reviewService.register("token", form));

    //then
    assertEquals(ErrorCode.ORDERED_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void registerReviewFail_REVIEW_ALREADY_EXIST() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Customer customer = Customer.builder()
        .id(1L)
        .build();
    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(customer));

    ReviewRegisterServiceForm form = ReviewRegisterServiceForm.builder()
        .orderId(1L)
        .rating(4)
        .contents("리뷰")
        .build();

    Seller seller = Seller.builder()
        .id(1L)
        .build();

    Product product = Product.builder()
        .seller(seller)
        .build();

    Ordered ordered = Ordered.builder()
        .id(1L)
        .customer(customer)
        .product(product)
        .build();

    given(orderedRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(ordered));

    given(reviewRepository.findByOrderedId(anyLong()))
        .willReturn(Optional.ofNullable(Review.builder()
            .id(1L)
            .build()));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> reviewService.register("token", form));

    //then
    assertEquals(ErrorCode.REVIEW_ALREADY_EXIST, exception.getErrorCode());
  }

  @Test
  void registerReviewFail_CUSTOMER_NOT_MATCH() {
    //given
    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Customer customer = Customer.builder()
        .id(1L)
        .build();

    Customer customer2 = Customer.builder()
        .id(2L)
        .build();

    given(customerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(customer));

    ReviewRegisterServiceForm form = ReviewRegisterServiceForm.builder()
        .orderId(1L)
        .rating(4)
        .contents("리뷰")
        .build();

    Seller seller = Seller.builder()
        .id(1L)
        .build();

    Product product = Product.builder()
        .seller(seller)
        .build();

    Ordered ordered = Ordered.builder()
        .id(1L)
        .customer(customer2)
        .product(product)
        .build();

    given(orderedRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(ordered));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> reviewService.register("token", form));

    //then
    assertEquals(ErrorCode.CUSTOMER_NOT_MATCH, exception.getErrorCode());
  }

  @Test
  void getReviewsSuccess() {
    //given
    List<Review> reviewList = new ArrayList<>();
    List<ReviewComment> reviewCommentList = new ArrayList<>();

    for (int i = 0; i < 3; i++) {
      Customer customer = Customer.builder()
          .email(i + "@naver.com")
          .build();
      Review review = Review.builder()
          .customer(customer)
          .rating(i + 5)
          .contents("리뷰내용")
          .reviewCommentList(reviewCommentList)
          .build();
      reviewList.add(review);
    }

    given(reviewRepository.findByProductId(anyLong(), any()))
        .willReturn(reviewList);

    //when
    List<ReviewDto> result = reviewService.getReviews(1L, 2, 5);

    //then
    assertEquals(3, result.size());
  }

  @Test
  void getReviewsFail_REVIEW_NOT_FOUND() {
    //given
    List<Review> reviewList = new ArrayList<>();

    given(reviewRepository.findByProductId(anyLong(), any()))
        .willReturn(reviewList);

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> reviewService.getReviews(1L, 2, 5));

    //then
    assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void registerReviewCommentSuccess() {
    //given
    ReviewCommentRegisterServiceForm form = ReviewCommentRegisterServiceForm.builder()
        .reviewId(1L)
        .contents("리뷰")
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Seller seller = Seller.builder()
        .id(1L)
        .build();

    Review review = Review.builder()
        .id(1L)
        .seller(seller)
        .build();

    given(sellerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(seller));

    given(reviewRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(review));

    //when
    reviewService.registerReviewComment("token", form);

    //then
    verify(reviewCommentRepository, times(1)).save(any(ReviewComment.class));
  }

  @Test
  void registerReviewCommentFail_INVALID_REVIEW_FORM() {
    //given
    ReviewCommentRegisterServiceForm form = ReviewCommentRegisterServiceForm.builder()
        .reviewId(1L)
        .contents("")
        .build();

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> reviewService.registerReviewComment("token", form));

    //then
    assertEquals(ErrorCode.INVALID_REVIEW_FORM, exception.getErrorCode());
  }

  @Test
  void registerReviewCommentFail_REVIEW_NOT_FOUND() {
    //given
    ReviewCommentRegisterServiceForm form = ReviewCommentRegisterServiceForm.builder()
        .reviewId(1L)
        .contents("댓글")
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Seller seller = Seller.builder()
        .id(1L)
        .build();

    given(sellerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(seller));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> reviewService.registerReviewComment("token", form));

    //then
    assertEquals(ErrorCode.REVIEW_NOT_FOUND, exception.getErrorCode());
  }

  @Test
  void registerReviewCommentFail_SELLER_NOT_MATCH() {
    //given
    ReviewCommentRegisterServiceForm form = ReviewCommentRegisterServiceForm.builder()
        .reviewId(1L)
        .contents("댓글")
        .build();

    given(provider.getUserVo(anyString()))
        .willReturn(new UserVo(1L, "yjjjwww@naver.com"));

    Seller seller = Seller.builder()
        .id(1L)
        .build();

    Seller seller2 = Seller.builder()
        .id(2L)
        .build();

    Review review = Review.builder()
        .id(1L)
        .seller(seller2)
        .build();

    given(sellerRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(seller));

    given(reviewRepository.findById(anyLong()))
        .willReturn(Optional.ofNullable(review));

    //when
    CustomException exception = assertThrows(CustomException.class,
        () -> reviewService.registerReviewComment("token", form));

    //then
    assertEquals(ErrorCode.SELLER_NOT_MATCH, exception.getErrorCode());
  }
}