package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.Review;
import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user/products")
@RequiredArgsConstructor
public class UserProductController {

    private final ProductService productService;
    private final OrderService orderService;
    private final UserService userService;
    private final ReviewService reviewService;

    @GetMapping
    public String productList(@RequestParam(value = "keyword", required = false) String keyword,
                              @RequestParam(value = "categoryId", required = false) Long categoryId,
                              @PageableDefault(size = 10) Pageable pageable,
                              Model model) {

        Page<Product> page = productService.getProducts(keyword, categoryId, pageable);

        model.addAttribute("products", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", productService.getAllCategories());
        return "user/product/list";
    }


    @GetMapping("/{id}")
    public String showProductDetail(@PathVariable("id") Long id,
                                    Model model,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);

        // 리뷰 목록
        List<Review> reviewList = reviewService.getReviewsByProduct(product);
        model.addAttribute("reviews", reviewList);

        boolean canWriteReview = false;
        if (userDetails != null) {
            UserEntity user = userService.getUserByUsername(userDetails.getUsername());
            model.addAttribute("currentUser", user);

            boolean hasPurchased = orderService.hasUserPurchasedProduct(user.getId(), product.getId());
            boolean alreadyReviewed = reviewService.existsByAuthorIdAndProductId(user.getId(), product.getId());

            // 구매했으면서 아직 리뷰 작성 안했으면 작성 가능
            canWriteReview = hasPurchased && !alreadyReviewed;
        }
        model.addAttribute("canWriteReview", canWriteReview);

        return "user/product/detail";
    }


}
