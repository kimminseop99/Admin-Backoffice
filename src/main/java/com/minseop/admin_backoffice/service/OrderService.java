package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.*;
import com.minseop.admin_backoffice.dto.OrderForm;
import com.minseop.admin_backoffice.repository.OrderRepository;
import com.minseop.admin_backoffice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    /**
     * 사용자 장바구니를 기반으로 주문을 생성한다.
     * 재고 부족시 예외를 던지고, 주문 저장 후 장바구니를 비운다.
     */
    @Transactional
    public void createOrder(UserEntity user, OrderForm orderForm) {

        // 장바구니 아이템 조회
        List<CartItem> cartItems = cartService.getCartItems(user);
        if (cartItems.isEmpty()) {
            throw new IllegalStateException("장바구니가 비어있습니다.");
        }

        // 재고 검증 및 주문 아이템 생성 준비
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            if (item.getQuantity() > product.getStock()) {
                throw new IllegalStateException(
                        String.format("재고가 부족합니다: %s (재고: %d, 주문수량: %d)",
                                product.getName(), product.getStock(), item.getQuantity())
                );
            }
        }

        // 주문 엔티티 생성 및 기본 정보 설정
        Order order = new Order();
        order.setUser(user);
        order.setRecipientName(orderForm.getRecipientName());
        order.setRecipientPhone(orderForm.getRecipientPhone());
        order.setDeliveryAddress(orderForm.getDeliveryAddress());
        order.setPaymentMethod(orderForm.getPaymentMethod());
        order.setOrderDate(LocalDateTime.now());

        // 주문 상품 상세 엔티티 생성 및 주문에 추가, 재고 차감
        for (CartItem item : cartItems) {
            Product product = item.getProduct();

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(product.getPrice());

            // 양방향 연관관계 설정
            order.addOrderItem(orderItem);

            // 재고 차감
            int updatedStock = product.getStock() - item.getQuantity();
            product.setStock(updatedStock);
            productRepository.save(product);
        }

        // 주문 저장
        orderRepository.save(order);

        // 장바구니 비우기
        cartService.clearCart(user);
    }

    @Transactional(readOnly = true)
    public List<Order> getOrdersByUser(UserEntity user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    @Transactional(readOnly = true)
    public Order getOrderByIdAndUser(Long orderId, UserEntity user) {
        return orderRepository.findByIdAndUser(orderId, user)
                .orElse(null);
    }


}
