package com.yjjjwww.yunmarket.review.model;

import com.yjjjwww.yunmarket.review.entity.Review;
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
public class ReviewDto {

  private String customerEmail;
  private Integer rating;
  private String contents;
  private List<ReviewCommentDto> commentDtoList;

  public static List<ReviewDto> toDtoList(List<Review> reviews) {
    return reviews.stream()
        .map(review -> ReviewDto.builder()
            .customerEmail(review.getCustomer().getEmail())
            .rating(review.getRating())
            .contents(review.getContents())
            .commentDtoList(ReviewCommentDto.toDtoList(review.getReviewCommentList()))
            .build())
        .collect(Collectors.toList());
  }
}
