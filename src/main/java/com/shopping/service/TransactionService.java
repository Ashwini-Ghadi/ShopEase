package com.shopping.service;

import java.util.List;

import com.shopping.entity.Order;
import com.shopping.entity.Seller;
import com.shopping.entity.Transaction;

public interface TransactionService {

	Transaction createTransaction(Order order);
	List<Transaction> getTransactionsBySellerId(Seller seller);
	List<Transaction> getAllTransactions();
}
