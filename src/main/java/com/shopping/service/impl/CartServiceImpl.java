package com.shopping.service.impl;

import org.springframework.stereotype.Service;

import com.shopping.entity.Cart;
import com.shopping.entity.CartItems;
import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.repository.CartItemsRepository;
import com.shopping.repository.CartRepository;
import com.shopping.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
	
	private final CartRepository cartRepository;
	private final CartItemsRepository cartItemsRepository;
	
	@Override
	public CartItems addCartItem(User user, Product product, String size, int quantity) {
		Cart cart = findUserCart(user);
		if (cart.getId() == null) {
	        cart = cartRepository.save(cart);  // Ensure cart is persisted
	    }

		CartItems isPresent =cartItemsRepository.findByCartAndProductAndSize(cart, product, size);
		if(isPresent == null) {
			CartItems cartItems = new CartItems();
			cartItems.setProduct(product);
			cartItems.setQuantity(quantity);
			cartItems.setUserId(user.getId());
			cartItems.setSize(size);
			
			int totalPrice = quantity* product.getSellingPrice();
			cartItems.setSellingPrice(totalPrice);
			cartItems.setMrpPrice(quantity* product.getMrpPrice());
			
			cart.getCartItems().add(cartItems);
			cartItems.setCart(cart);
			
			return cartItemsRepository.save(cartItems);
		}
		
		return isPresent;
	}

	@Override
	public Cart findUserCart(User user) {
		Cart cart = cartRepository.findByUserId(user.getId());
	    // If no cart exists, return an empty cart instead of throwing an error
	    if (cart == null) {
	        return new Cart();
	    }

		int totalPrice = 0;
		int totalDiscountedPrice = 0;
		int totalItem = 0;
		
		for(CartItems cartItem : cart.getCartItems()) {
			totalPrice = cartItem.getMrpPrice();
			totalDiscountedPrice = cartItem.getSellingPrice();
			totalItem = cartItem.getQuantity();
		}
		cart.setTotalMrpPrice(totalItem);
		cart.setTotalItem(totalItem);
		cart.setTotalSellingPrice(totalDiscountedPrice);
		cart.setDiscount(calculateDiscountPercentage(totalPrice,totalDiscountedPrice));
		cart.setTotalItem(totalItem);
		return cart;
	}
	
	private int calculateDiscountPercentage(int mrpPrice, int sellingPrice) {
		if(mrpPrice <=0 ) {
			//throw new IllegalArgumentException("Actual price must be greater than 0");
		return 0;
		}
		double discount = mrpPrice - sellingPrice;
		double discountPercentage = (discount/mrpPrice)*100;
		return (int) discountPercentage;
	}


}
