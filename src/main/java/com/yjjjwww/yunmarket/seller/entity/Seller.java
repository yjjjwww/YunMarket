package com.yjjjwww.yunmarket.seller.entity;

import com.yjjjwww.yunmarket.entity.BaseEntity;
import com.yjjjwww.yunmarket.product.entity.Product;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Seller extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;
  private String phone;
  private boolean deletedYn;

  @OneToMany(mappedBy = "seller", fetch = FetchType.EAGER)
  @ToString.Exclude
  private List<Product> productList = new ArrayList<>();
}
