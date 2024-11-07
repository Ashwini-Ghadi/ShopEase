package com.shopping.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.response.ApiResponse;

@RestController
public class HomeController {

	@GetMapping
	public ApiResponse HomeControllerHandler() {
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Welcome to ShopEase 🥳🥳");
		return apiResponse;
	}
}
