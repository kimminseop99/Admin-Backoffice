package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.ProductCategory;
import com.minseop.admin_backoffice.service.ProductCategoryService;
import com.minseop.admin_backoffice.service.ProductService;
import com.minseop.admin_backoffice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;
    private final ProductCategoryService productCategoryService;
    private final ProductService productService;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        model.addAttribute("pageTitle", "Admin Backoffice - Dashboard");

        long totalUsers = userService.getTotalUserCount();
        long newUsersLast7Days = userService.getNewUsersCountLast7Days();

        model.addAttribute("totalUsers", totalUsers);
        model.addAttribute("newUsersLast7Days", newUsersLast7Days);

        // 대시보드 fragment 경로
        model.addAttribute("dashboard", "admin/dashboard :: dashboard");

        return "admin/dashboard";
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/")
    public String showMainPage(Model model) {

        List<ProductCategory> categories = productCategoryService.getAllCategories();
        Map<ProductCategory, Product> randomProducts = new HashMap<>();

        for (ProductCategory category : categories) {
            Product randomProduct = productService.getRandomProductByCategory(category.getId());
            if(randomProduct != null) {
                randomProducts.put(category, randomProduct);
            }
        }

        List<Product> topRatedProducts = productService.getTopRatedProducts(8);

        model.addAttribute("randomProducts", randomProducts);
        model.addAttribute("topRatedProducts", topRatedProducts);

        return "layout/main";
    }

}
