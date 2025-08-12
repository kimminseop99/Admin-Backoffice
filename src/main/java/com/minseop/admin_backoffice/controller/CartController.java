package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.CartItem;
import com.minseop.admin_backoffice.domain.CartUpdateRequest;
import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.service.CartService;
import com.minseop.admin_backoffice.service.ProductService;
import com.minseop.admin_backoffice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final ProductService productService;
    private final UserService userService;

    @PostMapping("/add")
    public String addToCart(@RequestParam(value = "productId",required = false) Long productId,
                            @RequestParam(value = "quantity",required = false) Integer quantity,
                            @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());
        Product product = productService.getProductById(productId);
        cartService.addItemToCart(user, product, quantity);
        return "redirect:/cart/view";
    }

    @GetMapping("/view")
    public String viewCart(Model model,
                           @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());
        List<CartItem> items = cartService.getCartItems(user);
        int totalPrice = cartService.getCartTotal(user.getId());

        model.addAttribute("items", items);
        model.addAttribute("totalPrice", totalPrice);
        return "cart/view";
    }


    @PostMapping("/remove")
    public String removeFromCart(@RequestParam(value = "cartItemId",required = false) Long cartItemId,
                                 @AuthenticationPrincipal UserDetails userDetails) {
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());
        cartService.removeItemFromCart(user, cartItemId);
        return "redirect:/cart/view";
    }

    @PostMapping("/update")
    @ResponseBody
    public Map<String, Object> updateCartItem(@RequestBody CartUpdateRequest request, Principal principal) {
        Long userId = userService.getUserId(principal.getName());
        int itemTotal = cartService.updateQuantity(userId, request.getCartItemId(), request.getQuantity());
        int cartTotal = cartService.getCartTotal(userId);

        Map<String, Object> res = new HashMap<>();
        res.put("itemTotal", itemTotal);
        res.put("cartTotal", cartTotal);
        return res;
    }

}