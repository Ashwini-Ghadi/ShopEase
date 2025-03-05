package com.shopping.service;

import java.util.List;

import com.shopping.entity.Product;
import com.shopping.entity.Review;
import com.shopping.entity.User;
import com.shopping.request.CreateReviewRequest;

public interface ReviewService {

	Review createReview(CreateReviewRequest req, User user, Product product);
	List<Review> getReviewByProductId(Long productId);
	Review updateReview(Long reviewId, String reviewText, double rating, Long userId) throws Exception;
	void deleteReview(Long reviewId, Long userId) throws Exception;
	Review getReviewById(Long reviewId) throws Exception;
}
