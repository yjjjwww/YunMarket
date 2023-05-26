package com.yjjjwww.yunmarket.transaction.repository;

import com.yjjjwww.yunmarket.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
