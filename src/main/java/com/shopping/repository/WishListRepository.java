package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long>{

	WishList findByUserId(Long userId);
}
