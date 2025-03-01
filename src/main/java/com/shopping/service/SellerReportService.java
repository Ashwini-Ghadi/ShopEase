package com.shopping.service;

import com.shopping.entity.Seller;
import com.shopping.entity.SellerReport;

public interface SellerReportService {

	SellerReport getSellerReport(Seller seller);
	SellerReport updateSellerReport(SellerReport sellerReport);
}
