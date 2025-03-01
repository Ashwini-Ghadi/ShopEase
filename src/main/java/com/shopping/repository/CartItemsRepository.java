package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.Cart;
import com.shopping.entity.CartItems;
import com.shopping.entity.Product;

public interface CartItemsRepository extends JpaRepository<CartItems, Long>{

	CartItems findByCartAndProductAndSize(Cart cart, Product product, String size);
	
}
