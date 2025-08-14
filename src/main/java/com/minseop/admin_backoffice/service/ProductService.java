package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.ProductCategory;
import com.minseop.admin_backoffice.repository.ProductCategoryRepository;
import com.minseop.admin_backoffice.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    // 상품 등록
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    // 상품 수정
    public Product updateProduct(Long id, Product updatedProduct) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));

        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStock(updatedProduct.getStock());
        existing.setCategory(updatedProduct.getCategory());

        return productRepository.save(existing);
    }

    // 상품 삭제
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    // 상품 목록 (검색, 카테고리 필터 포함)
    public Page<Product> getProducts(String keyword, Long categoryId, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            return productRepository.findByNameContainingIgnoreCase(keyword, pageable);
        } else if (categoryId != null) {
            return productRepository.findByCategory_Id(categoryId, pageable);
        } else {
            return productRepository.findAll(pageable);
        }
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("상품이 존재하지 않습니다."));
    }

    // ================================
    // 상품 카테고리
    // ================================

    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }

    public ProductCategory createCategory(ProductCategory category) {
        return productCategoryRepository.save(category);
    }

    public ProductCategory updateCategory(Long id, ProductCategory updatedCategory) {
        ProductCategory existing = productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));
        existing.setName(updatedCategory.getName());
        return productCategoryRepository.save(existing);
    }

    public void deleteCategory(Long id) {
        productCategoryRepository.deleteById(id);
    }

    public ProductCategory getCategoryById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리가 존재하지 않습니다."));
    }

    public Product getRandomProductByCategory(Long categoryId) {
        List<Product> products = productRepository.findRandomProductByCategory(categoryId, PageRequest.of(0, 1));
        return products.isEmpty() ? null : products.get(0);
    }

    public List<Product> getTopRatedProducts(int i) {
        return productRepository.findAll(
                PageRequest.of(0, i, Sort.by(Sort.Direction.DESC, "averageRating"))
        ).getContent();
    }
}
