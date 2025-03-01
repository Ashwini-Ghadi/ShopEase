package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long>{

	SellerReport findBySellerId(Long sellerId);
}
