package com.shopping.service.impl;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopping.config.JwtProvider;
import com.shopping.domain.AccountStatus;
import com.shopping.domain.User_Role;
import com.shopping.entity.Address;
import com.shopping.entity.Seller;
import com.shopping.repository.AddressRepository;
import com.shopping.repository.SellerRepository;
import com.shopping.service.SellerService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class SellerServiceImpl implements SellerService {
	
	private final SellerRepository sellerRepository;
	private final JwtProvider jwtProvider;
	private final PasswordEncoder passwordEncoder;
	private final AddressRepository addressRepository;

	@Override
	public Seller getSellerProfile(String jwt) throws Exception {
		String email = jwtProvider.getEmailFromJwtToken(jwt);
		return this.getSellerByEmail(email);
	}

	@Override
	public Seller createSeller(Seller seller) throws Exception {
		Seller sellerExist = sellerRepository.findByEmail(seller.getEmail());
		if(sellerExist != null) {
			throw new Exception("Seller already exist, use differrent email");
		}
		Address SavedAddress = addressRepository.save(seller.getPickupAddress());
		Seller newSeller = new Seller();
		newSeller.setEmail(seller.getEmail());
		newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
		newSeller.setSellerName(seller.getSellerName());
		newSeller.setPickupAddress(SavedAddress);
		newSeller.setGSTIN(seller.getGSTIN());
		newSeller.setRole(User_Role.Role_Seller);
		newSeller.setMobile(seller.getMobile());
		newSeller.setBankDetails(seller.getBankDetails());
		newSeller.setBusinessDetails(seller.getBusinessDetails());
		
		return sellerRepository.save(newSeller);
	}

	@Override
	public Seller getSellerById(Long id) throws Exception {
		return sellerRepository.findById(id)
				.orElseThrow(() -> new Exception("seller not found with id "+id));
	}

	@Override
	public Seller getSellerByEmail(String email) throws Exception {
		Seller seller = sellerRepository.findByEmail(email);
		if(seller == null) {
			throw new Exception("seller not found .....");
		}
		return seller;
	}

	@Override
	public List<Seller> getAllSellers(AccountStatus status) {

		return sellerRepository.findByAccountStatus(status);
	}

	@Override
	public Seller updateSeller(Long id, Seller seller) throws Exception {
		Seller existingSeller = this.getSellerById(id);  //it get by above method
		
		if(seller.getSellerName() != null) {
			existingSeller.setSellerName(seller.getSellerName());
		}
		if(seller.getMobile() != null) {
			existingSeller.setMobile(seller.getMobile());
		}
		if(seller.getEmail() != null) {
			existingSeller.setEmail(seller.getEmail());
		}
		if(seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null) {
			existingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
		}
		if(seller.getBankDetails() != null
				&& seller.getBankDetails().getBankAccountHolderName() != null
				&& seller.getBankDetails().getBankIfscCode() != null
				&& seller.getBankDetails().getBankAccountNumber() != null ) {
			
			existingSeller.getBankDetails().setBankAccountHolderName(seller.getBankDetails().getBankAccountHolderName());
			existingSeller.getBankDetails().setBankIfscCode(seller.getBankDetails().getBankIfscCode());
			existingSeller.getBankDetails().setBankAccountNumber(seller.getBankDetails().getBankAccountNumber());	
		}
		if(seller.getPickupAddress() != null 
				&& seller.getPickupAddress().getAddress() != null
				&& seller.getPickupAddress().getMobile() != null 
				&& seller.getPickupAddress().getCity() != null 
				&& seller.getPickupAddress().getState() != null) {
			
			existingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
			existingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
			existingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
			existingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
			existingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
		}
		if(seller.getGSTIN() != null) {
			existingSeller.setGSTIN(seller.getGSTIN());
		}
		
		return sellerRepository.save(existingSeller);
	}

	@Override
	public void deleteSeller(Long id) throws Exception {
		Seller seller = getSellerById(id);
		sellerRepository.delete(seller);
	}

	@Override
	public Seller verifyEmail(String email, String otp) throws Exception {
		Seller seller =getSellerByEmail(email);
		seller.setEmailVerified(true);
		return sellerRepository.save(seller);
	}

	@Override
	public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {

		Seller seller =getSellerById(sellerId);
		seller.setAccountStatus(status);
		return sellerRepository.save(seller);
	}

}
