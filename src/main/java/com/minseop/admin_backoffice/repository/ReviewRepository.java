package com.minseop.admin_backoffice.repository;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProductOrderByCreatedAtDesc(Product product);

    List<Review> findByProductId(Long productId);

    List<Review> findByProduct(Product product);
}