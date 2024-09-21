package com.assignment.kirana.repository;
import com.assignment.kirana.model.TransactionRequest;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface TransactionRepository extends MongoRepository<TransactionRequest, String> {
}


