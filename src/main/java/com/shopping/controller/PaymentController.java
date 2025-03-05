package com.shopping.controller;

import org.json.HTTP;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.Order;
import com.shopping.entity.PaymentOrder;
import com.shopping.entity.Seller;
import com.shopping.entity.SellerReport;
import com.shopping.entity.User;
import com.shopping.response.ApiResponse;
import com.shopping.response.PaymentLinkResponse;
import com.shopping.service.PaymentService;
import com.shopping.service.SellerReportService;
import com.shopping.service.SellerService;
import com.shopping.service.TransactionService;
import com.shopping.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payment")
public class PaymentController {

	private final PaymentService paymentService;
	private final UserService userService;
	private final SellerService sellerService;
	private final SellerReportService sellerReportService;
	private final TransactionService transactionService;
	
	@GetMapping("/api/payment/{paymentId}")
	public ResponseEntity<ApiResponse> paymentSuccessHandler(
			@PathVariable String paymentId,
			@RequestParam String paymentLinkId,
			@RequestHeader("Authorization") String jwt) throws Exception{
		
		User user = userService.findUserByJwtToken(jwt);
		PaymentLinkResponse paymentResponse;
		PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
		
		boolean paymentSuccess = paymentService.ProceedPaymentOrder(paymentOrder, paymentId, paymentLinkId);
		
		if(paymentSuccess) {
			for(Order order : paymentOrder.getOrders()) {
				transactionService.createTransaction(order);
				Seller seller = sellerService.getSellerById(order.getSellerId());
				SellerReport report = sellerReportService.getSellerReport(seller);
				report.setTotalOrders(report.getTotalOrders());
				report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
				report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
				sellerReportService.updateSellerReport(report);
				
			}
		}
		
		ApiResponse res = new ApiResponse();
		res.setMessage("Payment Successfully");
		return new ResponseEntity<>(res, HttpStatus.CREATED);
	}
	
}
