package com.yjjjwww.yunmarket.review.model;

import com.yjjjwww.yunmarket.review.entity.ReviewComment;
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
public class ReviewCommentDto {

  private String sellerEmail;
  private String contents;

  public static List<ReviewCommentDto> toDtoList(List<ReviewComment> reviewCommentList) {
    return reviewCommentList.stream()
        .map(reviewComment -> ReviewCommentDto.builder()
            .sellerEmail(reviewComment.getSeller().getEmail())
            .contents(reviewComment.getContents())
            .build())
        .collect(Collectors.toList());
  }
}
