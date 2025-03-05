package com.shopping.service;

import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.entity.WishList;

public interface WishListService {

	WishList createWishlist(User user);
	WishList getWishlistByUserId(User user);
	WishList addProductToWishlist(User user, Product product);
}
