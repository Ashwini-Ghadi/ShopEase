package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.VerificationCode;

public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long>{

	VerificationCode findByEmail(String email);
}
