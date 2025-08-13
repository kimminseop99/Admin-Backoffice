package com.minseop.admin_backoffice.repository;

import com.minseop.admin_backoffice.domain.Order;
import com.minseop.admin_backoffice.domain.ProductCategory;
import com.minseop.admin_backoffice.domain.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(UserEntity user);

    Optional<Order> findByIdAndUser(Long id, UserEntity user);

    @Query("SELECT o FROM Order o " +
            "JOIN FETCH o.user u " +
            "WHERE (:searchKeyword IS NULL OR LOWER(u.username) LIKE LOWER(CONCAT('%', :searchKeyword, '%')))")
    Page<Order> findOrdersByUsernameContaining(@Param("searchKeyword") String searchKeyword, Pageable pageable);
}
