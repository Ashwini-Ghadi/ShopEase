package com.shopping.service.impl;

import org.springframework.stereotype.Service;

import com.shopping.entity.CartItems;
import com.shopping.entity.User;
import com.shopping.repository.CartItemsRepository;
import com.shopping.service.CartItemsService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartItemsServiceImpl implements CartItemsService{
	
	private final CartItemsRepository cartItemsRepository;
	
//	@Override
//	public CartItems updateCartItems(Long userId, Long id, CartItems cartItems) throws Exception {
//		CartItems items = findCartItemsById(id);
//		User cartItemUser = items.getCart().getUser();
//		System.out.println("User ID from token: " + userId);
//		System.out.println("User ID from cartItem: " + items.getUserId());
//		System.out.println("Are they equal? " + userId.equals(items.getUserId()));
//
//		if(cartItemUser.getId().equals(userId)) {
//			 System.err.println("Updating cart item...");
//			items.setQuantity(cartItems.getQuantity());
//			items.setMrpPrice(items.getQuantity() + items.getProduct().getMrpPrice());
//			items.setSellingPrice(items.getQuantity()+items.getProduct().getSellingPrice());
//			 return cartItemsRepository.save(items);
//		}
//		throw new Exception("you can't update this cartItem");
//	}
	@Override
	public CartItems updateCartItems(Long userId, Long id, CartItems cartItems) throws Exception {
	    CartItems items = findCartItemsById(id);
	    
//	    System.out.println("User ID from token: " + userId);
//	    System.out.println("User ID from cartItem: " + items.getUserId());
//	    System.out.println("Are they equal? " + userId.equals(items.getUserId()));

	    // ✅ Use items.getUserId() instead of cartItemUser.getId()
	    if (items.getUserId().equals(userId)) {  
	        items.setQuantity(cartItems.getQuantity());
	        items.setMrpPrice(items.getQuantity() * items.getProduct().getMrpPrice());
	        items.setSellingPrice(items.getQuantity() * items.getProduct().getSellingPrice());

	        return cartItemsRepository.save(items);
	    }

	    throw new Exception("You can't update this cartItem");
	}


	@Override
	public void removeCartItems(Long userId, Long cartItemsId) throws Exception {
		CartItems items = findCartItemsById(cartItemsId);
		  if (items.getUserId().equals(userId)) {
		        cartItemsRepository.delete(items);
		    } else {
		        throw new Exception("You can't delete this item");
		    }
		
	}

	@Override
	public CartItems findCartItemsById(Long id) throws Exception {
		return cartItemsRepository.findById(id).orElseThrow(() -> new Exception("cart item not found with id " + id));
	}

}
