package com.minseop.admin_backoffice.service;

import com.minseop.admin_backoffice.domain.Product;
import com.minseop.admin_backoffice.domain.Review;
import com.minseop.admin_backoffice.domain.UserEntity;
import com.minseop.admin_backoffice.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public List<Review> getReviewsByProduct(Product product) {
        return reviewRepository.findByProductOrderByCreatedAtDesc(product);
    }

    public Review createReview(Product product, UserEntity author, String title, String content, int rating) {
        Review review = Review.builder()
                .product(product)
                .author(author)
                .title(title)
                .content(content)
                .rating(rating)
                .createdAt(LocalDateTime.now())
                .build();
        return reviewRepository.save(review);
    }

    public void deleteReview(Review review) {
        reviewRepository.delete(review);
    }

    public void updateReview(Review review, String title, String content, int rating) {
        review.setTitle(title);
        review.setContent(content);
        review.setRating(rating);
        reviewRepository.save(review);
    }

    public List<Review> getReviewsByProductId(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public double getAverageRatingByProductId(Long productId) {
        List<Review> reviews = reviewRepository.findByProductId(productId);
        if (reviews.isEmpty()) return 0.0;

        double sum = reviews.stream().mapToInt(Review::getRating).sum();
        return Math.round((sum / reviews.size()) * 10.0) / 10.0; // 소수점 한 자리 반올림
    }
}
