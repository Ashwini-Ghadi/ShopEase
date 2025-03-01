package com.shopping.service.impl;

import org.springframework.stereotype.Service;

import com.shopping.entity.Seller;
import com.shopping.entity.SellerReport;
import com.shopping.repository.SellerReportRepository;
import com.shopping.service.SellerReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerReportSeviceImpl implements SellerReportService{
	
	private final SellerReportRepository sellerReportRepository;
	
	@Override
	public SellerReport getSellerReport(Seller seller) {
		SellerReport sellerReport = sellerReportRepository.findBySellerId(seller.getId());
		
		if(sellerReport == null) {
			SellerReport newReport= new SellerReport();
			newReport.setSeller(seller);
			return sellerReportRepository.save(newReport);
			
		}
		return sellerReport;
	}

	@Override
	public SellerReport updateSellerReport(SellerReport sellerReport) {
		return sellerReportRepository.save(sellerReport);
	}

}
