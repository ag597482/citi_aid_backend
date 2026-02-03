package com.project.citi_aid_backend.service;

import com.project.citi_aid_backend.dto.response.ContributorResponse;
import com.project.citi_aid_backend.model.Contribution;

import java.util.List;

public interface ContributionService {
    Contribution addContribution(String customerId, String complaintId, int amount);
    List<Contribution> getContributionsByComplaintId(String complaintId);
    List<Contribution> getContributionsByContributorId(String contributorId);
    int calculateFundCollected(String complaintId);
    List<ContributorResponse> getContributorsForComplaint(String complaintId);
}
