package com.minseop.admin_backoffice.repository;

import com.minseop.admin_backoffice.domain.Cart;
import com.minseop.admin_backoffice.domain.CartItem;
import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Optional<Cart> findByUser(UserEntity user);
}



