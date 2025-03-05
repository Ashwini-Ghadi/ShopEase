package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Long>{
	Coupon findByCode(String code);

}
