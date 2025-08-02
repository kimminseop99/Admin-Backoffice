package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.ProductCategory;
import com.minseop.admin_backoffice.service.ProductCategoryService;
import com.minseop.admin_backoffice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class ProductCategoryController {

    private final ProductService productService;

    // 카테고리 목록 조회
    @GetMapping
    public String listCategories(Model model) {
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/product/category-list";
    }

    // 카테고리 등록 폼
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("category", new ProductCategory());
        return "admin/product/category-form";
    }

    // 카테고리 등록 처리
    @PostMapping("/new")
    public String createCategory(@Valid @ModelAttribute("category") ProductCategory category,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/product/category-form";
        }
        productService.createCategory(category);
        return "redirect:/admin/categories";
    }

    // 카테고리 수정 폼
    @GetMapping("/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        ProductCategory category = productService.getCategoryById(id);
        model.addAttribute("category", category);
        return "admin/product/category-form";
    }

    // 카테고리 수정 처리
    @PostMapping("/{id}/edit")
    public String updateCategory(@PathVariable Long id,
                                 @Valid @ModelAttribute("category") ProductCategory category,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "admin/product/category-form";
        }
        productService.updateCategory(id, category);
        return "redirect:/admin/categories";
    }

    // 카테고리 삭제 처리
    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id) {
        productService.deleteCategory(id);
        return "redirect:/admin/categories";
    }
}