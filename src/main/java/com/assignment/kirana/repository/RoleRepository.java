package com.assignment.kirana.repository;

import com.assignment.kirana.model.Role;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.MongoRepository;
public interface RoleRepository extends MongoRepository<Role, String> {
    Role findByRole(String role);
}