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
import org.springframework.web.bind.annotation.*;

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
                               @RequestParam(name = "rating", required = false, defaultValue = "0") Integer rating,
                               @AuthenticationPrincipal CustomUserDetails userDetails) {

        Product product = productService.getProductById(productId);
        UserEntity user = userService.getUserByUsername(userDetails.getUsername());

        reviewService.createReview(product, user, title, content, rating);

        return "redirect:/user/products/" + productId;
    }

    @PostMapping("/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id) {
        Long productId = reviewService.getProductIdByReviewId(id); // 이 메서드를 service에 추가해야 함
        reviewService.deleteReview(id);
        return "redirect:/user/products/" + productId;
    }
}
