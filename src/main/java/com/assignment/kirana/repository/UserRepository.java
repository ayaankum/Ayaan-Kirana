package com.assignment.kirana.repository;

import com.assignment.kirana.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Integer> {
  User findByUserName(String username);
}
