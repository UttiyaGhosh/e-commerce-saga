package com.ug.orderservice.repository;

import com.ug.orderservice.model.entity.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.annotation.Transactional;

public interface TransactionRepository extends MongoRepository<Transaction, String>{

}
