package com.project.citi_aid_backend.repository;

import com.project.citi_aid_backend.model.Complaint;
import com.project.citi_aid_backend.enums.Department;
import com.project.citi_aid_backend.enums.Severity;
import com.project.citi_aid_backend.enums.Status;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComplaintRepository extends MongoRepository<Complaint, String> {
    List<Complaint> findByDepartment(Department department);
    List<Complaint> findBySeverity(Severity severity);
    List<Complaint> findByStatus(Status status);
    List<Complaint> findByAgentId(String agentId);
    List<Complaint> findByCustomerId(String customerId);
}

