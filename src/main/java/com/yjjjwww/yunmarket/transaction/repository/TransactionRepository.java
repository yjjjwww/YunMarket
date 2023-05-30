package com.yjjjwww.yunmarket.transaction.repository;

import com.yjjjwww.yunmarket.transaction.entity.Transaction;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

  List<Transaction> findByCustomerId(Long customerId);
}
