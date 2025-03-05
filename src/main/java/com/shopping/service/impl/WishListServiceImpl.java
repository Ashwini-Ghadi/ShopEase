package com.shopping.service.impl;

import org.springframework.stereotype.Service;

import com.shopping.entity.Product;
import com.shopping.entity.User;
import com.shopping.entity.WishList;
import com.shopping.repository.WishListRepository;
import com.shopping.service.WishListService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService{

	private final WishListRepository wishListRepository;
	@Override
	public WishList createWishlist(User user) {
		WishList wishlist= new WishList();
		wishlist.setUser(user);
		return wishListRepository.save(wishlist);
	}

	@Override
	public WishList getWishlistByUserId(User user) {
		WishList wishlist= wishListRepository.findByUserId(user.getId());
		if(wishlist ==  null) {
			wishlist = createWishlist(user);
		}
		return wishlist;
	}

	@Override
	public WishList addProductToWishlist(User user, Product product) {
		WishList wishlist= getWishlistByUserId(user);
		if(wishlist.getProducts().contains(product)) {
			wishlist.getProducts().remove(product);
		} else {
			wishlist.getProducts().add(product);
		}
		return wishListRepository.save(wishlist);
	}

}
