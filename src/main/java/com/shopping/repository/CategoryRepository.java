package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long>{
	Category findByCategoryId(String categoryId);

}
