package com.shopping.request;

import com.shopping.domain.User_Role;

import lombok.Data;

@Data
public class LoginOtpRequest {

	private String email;
	private String otp;
	private User_Role role;
	
}
