package com.shopping.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.VerificationCode;
import com.shopping.repository.VerificationCodeRepository;
import com.shopping.request.LoginRequest;
import com.shopping.response.ApiResponse;
import com.shopping.response.AuthResponse;
import com.shopping.service.AuthService;
import com.shopping.service.SellerService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/sellers")
public class SellerController {
	
	private final SellerService sellerService;
	private final VerificationCodeRepository verificationCodeRepository;
	private final AuthService authService;
	
	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception{
		
		String otp = req.getOtp();
		String email = req.getEmail();
		
//		VerificationCode verificationCode = verificationCodeRepository.findByEmail(email);
//		if(verificationCode == null || verificationCode.getOtp().equals(req.getOtp())) {
//			throw new Exception("Wrong otp.....");
//		}
		
		req.setEmail("seller_" +email);
		AuthResponse authResponse = authService.signin(req);
		return ResponseEntity.ok(authResponse);
		
	}
	
//	@PostMapping("/sent/login-otp")
//	public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode req) throws Exception{
//		
//		authService.sentLoginOtp(req.getEmail());
//		ApiResponse res = new ApiResponse();
//		res.setMessage("otp sent successfully");
//	
//		return ResponseEntity.ok(res);
//	}

}
