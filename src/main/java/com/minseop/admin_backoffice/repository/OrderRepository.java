package com.minseop.admin_backoffice.repository;

import com.minseop.admin_backoffice.domain.Order;
import com.minseop.admin_backoffice.domain.ProductCategory;
import com.minseop.admin_backoffice.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUserOrderByOrderDateDesc(UserEntity user);

    Optional<Order> findByIdAndUser(Long id, UserEntity user);
}
