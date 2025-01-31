package com.shopping.service;

import com.shopping.domain.User_Role;
import com.shopping.request.LoginRequest;
import com.shopping.response.AuthResponse;
import com.shopping.response.SignupRequest;

public interface AuthService {
	String createUser(SignupRequest req) throws Exception; 

	void sentLoginOtp(String email, User_Role role) throws Exception;

	AuthResponse signin(LoginRequest req);

}
