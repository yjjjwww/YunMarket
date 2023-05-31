package com.yjjjwww.yunmarket.review.repository;

import com.yjjjwww.yunmarket.review.entity.Review;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

  Optional<Review> findByOrderedId(Long orderedId);

  List<Review> findByProductId(Long productId, Pageable pageable);
}
