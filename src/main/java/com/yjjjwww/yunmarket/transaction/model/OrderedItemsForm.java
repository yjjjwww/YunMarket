package com.yjjjwww.yunmarket.transaction.model;

import com.yjjjwww.yunmarket.transaction.entity.Ordered;
import java.time.LocalDateTime;
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
public class OrderedItemsForm {

  private String name;
  private Integer price;
  private Integer quantity;
  private String image;
  private Long transactionId;
  private LocalDateTime transactionDate;
  private boolean transactionCancelled;

  public static List<OrderedItemsForm> of(List<Ordered> orderedList) {
    return orderedList.stream()
        .map(ordered -> OrderedItemsForm.builder()
            .name(ordered.getProduct().getName())
            .price(ordered.getPrice())
            .quantity(ordered.getQuantity())
            .image(ordered.getProduct().getImage())
            .transactionId(ordered.getTransaction().getId())
            .transactionDate(ordered.getTransaction().getTransactionDate())
            .transactionCancelled(ordered.getTransaction().isDeletedYn())
            .build())
        .collect(Collectors.toList());
  }
}
