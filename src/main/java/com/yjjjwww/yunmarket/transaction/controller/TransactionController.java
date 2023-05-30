package com.yjjjwww.yunmarket.transaction.controller;

import com.yjjjwww.yunmarket.transaction.model.TransactionsForm;
import com.yjjjwww.yunmarket.transaction.service.OrderService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/transaction")
public class TransactionController {

  private final OrderService orderService;

  @GetMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<List<TransactionsForm>> getTotalTransactions(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token
  ) {
    return ResponseEntity.ok(orderService.getTotalTransactions(token));
  }
}
