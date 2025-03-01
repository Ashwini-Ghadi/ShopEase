package com.shopping.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.Cart;
import com.shopping.entity.CartItems;
import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.exceptions.ProductException;
import com.shopping.request.AddItemRequest;
import com.shopping.response.ApiResponse;
import com.shopping.service.CartItemsService;
import com.shopping.service.CartService;
import com.shopping.service.ProductService;
import com.shopping.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {
	
	private final CartService cartService;
	private final CartItemsService cartItemsService;
	private final UserService userService;
	private final ProductService productService;
	
	@GetMapping()
	public ResponseEntity<Cart> findUserCartHandler(@RequestHeader("Authorization") String jwt)
							throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		Cart cart = cartService.findUserCart(user);
		return new ResponseEntity<Cart>(cart, HttpStatus.OK);
	}
	
	@PutMapping("/add")
	public ResponseEntity<CartItems> addItemToCart(
						@RequestBody AddItemRequest req,
						@RequestHeader("Authorization") String jwt) throws Exception, ProductException{
		
			User user = userService.findUserByJwtToken(jwt);
			Product product = productService.findProductById(req.getProductId());
			CartItems item = cartService.addCartItem(user, product, req.getSize(), req.getQuantity());
			
			ApiResponse res = new ApiResponse();
			res.setMessage("Item Added To Cart Successfully");
			return new ResponseEntity<>(item, HttpStatus.ACCEPTED);
	}
	
	@DeleteMapping("/item/{cartItemId}")
	public ResponseEntity<ApiResponse> deleteCartItemHandler(
											@PathVariable Long cartItemId,
											@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		cartItemsService.removeCartItems(user.getId(), cartItemId);
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Item Removed From Cart Successfully");
		return new ResponseEntity<ApiResponse>(res,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/item/{cartItemId}")
	public ResponseEntity<CartItems> updateCartItemHandler(
						@PathVariable Long cartItemId,
						@RequestBody CartItems cartItem,
						@RequestHeader("Authorization") String jwt)throws Exception{
						
	User user = userService.findUserByJwtToken(jwt);
	CartItems updatedCartItem= null;
	if(cartItem.getQuantity() > 0) {
		updatedCartItem = cartItemsService.updateCartItems(user.getId(), cartItemId, cartItem);
	}
	return new ResponseEntity<>(updatedCartItem, HttpStatus.ACCEPTED);
		
	}
	

}
