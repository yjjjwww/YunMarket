package com.yjjjwww.yunmarket.question.repository;

import com.yjjjwww.yunmarket.question.entity.Question;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {

  List<Question> findByProductId(Long productId, Pageable pageable);
}
