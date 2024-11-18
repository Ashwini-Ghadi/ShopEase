package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.Seller;
import java.util.List;


public interface SellerRepository extends JpaRepository<Seller, Long>{

	Seller findByEmail(String email);
}
