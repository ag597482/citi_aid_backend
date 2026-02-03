package com.project.citi_aid_backend.service.impl;

import com.project.citi_aid_backend.dto.request.CloseComplaintRequest;
import com.project.citi_aid_backend.dto.request.CreateComplaintRequest;
import com.project.citi_aid_backend.dto.request.UpdateComplaintRequest;
import com.project.citi_aid_backend.dto.response.ComplaintsSummary;
import com.project.citi_aid_backend.model.Agent;
import com.project.citi_aid_backend.model.Complaint;
import com.project.citi_aid_backend.model.Customer;
import com.project.citi_aid_backend.repository.AgentRepository;
import com.project.citi_aid_backend.repository.ComplaintRepository;
import com.project.citi_aid_backend.repository.CustomerRepository;
import com.project.citi_aid_backend.service.ComplaintService;
import com.project.citi_aid_backend.service.ContributionService;
import com.project.citi_aid_backend.enums.Department;
import com.project.citi_aid_backend.enums.Severity;
import com.project.citi_aid_backend.enums.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ComplaintServiceImpl implements ComplaintService {

    @Autowired
    private ComplaintRepository complaintRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContributionService contributionService;

    @Override
    public Complaint createComplaint(CreateComplaintRequest createComplaintRequest) {
        // Find the customer who created the complaint
        Optional<Customer> customerOpt = customerRepository.findById(createComplaintRequest.getCustomerId());
        if (customerOpt.isEmpty()) {
            throw new RuntimeException("Customer not found with id: " + createComplaintRequest.getCustomerId());
        }

        Complaint complaint = new Complaint();
        complaint.setTitle(createComplaintRequest.getTitle());
        complaint.setDescription(createComplaintRequest.getDescription());
        complaint.setLocation(createComplaintRequest.getLocation());
        complaint.setBeforePhoto(createComplaintRequest.getBeforePhoto());
        complaint.setDepartment(createComplaintRequest.getDepartment());
        complaint.setSeverity(createComplaintRequest.getSeverity());
        complaint.setStatus(Status.RAISED);
        complaint.setCustomer(customerOpt.get());
        complaint.setCreatedAt(LocalDateTime.now());
        
        // Handle crowdfunding fields
        if (createComplaintRequest.getCrowdFundingEnabled() != null) {
            complaint.setCrowdFundingEnabled(createComplaintRequest.getCrowdFundingEnabled());
        }
        if (createComplaintRequest.getTargetFund() != null) {
            complaint.setTargetFund(createComplaintRequest.getTargetFund());
        }
        
        complaint = complaintRepository.save(complaint);
        populateCrowdfundingFields(complaint);
        return complaint;
    }

    @Override
    public Optional<Complaint> getComplaintById(String id) {
        Optional<Complaint> complaintOpt = complaintRepository.findById(id);
        complaintOpt.ifPresent(this::populateCrowdfundingFields);
        return complaintOpt;
    }

    @Override
    public List<Complaint> getAllComplaints() {
        List<Complaint> complaints = complaintRepository.findAll();
        complaints.forEach(this::populateCrowdfundingFields);
        return complaints;
    }

    @Override
    public ComplaintsSummary getComplaintsSummary() {
        List<Complaint> complaints = complaintRepository.findAll();
        int openComplaints = (int) complaints.stream().filter(complaint -> complaint.getStatus() == Status.RAISED).count();
        int assignedComplaints = (int) complaints.stream().filter(complaint -> complaint.getStatus() == Status.AGENT_ASSIGNED).count();
        int inProgressComplaints = (int) complaints.stream().filter(complaint -> complaint.getStatus() == Status.IN_PROGRESS).count();
        int fixedComplaints = (int) complaints.stream().filter(complaint -> complaint.getStatus() == Status.FIXED).count();
        return ComplaintsSummary.builder()
            .openComplaints(openComplaints)
            .assignedComplaints(assignedComplaints)
            .inProgressComplaints(inProgressComplaints)
            .fixedComplaints(fixedComplaints)
            .build();
    }

    @Override
    public List<Complaint> getComplaintsByDepartment(Department department) {
        List<Complaint> complaints = complaintRepository.findByDepartment(department);
        complaints.forEach(this::populateCrowdfundingFields);
        return complaints;
    }

    @Override
    public List<Complaint> getComplaintsBySeverity(Severity severity) {
        List<Complaint> complaints = complaintRepository.findBySeverity(severity);
        complaints.forEach(this::populateCrowdfundingFields);
        return complaints;
    }

    @Override
    public List<Complaint> getComplaintsByStatus(Status status) {
        List<Complaint> complaints = complaintRepository.findByStatus(status);
        complaints.forEach(this::populateCrowdfundingFields);
        return complaints;
    }

    @Override
    public List<Complaint> getComplaintsByAgentId(String agentId) {
        List<Complaint> complaints = complaintRepository.findByAgentId(agentId);
        complaints.forEach(this::populateCrowdfundingFields);
        return complaints;
    }

    @Override
    public List<Complaint> getComplaintsByCustomerId(String customerId) {
        List<Complaint> complaints = complaintRepository.findByCustomerId(customerId);
        complaints.forEach(this::populateCrowdfundingFields);
        return complaints;
    }

    @Override
    public Complaint updateComplaint(String id, UpdateComplaintRequest updateComplaintRequest) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(id);
        if (optionalComplaint.isEmpty()) {
            throw new RuntimeException("Complaint not found with id: " + id);
        }

        Complaint complaint = optionalComplaint.get();

        if (updateComplaintRequest.getTitle() != null) {
            complaint.setTitle(updateComplaintRequest.getTitle());
        }
        if (updateComplaintRequest.getDescription() != null) {
            complaint.setDescription(updateComplaintRequest.getDescription());
        }
        if (updateComplaintRequest.getLocation() != null) {
            complaint.setLocation(updateComplaintRequest.getLocation());
        }
        if (updateComplaintRequest.getBeforePhoto() != null) {
            complaint.setBeforePhoto(updateComplaintRequest.getBeforePhoto());
        }
        if (updateComplaintRequest.getAfterPhoto() != null) {
            complaint.setAfterPhoto(updateComplaintRequest.getAfterPhoto());
        }
        if (updateComplaintRequest.getDepartment() != null) {
            complaint.setDepartment(updateComplaintRequest.getDepartment());
        }
        if (updateComplaintRequest.getSeverity() != null) {
            complaint.setSeverity(updateComplaintRequest.getSeverity());
        }
        if (updateComplaintRequest.getStatus() != null) {
            Status oldStatus = complaint.getStatus();
            Status newStatus = updateComplaintRequest.getStatus();
            complaint.setStatus(newStatus);

            // Update timestamps based on status changes
            if (oldStatus == Status.RAISED && newStatus == Status.AGENT_ASSIGNED) {
                complaint.setAssignedAt(LocalDateTime.now());
            }
            if (newStatus == Status.FIXED && complaint.getCompletedAt() == null) {
                complaint.setCompletedAt(LocalDateTime.now());
            }
        }
        if (updateComplaintRequest.getAgentId() != null) {
            Optional<Agent> optionalAgent = agentRepository.findById(updateComplaintRequest.getAgentId());
            if (optionalAgent.isPresent()) {
                complaint.setAgent(optionalAgent.get());
                if (complaint.getStatus() == Status.RAISED) {
                    complaint.setStatus(Status.AGENT_ASSIGNED);
                    complaint.setAssignedAt(LocalDateTime.now());
                }
            } else {
                throw new RuntimeException("Agent not found with id: " + updateComplaintRequest.getAgentId());
            }
        }
        if (updateComplaintRequest.getCrowdFundingEnabled() != null) {
            complaint.setCrowdFundingEnabled(updateComplaintRequest.getCrowdFundingEnabled());
        }
        if (updateComplaintRequest.getTargetFund() != null) {
            complaint.setTargetFund(updateComplaintRequest.getTargetFund());
        }

        complaint = complaintRepository.save(complaint);
        populateCrowdfundingFields(complaint);
        return complaint;
    }

    @Override
    public Complaint assignAgentToComplaint(String complaintId, String agentId) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(complaintId);
        if (optionalComplaint.isEmpty()) {
            throw new RuntimeException("Complaint not found with id: " + complaintId);
        }

        Optional<Agent> optionalAgent = agentRepository.findById(agentId);
        if (optionalAgent.isEmpty()) {
            throw new RuntimeException("Agent not found with id: " + agentId);
        }

        Agent agent = optionalAgent.get();
        agent.setAssignedComplaint(agent.getAssignedComplaint() + 1);
        agentRepository.save(agent);

        Complaint complaint = optionalComplaint.get();
        complaint.setAgent(agent);
        complaint.setAssignedAt(LocalDateTime.now());
        complaint.setStatus(Status.AGENT_ASSIGNED);

        complaint = complaintRepository.save(complaint);
        populateCrowdfundingFields(complaint);
        return complaint;
    }

    @Override
    public Complaint discardComplaint(String complaintId) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(complaintId);
        if (optionalComplaint.isEmpty()) {
            throw new RuntimeException("Complaint not found with id: " + complaintId);
        }

        Complaint complaint = optionalComplaint.get();
        complaint.setStatus(Status.DISCARDED);
        complaint.setCompletedAt(LocalDateTime.now());

        complaint = complaintRepository.save(complaint);
        populateCrowdfundingFields(complaint);
        return complaint;
    }

    @Override
    public Complaint startProgress(String complaintId) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(complaintId);
        if (optionalComplaint.isEmpty()) {
            throw new RuntimeException("Complaint not found with id: " + complaintId);
        }

        Complaint complaint = optionalComplaint.get();
        
        // Check if complaint is in AGENT_ASSIGNED status
        if (complaint.getStatus() != Status.AGENT_ASSIGNED) {
            throw new RuntimeException("Complaint must be in AGENT_ASSIGNED status to start progress. Current status: " + complaint.getStatus());
        }

        // Check if agent is assigned
        if (complaint.getAgent() == null) {
            throw new RuntimeException("No agent assigned to this complaint");
        }

        // Update complaint status to IN_PROGRESS
        complaint.setStatus(Status.IN_PROGRESS);
        complaint = complaintRepository.save(complaint);

        // Update agent's complaintsInProgress count
        Agent agent = complaint.getAgent();
        agent.setComplaintsInProgress(agent.getComplaintsInProgress() + 1);
        agent.setAssignedComplaint(agent.getAssignedComplaint()-1);
        agentRepository.save(agent);

        populateCrowdfundingFields(complaint);
        return complaint;
    }

    @Override
    public Complaint closeComplaint(String complaintId, CloseComplaintRequest closeComplaintRequest) {
        Optional<Complaint> optionalComplaint = complaintRepository.findById(complaintId);
        if (optionalComplaint.isEmpty()) {
            throw new RuntimeException("Complaint not found with id: " + complaintId);
        }
        Complaint complaint = optionalComplaint.get();

        // Check if complaint is in AGENT_ASSIGNED status
        if (complaint.getStatus() != Status.IN_PROGRESS) {
            throw new RuntimeException("Complaint must be in IN_PROGRESS status to mark complete. Current status: " + complaint.getStatus());
        }

        // Check if agent is assigned
        if (complaint.getAgent() == null) {
            throw new RuntimeException("No agent assigned to this complaint");
        }

        // Update complaint
        if (closeComplaintRequest.getAfterPhotoUrl() != null) {
            complaint.setAfterPhoto(closeComplaintRequest.getAfterPhotoUrl());
        }
        complaint.setStatus(Status.FIXED);
        complaint.setCompletedAt(LocalDateTime.now());
        complaint = complaintRepository.save(complaint);

        // Update agent's counts
        Agent agent = complaint.getAgent();
        agent.setClosedComplaints(agent.getClosedComplaints() + 1);
        agent.setComplaintsInProgress(agent.getComplaintsInProgress()-1);
        
        agentRepository.save(agent);

        populateCrowdfundingFields(complaint);
        return complaint;
    }

    @Override
    public void deleteComplaint(String id) {
        if (!complaintRepository.existsById(id)) {
            throw new RuntimeException("Complaint not found with id: " + id);
        }
        complaintRepository.deleteById(id);
    }

    // Helper method to populate crowdfunding fields in Complaint
    private void populateCrowdfundingFields(Complaint complaint) {
        complaint.setFundCollected(contributionService.calculateFundCollected(complaint.getId()));
        complaint.setContributors(contributionService.getContributorsForComplaint(complaint.getId()));
    }
}

