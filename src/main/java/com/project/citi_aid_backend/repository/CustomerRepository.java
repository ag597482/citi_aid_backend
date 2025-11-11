package com.project.citi_aid_backend.repository;

import com.project.citi_aid_backend.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {
    Optional<Customer> findByName(String name);
    Optional<Customer> findByEmail(String email);
}

