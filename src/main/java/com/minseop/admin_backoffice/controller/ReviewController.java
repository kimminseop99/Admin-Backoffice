package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.security.CustomUserDetails;
import com.minseop.admin_backoffice.service.ProductService;
import com.minseop.admin_backoffice.service.ReviewService;
import com.minseop.admin_backoffice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/reviews")
public class ReviewController {

    private final ProductService productService;
    private final ReviewService reviewService;
    private final UserService userService;

    @PostMapping("/create/{productId}")
    public String createReview(@PathVariable("productId") Long productId,
                               @RequestParam(value = "title",required = false) String title,
                               @RequestParam(value = "content",required = false) String content,
                               @RequestParam(value = "rating",required = false) int rating,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        Product product = productService.getProductById(productId);
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());

        reviewService.createReview(product, user, title, content, rating);

        return "redirect:/products/" + productId;
    }
}
