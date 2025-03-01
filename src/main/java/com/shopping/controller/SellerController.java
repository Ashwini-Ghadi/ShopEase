package com.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.config.JwtProvider;
import com.shopping.domain.AccountStatus;
import com.shopping.entity.Seller;
import com.shopping.entity.SellerReport;
import com.shopping.entity.VerificationCode;
import com.shopping.repository.VerificationCodeRepository;
import com.shopping.request.LoginRequest;
import com.shopping.response.ApiResponse;
import com.shopping.response.AuthResponse;
import com.shopping.service.AuthService;
import com.shopping.service.EmailService;
import com.shopping.service.SellerReportService;
import com.shopping.service.SellerService;
import com.shopping.utils.OtpUtil;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@AllArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
	
	private final SellerService sellerService;
	private final VerificationCodeRepository verificationCodeRepository;
	private final AuthService authService;
	private final EmailService emailService;
	private final JwtProvider jwtProvider;
	private final SellerReportService sellerReportService;
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception{
		
		String otp = req.getOtp();
		String email = req.getEmail();

		req.setEmail("seller_" +email);
		//System.out.println(otp +" -  "+email);

		AuthResponse authResponse = authService.signin(req);
		return ResponseEntity.ok(authResponse);
		
	}

	@PatchMapping("/verify/{otp}")
	public ResponseEntity<Seller> verifySellerEmail(@PathVariable String otp) throws Exception{
		
		VerificationCode verificationCode =  verificationCodeRepository.findByOtp(otp);
		if(verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			throw new Exception("wrong otp...");
		}
		Seller seller =  sellerService.verifyEmail(verificationCode.getEmail(), otp);
		return new ResponseEntity<>(seller,HttpStatus.OK);
		
	}
	
	@PostMapping
	public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception{
		
		Seller savedSeller = sellerService.createSeller(seller);
		String otp = OtpUtil.generateOtp();
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(seller.getEmail());
		verificationCodeRepository.save(verificationCode);
		
		String subject = "ShopEase Email Verification Code";
		String text = "Welcome to ShopEase, verify your account using this link";
		String frontend_url = " http://localhost:8080/verify-seller/";
		emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject, text + frontend_url);
		
		return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws Exception {
		Seller seller =sellerService.getSellerById(id);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}
	
	@GetMapping("/profile")
	public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
		Seller seller = sellerService.getSellerProfile(jwt);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}
	
	@GetMapping("/report")
	public ResponseEntity<SellerReport> getSellerReport(@RequestHeader("Authorization") String jwt) throws Exception{
		//String email = jwtProvider.getEmailFromJwtToken(jwt);
		Seller seller = sellerService.getSellerProfile(jwt);
		SellerReport report = sellerReportService.getSellerReport(seller);
		return new ResponseEntity<>(report, HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required= false) AccountStatus status){
		List<Seller> sellers = sellerService.getAllSellers(status);
		return ResponseEntity.ok(sellers);
	}
	
	@PatchMapping()
	public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, 
			@RequestBody Seller seller) throws Exception{
		
		Seller profile = sellerService.getSellerProfile(jwt);
		Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
		return ResponseEntity.ok(updatedSeller);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception{
		sellerService.deleteSeller(id);
		return ResponseEntity.noContent().build();
	}

}
