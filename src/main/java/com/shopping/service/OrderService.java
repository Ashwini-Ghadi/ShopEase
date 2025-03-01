package com.shopping.service;

import java.util.List;
import java.util.Set;

import com.shopping.domain.OrderStatus;
import com.shopping.entity.Address;
import com.shopping.entity.Cart;
import com.shopping.entity.Order;
import com.shopping.entity.OrderItem;
import com.shopping.entity.User;

public interface OrderService {

	Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
	Order findOrderById(Long id) throws Exception;
	List<Order> userOrderHistory(Long userId);
	List<Order> sellersOrder(Long sellerId);
	Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception;
	Order cancleOrder(Long orderId, User user) throws Exception;
	OrderItem getOrderItemById(Long id) throws Exception;
}
