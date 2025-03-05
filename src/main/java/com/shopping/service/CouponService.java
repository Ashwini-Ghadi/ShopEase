package com.shopping.service;

import java.util.List;

import com.shopping.entity.Cart;
import com.shopping.entity.Coupon;
import com.shopping.entity.User;

public interface CouponService {

	Cart applyCoupon(String code, double orderValue, User user) throws Exception;
	Cart removeCoupon(String code, User user) throws Exception;
	Coupon findCouponById(Long id) throws Exception;
	Coupon createCoupon(Coupon coupon);
	List<Coupon> findAllCoupons();
	void deleteCoupon(Long id) throws Exception;
}
