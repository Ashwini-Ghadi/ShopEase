package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.Deal;

public interface DealRepository extends JpaRepository<Deal, Long>{

}
