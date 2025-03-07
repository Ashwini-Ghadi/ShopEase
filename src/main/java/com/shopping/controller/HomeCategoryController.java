package com.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.Home;
import com.shopping.entity.HomeCategory;
import com.shopping.service.HomeCategoryService;
import com.shopping.service.HomeService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
//@RequestMapping("/api")
public class HomeCategoryController {

	private final HomeCategoryService homeCategoryService;
	private final HomeService homeService;
	
	@PostMapping("/home/categories")
	public ResponseEntity<Home> createHomeCategories(
			@RequestBody List<HomeCategory> homeCategories){
		List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
		Home home = homeService.createHomePageData(categories);
		return new ResponseEntity<>(home, HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/admin/home-category")
	public ResponseEntity<List<HomeCategory>> getHomeCategory() throws Exception{
		List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
		return ResponseEntity.ok(categories);
	}
	
	@PatchMapping("/admin/home-category/{id}")
	public ResponseEntity<HomeCategory> updateHomeCategory(
			@PathVariable Long id,
			@RequestBody HomeCategory homeCategory) throws Exception{
		
		HomeCategory updatedCategory = homeCategoryService.updateHomeCategory(homeCategory, id);
		return ResponseEntity.ok(updatedCategory);
	}
}
