package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.ProductCategory;
import com.minseop.admin_backoffice.service.ProductCategoryService;
import com.minseop.admin_backoffice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 상품 목록 조회 (페이징, 검색, 필터링)
    @GetMapping
    public String listProducts(@RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "categoryId",required = false) Long categoryId,
                               @PageableDefault(size = 10) Pageable pageable,
                               Model model) {
        Page<Product> page = productService.getProducts(keyword, categoryId, pageable);
        model.addAttribute("products", page);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/product/list";
    }

    // 상품 등록 폼
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/product/form";
    }

    // 상품 등록 처리
    @PostMapping("/new")
    public String createProduct(@Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            return "admin/product/form";
        }
        productService.createProduct(product);
        return "redirect:/admin/products";
    }

    // 상품 수정 폼
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        model.addAttribute("categories", productService.getAllCategories());
        return "admin/product/form";
    }

    // 상품 수정 처리
    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable("id") Long id,
                                @Valid @ModelAttribute("product") Product product,
                                BindingResult bindingResult,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            return "admin/product/form";
        }
        productService.updateProduct(id, product);
        return "redirect:/admin/products";
    }

    // 상품 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
