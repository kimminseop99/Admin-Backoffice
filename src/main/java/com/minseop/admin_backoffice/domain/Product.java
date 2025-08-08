package com.minseop.admin_backoffice.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long id;

    @Column(name = "product_name", nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "product_description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "product_price", nullable = false)
    private int price;

    @Column(name = "product_stock", nullable = false)
    private int stock;

    @Column(name = "image_filename")
    private String imageFilename;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    private ProductCategory category;

    private Double averageRating;

    public Long getCategoryId() {
        return category != null ? category.getId() : null;
    }

    public void setCategoryId(Long categoryId) {
        if (this.category == null) {
            this.category = new ProductCategory();
        }
        this.category.setId(categoryId);
    }
}
