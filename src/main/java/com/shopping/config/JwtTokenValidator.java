package com.shopping.config;

import java.io.IOException;
import java.security.Key;
import java.util.List;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

import javax.crypto.SecretKey;
import javax.swing.JComboBox.KeySelectionManager;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity.JwtSpec;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtTokenValidator extends OncePerRequestFilter {

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		String jwt = request.getHeader("Authorization");
		//above str get the token in this format :- Bearer jwt (jwt- actual token)
		if(jwt != null) {
			jwt = jwt.substring(7); //so we need extract the bearer keyword to get the jwt token thats why wrote 7
			
			try {
				SecretKey key = Keys.hmacShaKeyFor(JWT_CONSTANT.SECRET_KEY.getBytes());
				Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
								
				
				String email = String.valueOf(claims.get("email"));
				String authorities = String.valueOf(claims.get("authorities"));
				
				List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
				Authentication authentication = new UsernamePasswordAuthenticationToken(email,null, auths); //password is provided as null
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
				
			}catch (Exception e) {
				throw new BadCredentialsException("Invalid JWT Token.....");
			}
		}
		filterChain.doFilter(request, response);
	}

	
}
