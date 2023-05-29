package com.yjjjwww.yunmarket.transaction.repository;

import com.yjjjwww.yunmarket.transaction.entity.Ordered;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderedRepository extends JpaRepository<Ordered, Long> {

  List<Ordered> findByCustomerId(Long customerId);

  List<Ordered> findByCustomerIdAndTransactionId(Long customerId, Long transactionId);
}
