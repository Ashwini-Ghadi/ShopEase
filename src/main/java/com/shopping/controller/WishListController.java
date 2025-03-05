package com.shopping.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.entity.WishList;
import com.shopping.service.ProductService;
import com.shopping.service.UserService;
import com.shopping.service.WishListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishListController {

	private final WishListService wishListService;
	private final UserService userService;
	private final ProductService productService;
	
	public ResponseEntity<WishList> getWishlistByUserId(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		WishList wishlist = wishListService.getWishlistByUserId(user);
		return ResponseEntity.ok(wishlist);
	}
	
	@PostMapping("/add-product/{productId}")
	public ResponseEntity<WishList> addProductToWishList(
			@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		Product product = productService.findProductById(productId);
		User user =userService.findUserByJwtToken(jwt);
		WishList updatedWishList = wishListService.addProductToWishlist(user, product);
		return ResponseEntity.ok(updatedWishList);
	}
}
