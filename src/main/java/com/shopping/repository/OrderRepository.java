package com.shopping.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.Order;

public interface OrderRepository extends JpaRepository<Order, Long>{
	
	List<Order> findByUserId(Long userId);
	List<Order> findBySellerId(Long sellerId);

}
