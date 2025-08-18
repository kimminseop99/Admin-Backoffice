package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.Review;
import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.repository.ProductRepository;
import com.minseop.admin_backoffice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;

    // 상품별 리뷰 조회
    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    // 상품 ID로 리뷰 조회
    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    // 평균 평점 계산
    private double calculateAverageRating(Product product) {
        List<Review> reviews = reviewRepository.findByProduct(product);
        if (reviews.isEmpty()) return 0.0;
        double avg = reviews.stream().mapToInt(Review::getRating).average().orElse(0.0);
        return Math.round(avg * 10.0) / 10.0; // 소수점 한 자리 반올림
    }

    // ⭐ 리뷰 생성 + 평균 평점 갱신
    @Transactional
    public Review createReview(Product product, UserEntity author, String title, String content, int rating) {
        Review review = Review.builder()
                .product(product)
                .author(author)
                .title(title)
                .content(content)
                .rating(rating)
                .createdAt(LocalDateTime.now())
                .build();
        reviewRepository.save(review);

        // 평균 평점 갱신
        product.setAverageRating(calculateAverageRating(product));
        productRepository.save(product);

        return review;
    }

    // ⭐ 리뷰 수정 + 평균 평점 갱신
    @Transactional
    public void updateReview(Review review, String title, String content, int rating) {
        review.setTitle(title);
        review.setContent(content);
        review.setRating(rating);
        reviewRepository.save(review);

        Product product = review.getProduct();
        product.setAverageRating(calculateAverageRating(product));
        productRepository.save(product);
    }

    // ⭐ 리뷰 삭제 + 평균 평점 갱신
    @Transactional
    public void deleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다. id=" + reviewId));

        Product product = review.getProduct();
        reviewRepository.delete(review);

        // 삭제 후 평균 평점 갱신
        product.setAverageRating(calculateAverageRating(product));
        productRepository.save(product);
    }

    // 리뷰 ID로 상품 ID 조회
    public Long getProductIdByReviewId(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 리뷰를 찾을 수 없습니다. id=" + id));
        return review.getProduct().getId();
    }

}
