package com.shopping.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.shopping.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

}
