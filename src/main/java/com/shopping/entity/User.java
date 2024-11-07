package com.shopping.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.shopping.domain.User_Role;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	private String email;
	
	private String fullName;
	
	private String mobile;
	
	private User_Role role = User_Role.Role_Customer;
	
	@OneToMany
	private Set<Address> addressess = new HashSet<>();
	
	@ManyToMany
	@JsonIgnore
	private Set<Coupen> usedCoupens = new HashSet<>();
}
