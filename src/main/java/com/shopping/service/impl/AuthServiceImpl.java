package com.shopping.service.impl;

import java.security.Security;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopping.config.JwtProvider;
import com.shopping.domain.User_Role;
import com.shopping.entity.Cart;
import com.shopping.entity.Seller;
import com.shopping.entity.User;
import com.shopping.entity.VerificationCode;
import com.shopping.repository.CartRepository;
import com.shopping.repository.SellerRepository;
import com.shopping.repository.UserRepository;
import com.shopping.repository.VerificationCodeRepository;
import com.shopping.request.LoginRequest;
import com.shopping.response.AuthResponse;
import com.shopping.response.SignupRequest;
import com.shopping.service.AuthService;
import com.shopping.service.EmailService;
import com.shopping.utils.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final CartRepository cartRepository;
	private final JwtProvider jwtProvider;
	private final VerificationCodeRepository verificationCodeRepository;
	private final EmailService emailService;
	private final CustomUserServiceImpl customUserService;
	private final SellerRepository sellerRepository;
	
	
	@Override
	public String createUser(SignupRequest req) throws Exception {
		
		VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());;
		if(verificationCode == null || !verificationCode.getOtp().equals(req.getOtp()) ) {
			throw new Exception("Wrong otp.....");
		}
		
		
		User user = userRepository.findByEmail(req.getEmail());
		
		if(user== null) {
			User createdUser = new User();
			createdUser.setEmail(req.getEmail());
			createdUser.setFullName(req.getFullName());
			createdUser.setRole(User_Role.Role_Customer);
			createdUser.setMobile("8764534578");
			createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
		
			user = userRepository.save(createdUser);
			
			Cart cart = new Cart();
			cart.setUser(user);
			cartRepository.save(cart);
			
		}
		
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(User_Role.Role_Customer.toString()));
		
		Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(),null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return jwtProvider.generateToken(authentication);
	}

	@Override
	public void sentLoginOtp(String email, User_Role role) throws Exception {
		String SIGNING_PREFIX = "signing_"; //When you send "signing_a@gmail.com", your code removes "signing_"It then looks for "a@gmail.com" in userRepository.findByEmail(email).,
									    	// When you send "signin_a@gmail.com", your code does not match "signing_", so the prefix is not removed.
											//It searches for "signin_a@gmail.com" as is.
		//String SELLER_PREFIX = "seller_";
		
		if(email.startsWith(SIGNING_PREFIX)) {
			email= email.substring(SIGNING_PREFIX.length());
			
			if(role.equals(User_Role.Role_Seller)) {
				Seller seller = sellerRepository.findByEmail(email);
				if(seller == null) {
					throw new Exception("Seller not found .....");
				
				}
			}else {
				User user = userRepository.findByEmail(email);
				if(user == null) {
					throw new Exception("User not exist with provided email.....");
				}
			}
			
		}
		
		VerificationCode isExist = verificationCodeRepository.findByEmail(email);
		if(isExist !=null) {
			verificationCodeRepository.delete(isExist);
		}
		
		String otp = OtpUtil.generateOtp();
		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(email);
		verificationCodeRepository.save(verificationCode);
		
		String subject = "ShopEase login/signup otp";
		String text = "Your login/signup otp is - " +otp;
		
		emailService.sendVerificationOtpEmail(email, otp, subject, text);
	
	}

	@Override
	public AuthResponse signin(LoginRequest req) {
		String username = req.getEmail();
		String otp = req.getOtp();
		
		Authentication authentication = authenticate(username,otp);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		String token =jwtProvider.generateToken(authentication);
		AuthResponse authResponse =new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login Success");
		
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();
		
		authResponse.setRole(User_Role.valueOf(roleName));
		
		return authResponse;
	}

	private Authentication authenticate(String username, String otp) {
		UserDetails userDetails= customUserService.loadUserByUsername(username);
		if(userDetails == null) {
			throw new BadCredentialsException("Invalid username");
		}

		VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
		if (verificationCode == null) {
		    throw new BadCredentialsException("No OTP found for email: " + username);
		}
		if (!verificationCode.getOtp().equals(otp)) {
		    throw new BadCredentialsException("Wrong OTP provided for email: " + username);
		}

		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}


}
