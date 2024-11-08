package com.shopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.User;
import com.shopping.repository.UserRepository;
import com.shopping.response.SignupRequest;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
//@RequiredArgsConstructor
public class AuthController {
	@Autowired
	private UserRepository userRepository;

	@PostMapping("/signup")
	public ResponseEntity<User> createUserHandler(@RequestBody SignupRequest req){
		User user = new User();
		user.setEmail(req.getEmail());
		user.setFullName(req.getFullName());
		
		User savedUser =userRepository.save(user);
		
		return ResponseEntity.ok(savedUser);
	}
}
