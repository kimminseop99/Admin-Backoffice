package com.minseop.admin_backoffice.repository;

import com.minseop.admin_backoffice.domain.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);
    Optional<Product> findById(Long id);

    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId ORDER BY function('RAND')")
    List<Product> findRandomProductByCategory(@Param("categoryId") Long categoryId, Pageable pageable);

    @Query(value = "SELECT p FROM Product p LEFT JOIN FETCH p.category c " +
            "WHERE (:keyword IS NULL OR p.name LIKE %:keyword%) " +
            "AND (:categoryId IS NULL OR c.id = :categoryId)",
            countQuery = "SELECT COUNT(p) FROM Product p " +
                    "WHERE (:keyword IS NULL OR p.name LIKE %:keyword%) " +
                    "AND (:categoryId IS NULL OR p.category.id = :categoryId)")
    Page<Product> findProductsWithCategory(@Param("keyword") String keyword,
                                           @Param("categoryId") Long categoryId,
                                           Pageable pageable);

}