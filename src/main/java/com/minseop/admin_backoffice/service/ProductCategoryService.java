package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.ProductCategory;
import com.minseop.admin_backoffice.repository.ProductCategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    // 전체 카테고리 조회
    public List<ProductCategory> findAll() {
        return productCategoryRepository.findAll();
    }

    // ID로 카테고리 조회 (없으면 예외)
    public ProductCategory findById(Long id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("카테고리를 찾을 수 없습니다. id=" + id));
    }

    // 카테고리 이름 중복 체크
    public boolean existsByName(String name) {
        return productCategoryRepository.findByName(name).isPresent();
    }

    // 카테고리 생성
    public ProductCategory create(ProductCategory category) {
        if (existsByName(category.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }
        return productCategoryRepository.save(category);
    }

    // 카테고리 수정
    public ProductCategory update(Long id, ProductCategory updatedCategory) {
        ProductCategory existing = findById(id);
        if (!existing.getName().equals(updatedCategory.getName()) && existsByName(updatedCategory.getName())) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 이름입니다.");
        }
        existing.setName(updatedCategory.getName());
        return productCategoryRepository.save(existing);
    }

    // 카테고리 삭제
    public void delete(Long id) {
        ProductCategory existing = findById(id);
        productCategoryRepository.delete(existing);
    }

    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }
}