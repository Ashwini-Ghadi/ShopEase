package com.shopping.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopping.config.JwtProvider;
import com.shopping.entity.User;
import com.shopping.repository.CartRepository;
import com.shopping.repository.UserRepository;
import com.shopping.repository.VerificationCodeRepository;
import com.shopping.service.EmailService;
import com.shopping.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	private final UserRepository userRepository;
	private final JwtProvider jwtProvider;
	
	@Override
	public User findUserByJwtToken(String jwt) throws Exception {
		
		String email = jwtProvider.getEmailFromJwtToken(jwt);
	
		return this.findUserByEmail(email);
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		if(user==null) {
			throw new Exception("User not found with email - " +email);
		}
		
		return user;
	}

}
