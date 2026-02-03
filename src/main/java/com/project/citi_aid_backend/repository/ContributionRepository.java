package com.project.citi_aid_backend.repository;

import com.project.citi_aid_backend.model.Contribution;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContributionRepository extends MongoRepository<Contribution, String> {
    List<Contribution> findByComplaint_Id(String complaintId);
    List<Contribution> findByContributor_Id(String contributorId);
    List<Contribution> findByComplaint_IdAndContributor_Id(String complaintId, String contributorId);
}
