package com.shopping.controller;

import java.util.List;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.domain.PaymentMethod;
import com.shopping.entity.Address;
import com.shopping.entity.Cart;
import com.shopping.entity.Order;
import com.shopping.entity.OrderItem;
import com.shopping.entity.PaymentOrder;
import com.shopping.entity.Seller;
import com.shopping.entity.SellerReport;
import com.shopping.entity.User;
import com.shopping.response.PaymentLinkResponse;
import com.shopping.service.CartService;
import com.shopping.service.OrderService;
import com.shopping.service.SellerReportService;
import com.shopping.service.SellerService;
import com.shopping.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
	
	private final OrderService orderService;
	private final UserService userService;
	private final CartService cartService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	
	@PostMapping()
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(
			@RequestBody Address shippingAddress,
			@RequestParam PaymentMethod paymentMethod,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user =userService.findUserByJwtToken(jwt);
		Cart cart = cartService.findUserCart(user);
		Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);
		
		PaymentLinkResponse res = new PaymentLinkResponse();
//		PaymentOrder paymentOrder = paymentService.createOrder(user,orders);
//		if(paymentMethod.equals(paymentMethod.RAZORPAY)) {
//			PaymentLink payment = paymentService.createRazorPayPaymentLink(user,
//									paymentOrder.getAmount(),
//									paymentOrder.getId());
//			
//			String paymentUrl = payment.get("short_url");
//			String paymentUrlId = payment.get("id");
//			
//			res.setPayment_link_url(paymentUrl);
//			paymentOrder.setPaymentLinkId(paymentUrlId);
//			paymentOrderRepository.save(paymentOrder);
//		
//		} else {
//			String paymentUrl = paymentService.createStripePaymentLink(user,
//					paymentOrder.getAmount(),
//					paymentOrder.getId());
//			res.setPayment_link_url(paymentUrl);
//		}
//		
		return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<Order>> usersOrderHistoryHandler(
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		List<Order> orders =  orderService.userOrderHistory(user.getId());
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt)throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		Order orders = orderService.findOrderById(orderId);
		return new ResponseEntity<>(orders, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/item/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(
			@PathVariable Long orderItemId,
			@RequestHeader("Authorization") String jwt)throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		OrderItem orderItem = orderService.getOrderItemById(orderItemId);
		return new ResponseEntity<>(orderItem, HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancelOrderHandler(
			@PathVariable Long orderId,
			@RequestHeader("Authorization") String jwt)throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		Order order = orderService.cancleOrder(orderId, user);
		
		Seller seller= sellerService.getSellerById(order.getSellerId());
		SellerReport report =sellerReportService.getSellerReport(seller);
		
		report.setCanceledOrders(report.getCanceledOrders()+1);
		report.setTotalRefunds(report.getTotalRefunds()+ order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(report);
		
		return ResponseEntity.ok(order);
	}
	
}
