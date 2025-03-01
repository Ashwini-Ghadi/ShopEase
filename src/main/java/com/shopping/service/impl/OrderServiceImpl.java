package com.shopping.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.shopping.domain.OrderStatus;
import com.shopping.domain.PaymentStatus;
import com.shopping.entity.Address;
import com.shopping.entity.Cart;
import com.shopping.entity.CartItems;
import com.shopping.entity.Order;
import com.shopping.entity.OrderItem;
import com.shopping.entity.User;
import com.shopping.repository.AddressRepository;
import com.shopping.repository.OrderItemRepository;
import com.shopping.repository.OrderRepository;
import com.shopping.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService{
	
	private final OrderRepository orderRepository;
	private final AddressRepository addressRepository;
	private final OrderItemRepository orderItemRepository;
	
	@Override
	public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
		if(!user.getAddressess().contains(shippingAddress)) {
			user.getAddressess().add(shippingAddress);
		}
		Address address = addressRepository.save(shippingAddress);
		
		//creating listfor different sellers for different order , or brand
		// eg. brand 1= 2shirts, brand 2 = 2 pants both sellers are different
		Map<Long, List<CartItems>> itemsBySeller = cart.getCartItems().stream()
					.collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));
		
		Set<Order> orders= new HashSet<>();
		
		for(Map.Entry<Long, List<CartItems>> entry :itemsBySeller.entrySet()) {
			Long sellerId = entry.getKey();
			List<CartItems> items = entry.getValue();
			
			int totalOrderPrice = items.stream().mapToInt(CartItems::getSellingPrice).sum();
			int totalItems = items.stream().mapToInt(CartItems::getQuantity).sum();
			
			Order createdOrder = new Order();
			createdOrder.setUser(user);
			createdOrder.setSellerId(sellerId);
			createdOrder.setTotalMrpPrice(totalOrderPrice);
			createdOrder.setTotalSellingPrice(totalOrderPrice);
			createdOrder.setTotalItem(totalItems);
			createdOrder.setShippingAddress(address);
			createdOrder.setOrderStatus(OrderStatus.PENDING);
			createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);
			
			Order savedOrder = orderRepository.save(createdOrder);
			orders.add(savedOrder);
			
			List<OrderItem> orderItems = new ArrayList<>();
			for(CartItems item : items) {
				OrderItem orderItem = new OrderItem();
				orderItem.setOrder(savedOrder);
				orderItem.setMrpPrice(item.getMrpPrice());
				orderItem.setProduct(item.getProduct());
				orderItem.setQuantity(item.getQuantity());
				orderItem.setSize(item.getSize());
				orderItem.setUserId(item.getUserId());
				orderItem.setSellingPrice(item.getSellingPrice());
				
				savedOrder.getOrderItems().add(orderItem);
				
				OrderItem savedOrderItem = orderItemRepository.save(orderItem);
				orderItems.add(savedOrderItem);
			}
		}
		
		return orders;
	}

	@Override
	public Order findOrderById(Long id) throws Exception {
		return orderRepository.findById(id).orElseThrow(() -> new Exception("order not found....."));
	}

	@Override
	public List<Order> userOrderHistory(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	@Override
	public List<Order> sellersOrder(Long sellerId) {
		return orderRepository.findBySellerId(sellerId);
	}

	@Override
	public Order updateOrderStatus(Long orderId, OrderStatus orderStatus) throws Exception {
		Order order = findOrderById(orderId);
		order.setOrderStatus(orderStatus);
		return orderRepository.save(order);
	}

	@Override
	public Order cancleOrder(Long orderId, User user) throws Exception {
		Order order = findOrderById(orderId);
		if(!user.getId().equals(order.getUser().getId())) {
			throw new Exception("you don't have access to this order");
		}
		order.setOrderStatus(OrderStatus.CANCELLED);
		return orderRepository.save(order);
	}

	@Override
	public OrderItem getOrderItemById(Long id) throws Exception {
		return orderItemRepository.findById(id).orElseThrow(()-> new Exception("order item not exist....."));
	}

}
