package com.project.citi_aid_backend.repository;

import com.project.citi_aid_backend.model.Agent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AgentRepository extends MongoRepository<Agent, String> {
    Optional<Agent> findByName(String name);
    Optional<Agent> findByPhone(String phone);
    List<Agent> findByDepartment(com.project.citi_aid_backend.enums.Department department);
}

