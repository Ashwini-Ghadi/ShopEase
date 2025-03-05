package com.shopping.service;

import java.util.Set;

import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.shopping.entity.Order;
import com.shopping.entity.PaymentOrder;
import com.shopping.entity.User;
import com.stripe.exception.StripeException;

public interface PaymentService {
 
	PaymentOrder createOrder(User user, Set<Order> orders);
	PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
	PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;
	Boolean ProceedPaymentOrder(PaymentOrder paymentOrder, String paymentId, String PaymentLinkId) throws RazorpayException;
	PaymentLink createRazorpayPaymentLink(User user , Long amount, Long orderId) throws RazorpayException;
	String createStripePaymentLink(User user, Long amount, Long orderId) throws StripeException;

}
