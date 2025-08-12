package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.CartItem;
import com.minseop.admin_backoffice.domain.Order;
import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.dto.OrderForm;
import com.minseop.admin_backoffice.service.CartService;
import com.minseop.admin_backoffice.service.OrderService;
import com.minseop.admin_backoffice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {

    private final UserService userService;
    private final CartService cartService;
    private final OrderService orderService;
    @GetMapping("/order/form")
    public String showOrderForm(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());
        List<CartItem> cartItems = cartService.getCartItems(user);

        if(cartItems.isEmpty()) {
            return "redirect:/cart/view"; // 장바구니 비어있으면 주문 불가
        }

        int totalPrice = cartItems.stream()
                .mapToInt(i -> i.getProduct().getPrice() * i.getQuantity())
                .sum();

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("orderForm", new OrderForm());

        return "order/form"; // thymeleaf 주문 폼 뷰
    }

    @PostMapping("/order/create")
    public String createOrder(
            @ModelAttribute("orderForm") @Valid OrderForm orderForm,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        if (bindingResult.hasErrors()) {
            // 검증 실패 시 다시 주문 폼으로
            // 장바구니, 총합계 다시 세팅 필요
            UserEntity user = userService.getUserByUsername(userDetails.getUsername());
            List<CartItem> cartItems = cartService.getCartItems(user);
            int totalPrice = cartItems.stream()
                    .mapToInt(i -> i.getProduct().getPrice() * i.getQuantity())
                    .sum();

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", totalPrice);

            return "order/form";
        }

        try {
            UserEntity user = userService.getUserByUsername(userDetails.getUsername());
            orderService.createOrder(user, orderForm);

            return "redirect:/order/complete";
        } catch (Exception e) {
            // 주문 처리 중 예외 발생 시 에러 메시지 전달 후 폼 재노출
            UserEntity user = userService.getUserByUsername(userDetails.getUsername());
            List<CartItem> cartItems = cartService.getCartItems(user);
            int totalPrice = cartItems.stream()
                    .mapToInt(i -> i.getProduct().getPrice() * i.getQuantity())
                    .sum();

            model.addAttribute("cartItems", cartItems);
            model.addAttribute("totalPrice", totalPrice);
            model.addAttribute("errorMessage", "주문 처리 중 오류가 발생했습니다. 다시 시도해 주세요.");

            return "order/form";
        }
    }

    @GetMapping("/order/complete")
    public String showOrderComplete() {
        return "order/complete";
    }

    @GetMapping("/order/detail/{orderId}")
    public String showOrderDetail(@PathVariable("orderId") Long orderId,
                                  @AuthenticationPrincipal UserDetails userDetails,
                                  Model model) {
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());

        Order order = orderService.getOrderByIdAndUser(orderId, user);
        if (order == null) {
            return "redirect:/order/history";
        }

        model.addAttribute("order", order);


        return "order/detail";
    }

    @GetMapping("/order/history")
    public String showOrderHistory(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());

        List<Order> orders = orderService.getOrdersByUser(user);


        model.addAttribute("orders", orders);


        return "order/history";
    }




}
