package com.shopping.service;

import com.shopping.entity.CartItems;

public interface CartItemsService {

	CartItems updateCartItems(Long userId, Long id, CartItems cartItems) throws Exception;
	void removeCartItems(Long userId, Long cartItemsId) throws Exception;
	CartItems findCartItemsById(Long id) throws Exception;
}
