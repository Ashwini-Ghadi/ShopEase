package com.shopping.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.service.annotation.PatchExchange;

import com.shopping.entity.Product;
import com.shopping.entity.Review;
import com.shopping.entity.User;
import com.shopping.exceptions.ProductException;
import com.shopping.request.CreateReviewRequest;
import com.shopping.response.ApiResponse;
import com.shopping.service.ProductService;
import com.shopping.service.ReviewService;
import com.shopping.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {

	private final ReviewService reviewService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping("/products/{productId}/reviews")
	public ResponseEntity<List<Review>> getReviewsByProductId(
				@PathVariable Long productId){
		List<Review> reviews = reviewService.getReviewByProductId(productId);
		return ResponseEntity.ok(reviews);
	}
	
	@PostMapping("/products/{productId}/reviews")
	public ResponseEntity<Review> writeReview(
			@RequestBody CreateReviewRequest req,
			@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws Exception, ProductException {
		
		User user = userService.findUserByJwtToken(jwt);
		Product product = productService.findProductById(productId);
		Review review =  reviewService.createReview(req, user, product);
		return  ResponseEntity.ok(review);
		
	}
	
	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<Review> updateReview(
			@RequestBody CreateReviewRequest req,
			@PathVariable Long reviewId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		
		User user = userService.findUserByJwtToken(jwt);
		Review review =  reviewService.updateReview(reviewId,req.getReviewText(),req.getReviewRating(),user.getId());
		return ResponseEntity.ok(review);
	}
	
	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<ApiResponse> deleteReview(
			@RequestBody CreateReviewRequest req,
			@PathVariable Long reviewId,
			@RequestHeader("Authorization") String jwt) throws Exception, ProductException {

		User user = userService.findUserByJwtToken(jwt);
		reviewService.deleteReview(reviewId, user.getId());
		ApiResponse res = new ApiResponse();
		res.setMessage("Review deleted successfully");

		return ResponseEntity.ok(res);
	}
}
