
package com.yjjjwww.yunmarket.review.entity;

import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.entity.BaseEntity;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.seller.entity.Seller;
import com.yjjjwww.yunmarket.transaction.entity.Ordered;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
public class Review extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "product_id")
  @ToString.Exclude
  private Product product;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @ToString.Exclude
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  @ToString.Exclude
  private Seller seller;

  @OneToOne
  @JoinColumn(name = "ordered_id")
  @ToString.Exclude
  private Ordered ordered;

  @OneToMany(mappedBy = "review", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<ReviewComment> reviewCommentList = new ArrayList<>();

  private Integer rating;
  private String contents;
  private boolean deletedYn;
}
