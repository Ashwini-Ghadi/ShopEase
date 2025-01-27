package com.shopping.service;

import com.shopping.entity.User;

public interface UserService {

	 User findUserByJwtToken(String jwt) throws Exception;
	
	 User findUserByEmail(String email) throws Exception;
}
