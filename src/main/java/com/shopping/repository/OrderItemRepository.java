package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long>{

}
