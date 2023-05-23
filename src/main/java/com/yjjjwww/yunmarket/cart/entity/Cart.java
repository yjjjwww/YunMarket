package com.yjjjwww.yunmarket.cart.entity;

import com.yjjjwww.yunmarket.customer.entity.Customer;
import com.yjjjwww.yunmarket.product.entity.Product;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
public class Cart {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @ToString.Exclude
  private Customer customer;

  @ManyToOne
  @JoinColumn(name = "product_id")
  @ToString.Exclude
  private Product product;

  private Integer quantity;
}
