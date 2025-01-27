package com.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.domain.User_Role;
import com.shopping.entity.User;
import com.shopping.entity.VerificationCode;
import com.shopping.repository.UserRepository;
import com.shopping.request.LoginRequest;
import com.shopping.response.ApiResponse;
import com.shopping.response.AuthResponse;
import com.shopping.response.SignupRequest;
import com.shopping.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	
	private UserRepository userRepository;
	private final AuthService authService;


	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception{
		String jwt = authService.createUser(req);
		AuthResponse res = new AuthResponse();
		res.setJwt(jwt);
		res.setMessage("Register Success");
		res.setRole(User_Role.Role_Customer);
		
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/sent/login-signup-otp")
	public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody VerificationCode req) throws Exception{
		
		authService.sentLoginOtp(req.getEmail());
		ApiResponse res = new ApiResponse();
		res.setMessage("otp sent successfully");
	
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception{
		
		AuthResponse authResponse = authService.signin(req);
		
		return ResponseEntity.ok(authResponse);
	}
}
