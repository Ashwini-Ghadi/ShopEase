package com.shopping.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopping.entity.Deal;
import com.shopping.response.ApiResponse;
import com.shopping.service.DealService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {

	private final DealService dealService;
	
	@PostMapping()
	public ResponseEntity<Deal> createDeals(@RequestBody Deal deal){
		Deal createdDeals = dealService.createDeal(deal);
		return new ResponseEntity<>(createdDeals, HttpStatus.ACCEPTED);
	}
	
	@PatchMapping("/{id}")
	public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) throws Exception{
		Deal updatedDeal = dealService.updateDeal(deal, id);
		return ResponseEntity.ok(updatedDeal);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteDeals(@PathVariable Long id) throws Exception{
		dealService.deleteDeal(id);
		ApiResponse apiResponse = new ApiResponse();
		apiResponse.setMessage("Deal deleted");
		return new ResponseEntity<>(apiResponse, HttpStatus.ACCEPTED);
	}
}
