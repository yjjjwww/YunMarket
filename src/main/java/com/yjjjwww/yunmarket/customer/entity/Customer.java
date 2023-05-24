package com.yjjjwww.yunmarket.customer.entity;

import com.yjjjwww.yunmarket.cart.entity.Cart;
import com.yjjjwww.yunmarket.entity.BaseEntity;
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
public class Customer extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String password;
  private String phone;
  private String address;
  private Integer point;
  private boolean deletedYn;

  @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
  @ToString.Exclude
  private List<Cart> cartList = new ArrayList<>();
}
