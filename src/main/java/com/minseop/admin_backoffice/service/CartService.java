package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.Cart;
import com.minseop.admin_backoffice.domain.CartItem;
import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.repository.CartItemRepository;
import com.minseop.admin_backoffice.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    @Transactional
    public Cart getOrCreateCart(UserEntity user) {
        return cartRepository.findByUser(user)
                .orElseGet(() -> cartRepository.save(
                        Cart.builder()
                                .user(user)
                                .createdAt(LocalDateTime.now())
                                .build()
                ));
    }

    @Transactional
    public void addItemToCart(UserEntity user, Product product, int quantity) {
        Cart cart = getOrCreateCart(user);

        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElseGet(() -> CartItem.builder()
                        .cart(cart)
                        .product(product)
                        .quantity(0)
                        .addedAt(LocalDateTime.now())
                        .build()
                );

        cartItem.setQuantity(cartItem.getQuantity() + quantity);
        cartItemRepository.save(cartItem);
    }

    @Transactional
    public void removeItemFromCart(UserEntity user, Long cartItemId) {
        Cart cart = getOrCreateCart(user);
        cartItemRepository.findById(cartItemId).ifPresent(item -> {
            if (item.getCart().equals(cart)) {
                cartItemRepository.delete(item);
            }
        });
    }

    @Transactional(readOnly = true)
    public List<CartItem> getCartItems(UserEntity user) {
        Cart cart = getOrCreateCart(user);
        return cart.getItems();
    }

    @Transactional
    public int updateQuantity(Long userId, Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findByIdAndUserId(cartItemId, userId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 장바구니 항목"));

        item.setQuantity(quantity);
        cartItemRepository.save(item);

        return item.getProduct().getPrice() * quantity;
    }

    public int getCartTotal(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .mapToInt(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();
    }

}

