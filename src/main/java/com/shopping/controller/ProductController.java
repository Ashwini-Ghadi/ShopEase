package com.shopping.controller;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.Product;
import com.shopping.exceptions.ProductException;
import com.shopping.service.ProductService;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
	
	private final ProductService productService;
	
	@GetMapping("/{productId}")
	public ResponseEntity<Product> getProductById(@PathVariable Long productId)
					throws ProductException{
		
		Product product = productService.findProductById(productId);
	return new ResponseEntity<>(product, HttpStatus.OK);
	
	}
	
	@GetMapping("/search")
	public ResponseEntity<List<Product>> searchProduct(@RequestParam(required = false) String query){
		List<Product> products = productService.searchProducts(query);
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@GetMapping()
	public ResponseEntity<Page<Product>> getAllProducts(
										@RequestParam(required = false) String category,
										@RequestParam(required = false) String brand,
										@RequestParam(required = false) String color,
										@RequestParam(required = false) String size,
										@RequestParam(required = false) Integer minPrice,
										@RequestParam(required = false) Integer maxPrice,
										@RequestParam(required = false) Integer minDiscount,
										@RequestParam(required = false) String sort,
										@RequestParam(required = false) String stock,
										@RequestParam(required = false) Integer pageNumber){
		
		return new ResponseEntity<>(
				productService.getAllProducts(category, color, brand,
								size, minPrice, maxPrice, sort, 
								stock, minDiscount, pageNumber),HttpStatus.OK);
										
	}
	
	
}
