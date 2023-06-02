package com.yjjjwww.yunmarket.transaction.controller;

import com.yjjjwww.yunmarket.transaction.model.OrderedItemsForm;
import com.yjjjwww.yunmarket.transaction.service.OrderService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/order")
public class OrderController {

  private final OrderService orderService;

  private static final String ORDER_ITEMS_SUCCESS = "주문 완료";

  @ApiOperation(value = "상품 주문하기")
  @PostMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<String> orderItems(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @RequestParam("point") Integer point
  ) {
    orderService.orderItems(token, point);
    return ResponseEntity.ok(ORDER_ITEMS_SUCCESS);
  }

  @ApiOperation(value = "전체 주문 정보 가져오기")
  @GetMapping
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<List<OrderedItemsForm>> getTotalOrderedItems(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token
  ) {
    return ResponseEntity.ok(orderService.getTotalOrderedItems(token));
  }

  @ApiOperation(value = "한 주문 정보에 대한 모든 ordered 조회하기")
  @GetMapping("/transaction/{id}")
  @PreAuthorize("hasRole('CUSTOMER')")
  public ResponseEntity<List<OrderedItemsForm>> getOrderedItemsByTransaction(
      @RequestHeader(name = HttpHeaders.AUTHORIZATION) String token,
      @Parameter(name = "id", description = "주문 정보 아이디")
      @PathVariable("id") long id
  ) {
    return ResponseEntity.ok(orderService.getOrderedItemsByTransaction(token, id));
  }
}
