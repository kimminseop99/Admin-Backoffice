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

    @Column(name = "product_image_url")
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id")
    private ProductCategory category;
}
