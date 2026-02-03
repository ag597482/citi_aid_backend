package com.project.citi_aid_backend.service.impl;

import com.project.citi_aid_backend.dto.response.ContributorResponse;
import com.project.citi_aid_backend.model.Complaint;
import com.project.citi_aid_backend.model.Contribution;
import com.project.citi_aid_backend.model.Customer;
import com.project.citi_aid_backend.repository.ComplaintRepository;
import com.project.citi_aid_backend.repository.ContributionRepository;
import com.project.citi_aid_backend.repository.CustomerRepository;
import com.project.citi_aid_backend.service.ContributionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContributionServiceImpl implements ContributionService {

    @Autowired
    private ContributionRepository contributionRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ComplaintRepository complaintRepository;

    @Override
    public Contribution addContribution(String customerId, String complaintId, int amount) {
        // Validate customer exists
        Optional<Customer> customerOpt = customerRepository.findById(customerId);
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }

        // Validate complaint exists
        Optional<Complaint> complaintOpt = complaintRepository.findById(complaintId);
        if (complaintOpt.isEmpty()) {
            throw new RuntimeException("Complaint not found with id: " + complaintId);
        }

        Customer customer = customerOpt.get();
        Complaint complaint = complaintOpt.get();

        // Create contribution
        Contribution contribution = new Contribution();
        contribution.setContributor(customer);
        contribution.setComplaint(complaint);
        contribution.setAmount(amount);
        contribution.setCreatedAt(LocalDateTime.now());

        return contributionRepository.save(contribution);
    }

    @Override
    public List<Contribution> getContributionsByComplaintId(String complaintId) {
        return contributionRepository.findByComplaint_Id(complaintId);
    }

    @Override
    public List<Contribution> getContributionsByContributorId(String contributorId) {
        return contributionRepository.findByContributor_Id(contributorId);
    }

    @Override
    public int calculateFundCollected(String complaintId) {
        List<Contribution> contributions = contributionRepository.findByComplaint_Id(complaintId);
        return contributions.stream()
                .mapToInt(Contribution::getAmount)
                .sum();
    }

    @Override
    public List<ContributorResponse> getContributorsForComplaint(String complaintId) {
        List<Contribution> contributions = contributionRepository.findByComplaint_Id(complaintId);
        return contributions.stream()
                .map(contribution -> {
                    ContributorResponse response = new ContributorResponse();
                    Customer contributor = contribution.getContributor();
                    response.setContributorId(contributor.getId());
                    response.setContributorName(contributor.getName());
                    response.setContributorEmail(contributor.getEmail());
                    response.setAmount(contribution.getAmount());
                    response.setContributedAt(contribution.getCreatedAt());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
