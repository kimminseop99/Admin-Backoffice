package com.minseop.admin_backoffice.controller;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.ProductCategory;
import com.minseop.admin_backoffice.service.FileStorageService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FileStorageService fileStorageService;

    // 상품 목록 조회 (페이징, 검색, 필터링)
    @GetMapping
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "categoryId", required = false) Long categoryId,
                               Model model) {

        List<Product> products = productService.getAllProductsWithCategory(keyword, categoryId);

        model.addAttribute("products", products); // Page -> List
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
                                @RequestParam("imageFile") MultipartFile imageFile,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            return "admin/product/form";
        }
        if (!imageFile.isEmpty()) {
            String filename = fileStorageService.store(imageFile);
            product.setImageFilename(filename);
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
                                @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("categories", productService.getAllCategories());
            return "admin/product/form";
        }

        // DB에서 기존 상품 가져오기
        Product existingProduct = productService.getProductById(id);

        // 이미지가 새로 업로드 되었으면 덮어쓰기
        if (imageFile != null && !imageFile.isEmpty()) {
            String filename = fileStorageService.store(imageFile);
            existingProduct.setImageFilename(filename);
        }

        // 나머지 필드 업데이트
        existingProduct.setName(product.getName());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setPrice(product.getPrice());
        existingProduct.setStock(product.getStock());
        existingProduct.setCategoryId(product.getCategoryId());

        productService.updateProduct(id, existingProduct);

        return "redirect:/admin/products";
    }


    // 상품 삭제 처리
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }
}
