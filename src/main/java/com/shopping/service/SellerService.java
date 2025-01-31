package com.shopping.service;

import java.util.List;

import com.shopping.domain.AccountStatus;
import com.shopping.entity.Seller;

public interface SellerService {
	
	Seller getSellerProfile(String jwt) throws Exception;
	Seller createSeller(Seller seller) throws Exception;
	Seller getSellerById(Long id) throws Exception;
	Seller getSellerByEmail(String email) throws Exception;
	List<Seller> getAllSellers(AccountStatus status);
	Seller updateSeller(Long id, Seller seller) throws Exception;
	void deleteSeller(Long id) throws Exception;
	Seller verifyEmail(String email, String otp) throws Exception;
	Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;
	
}
