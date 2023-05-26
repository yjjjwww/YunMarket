package com.yjjjwww.yunmarket.transaction.controller;

import com.yjjjwww.yunmarket.transaction.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  private static final String ORDER_ITEMS_SUCCESS = "주문 완료";


  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> orderItems(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token
  ) {
    orderService.orderItems(token);
    return ResponseEntity.ok(ORDER_ITEMS_SUCCESS);
  }
}
