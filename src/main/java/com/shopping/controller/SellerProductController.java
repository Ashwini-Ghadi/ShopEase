package com.shopping.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.Product;
import com.shopping.entity.Seller;
import com.shopping.exceptions.ProductException;
import com.shopping.request.CreateProductRequest;
import com.shopping.service.ProductService;
import com.shopping.service.SellerService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/sellers/products")
public class SellerProductController {

	private final ProductService productService;
	private final SellerService sellerService;
	
	@GetMapping()
	public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader("Authorization") String jwt)
				throws Exception{
		Seller seller = sellerService.getSellerProfile(jwt);
		List<Product> products = productService.getProductBySellerId(seller.getId());
		return new ResponseEntity<>(products, HttpStatus.OK);
	}
	
	@PostMapping()
	public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,
									@RequestHeader("Authorization") String jwt) throws Exception{
		
		Seller seller = sellerService.getSellerProfile(jwt);
		Product product = productService.createProduct(request, seller);
		return new ResponseEntity<>(product, HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
		try {
			productService.deleteProduct(productId);
			return new ResponseEntity<>(HttpStatus.OK);
		}catch (ProductException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PutMapping("/{productId}")
	public ResponseEntity<Product> updateProduct(@PathVariable Long productId,
									@RequestBody Product product){
		try {
			Product updatedProduct = productService.updateProduct(productId, product);
			return new ResponseEntity<>(updatedProduct,HttpStatus.OK);
		} catch (ProductException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);			
		}
	}
}
