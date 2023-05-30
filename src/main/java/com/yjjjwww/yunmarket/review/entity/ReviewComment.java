
package com.yjjjwww.yunmarket.review.entity;

import com.yjjjwww.yunmarket.entity.BaseEntity;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class ReviewComment extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "review_id")
  @ToString.Exclude
  private Review review;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  @ToString.Exclude
  private Seller seller;

  private String contents;
  private boolean deletedYn;
}
