package com.shopping.service;

import com.shopping.entity.Cart;
import com.shopping.entity.CartItems;
import com.shopping.entity.Product;
import com.shopping.entity.User;

public interface CartService {

	public CartItems addCartItem(User user, Product product, String size,int quantity);
	
	public Cart findUserCart(User user);
}
