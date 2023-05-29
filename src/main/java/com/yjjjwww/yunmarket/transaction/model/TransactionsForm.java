package com.yjjjwww.yunmarket.transaction.model;

import com.yjjjwww.yunmarket.transaction.entity.Transaction;
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
public class TransactionsForm {

  private Long id;
  private Integer price;
  private Integer pointUse;
  private LocalDateTime transactionDate;
  private boolean transactionCancelled;

  public static List<TransactionsForm> of(List<Transaction> transactionList) {
    return transactionList.stream()
        .map(transaction -> TransactionsForm.builder()
            .id(transaction.getId())
            .price(transaction.getTransactionPrice())
            .pointUse(transaction.getPointUse())
            .transactionDate(transaction.getTransactionDate())
            .transactionCancelled(transaction.isDeletedYn())
            .build())
        .collect(Collectors.toList());
  }
}
