package com.shopping.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.shopping.entity.Product;
import com.shopping.entity.Seller;
import com.shopping.exceptions.ProductException;
import com.shopping.request.CreateProductRequest;

public interface ProductService {

	public Product createProduct(CreateProductRequest req, Seller seller);
	public void deleteProduct(Long productId)throws ProductException;
	public Product updateProduct(Long productId, Product product) throws ProductException;
	Product findProductById(Long productId) throws ProductException;
	List<Product> searchProducts(String query);
	public Page<Product> getAllProducts(
		String category,
		String colors,
		String brand,
		String sizes,
		Integer minPrice,
		Integer maxPrice,
		String sort,
		String stock,
		Integer minDiscount,
		Integer pageNumber //for filtering the products
	);
	List<Product> getProductBySellerId(Long sellerId);
}
