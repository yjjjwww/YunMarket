package com.yjjjwww.yunmarket.transaction.service;

import com.yjjjwww.yunmarket.transaction.model.OrderedItemsForm;
import com.yjjjwww.yunmarket.transaction.model.TransactionsForm;
import java.util.List;

public interface OrderService {

  /**
   * 상품 주문하기
   */
  void orderItems(String token);

  /**
   * 주문 상품 전체 목록 가져오기
   */
  List<OrderedItemsForm> getTotalOrderedItems(String token);

  /**
   * 전체 주문 정보 가져오기
   */
  List<TransactionsForm> getTotalTransactions(String token);

  /**
   * 한 주문의 주문 상품 가져오기
   */
  List<OrderedItemsForm> getOrderedItemsByTransaction(String token, Long transactionId);
}
