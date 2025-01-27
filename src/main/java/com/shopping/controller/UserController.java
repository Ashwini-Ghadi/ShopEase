package com.shopping.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.domain.User_Role;
import com.shopping.entity.User;
import com.shopping.response.AuthResponse;
import com.shopping.response.SignupRequest;
import com.shopping.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	@GetMapping("/users/profile")
	public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		
		return ResponseEntity.ok(user);
	}
}
