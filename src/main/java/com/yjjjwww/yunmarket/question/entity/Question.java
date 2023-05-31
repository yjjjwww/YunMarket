
package com.yjjjwww.yunmarket.question.entity;

import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.entity.BaseEntity;
import com.yjjjwww.yunmarket.product.entity.Product;
import com.yjjjwww.yunmarket.seller.entity.Seller;
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
public class Question extends BaseEntity {

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

  @OneToMany(mappedBy = "question", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Answer> answerList = new ArrayList<>();

  private String contents;
  private boolean deletedYn;
}
