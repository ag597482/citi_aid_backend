package com.project.citi_aid_backend.service.impl;

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
        return complaintRepository.save(complaint);
    }

    @Override
    public Optional<Complaint> getComplaintById(String id) {
        return complaintRepository.findById(id);
    }

    @Override
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
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
        return complaintRepository.findByDepartment(department);
    }

    @Override
    public List<Complaint> getComplaintsBySeverity(Severity severity) {
        return complaintRepository.findBySeverity(severity);
    }

    @Override
    public List<Complaint> getComplaintsByStatus(Status status) {
        return complaintRepository.findByStatus(status);
    }

    @Override
    public List<Complaint> getComplaintsByAgentId(String agentId) {
        return complaintRepository.findByAgentId(agentId);
    }

    @Override
    public List<Complaint> getComplaintsByCustomerId(String customerId) {
        return complaintRepository.findByCustomerId(customerId);
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

        return complaintRepository.save(complaint);
    }

    @Override
    public void deleteComplaint(String id) {
        if (!complaintRepository.existsById(id)) {
            throw new RuntimeException("Complaint not found with id: " + id);
        }
        complaintRepository.deleteById(id);
    }
}

