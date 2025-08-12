package com.minseop.admin_backoffice.repository;

import com.minseop.admin_backoffice.domain.Cart;
import com.minseop.admin_backoffice.domain.CartItem;
import com.minseop.admin_backoffice.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    @Query("select ci from CartItem ci join ci.cart c join c.user u where ci.id = :cartItemId and u.id = :userId")
    Optional<CartItem> findByIdAndUserId(@Param("cartItemId") Long cartItemId, @Param("userId") Long userId);

    @Query("select ci from CartItem ci join ci.cart c join c.user u where u.id = :userId")
    List<CartItem> findByUserId(@Param("userId") Long userId);

    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
