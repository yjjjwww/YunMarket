package com.yjjjwww.yunmarket.transaction.entity;

import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.entity.BaseEntity;
import java.time.LocalDateTime;
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
public class Transaction extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @ToString.Exclude
  private Customer customer;

  @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY)
  @ToString.Exclude
  private List<Ordered> orderedListList = new ArrayList<>();

  private LocalDateTime transactionDate;
  private Integer transactionPrice;
  private Integer pointUse;
  private boolean deletedYn;
}
