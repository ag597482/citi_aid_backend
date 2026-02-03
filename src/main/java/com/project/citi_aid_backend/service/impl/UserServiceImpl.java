package com.project.citi_aid_backend.service.impl;

import com.project.citi_aid_backend.dto.request.CreateAdminRequest;
import com.project.citi_aid_backend.dto.request.CreateAgentRequest;
import com.project.citi_aid_backend.dto.request.CreateCustomerRequest;
import com.project.citi_aid_backend.dto.request.UpdateAgentRequest;
import com.project.citi_aid_backend.dto.response.AgentProfile;
import com.project.citi_aid_backend.dto.response.CustomerProfile;
import com.project.citi_aid_backend.dto.response.SignupResponse;
import com.project.citi_aid_backend.enums.Status;
import com.project.citi_aid_backend.model.*;
import com.project.citi_aid_backend.repository.AdminRepository;
import com.project.citi_aid_backend.repository.AgentRepository;
import com.project.citi_aid_backend.repository.CustomerRepository;
import com.project.citi_aid_backend.service.ComplaintService;
import com.project.citi_aid_backend.service.ContributionService;
import com.project.citi_aid_backend.service.UserService;
import com.project.citi_aid_backend.enums.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private ComplaintService complaintService;

    @Autowired
    private ContributionService contributionService;

    // Admin methods
    @Override
    public Admin createAdmin(CreateAdminRequest createAdminRequest) {
        Admin admin = new Admin();
        admin.setName(createAdminRequest.getName());
        admin.setPassword(createAdminRequest.getPassword());
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> getAdminByName(String name) {
        return adminRepository.findByName(name);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // Customer methods
    @Override
    public Customer createCustomer(CreateCustomerRequest createCustomerRequest) {
        Customer customer = new Customer();
        customer.setName(createCustomerRequest.getName());
        customer.setEmail(createCustomerRequest.getEmail());
        customer.setPhone(createCustomerRequest.getPhone());
        customer.setPassword(createCustomerRequest.getPassword());
        return customerRepository.save(customer);
    }

    @Override
    public Optional<Customer> getCustomerByName(String name) {
        return customerRepository.findByName(name);
    }

    @Override
    public Optional<Customer> getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public CustomerProfile getCustomerProfile(String customerId) {
        Customer customer = customerRepository.findById(customerId).get();
        List<Complaint> customerComplaints = complaintService.getComplaintsByCustomerId(customerId);
        List<Contribution> contributions = contributionService.getContributionsByContributorId(customerId);
        CustomerProfile customerProfile = new CustomerProfile();
        customerProfile.setCustomer(customer);
        customerProfile.setActiveComplaints(customerComplaints.stream().filter(complaint -> !complaint.getStatus().equals(Status.FIXED)).toList());
        customerProfile.setClosedComplaints(customerComplaints.stream().filter(complaint -> complaint.getStatus().equals(Status.FIXED)).toList());
        customerProfile.setContributions(contributions);
        return customerProfile;
    }

    // Agent methods
    @Override
    public SignupResponse createAgent(CreateAgentRequest createAgentRequest) {
        // Check if agent with same phone number already exists
        Optional<Agent> existingAgent = agentRepository.findByPhone(createAgentRequest.getPhone());
        if (existingAgent.isPresent()) {
            return new SignupResponse(null, null, null, 
                "Agent with phone number " + createAgentRequest.getPhone() + " already exists", false);
        }

        Agent agent = new Agent();
        agent.setName(createAgentRequest.getName());
        agent.setPhone(createAgentRequest.getPhone());
        agent.setPassword(createAgentRequest.getPassword());
        agent.setDocument(createAgentRequest.getDocument());
        agent.setDepartment(createAgentRequest.getDepartment());
        Agent savedAgent = agentRepository.save(agent);

        return new SignupResponse(savedAgent.getId(), savedAgent.getName(), 
            null, "Agent created successfully", true);
    }

    @Override
    public Optional<Agent> getAgentByName(String name) {
        return agentRepository.findByName(name);
    }

    @Override
    public Optional<Agent> getAgentByPhone(String phone) {
        return agentRepository.findByPhone(phone);
    }

    @Override
    public List<Agent> getAllAgents() {
        return agentRepository.findAll();
    }

    @Override
    public List<Agent> getAgentsByDepartment(Department department) {
        return agentRepository.findByDepartment(department);
    }

    @Override
    public AgentProfile getAgentProfile(String agentId) {
        Agent agent = agentRepository.findById(agentId).get();
        List<Complaint> agentComplaints = complaintService.getComplaintsByAgentId(agentId);
        AgentProfile agentProfile = new AgentProfile();
        agentProfile.setAgent(agent);
        agentProfile.setActiveComplaints(agentComplaints.stream().filter(complaint -> !complaint.getStatus().equals(Status.FIXED)).toList());
        agentProfile.setClosedComplaints(agentComplaints.stream().filter(complaint -> complaint.getStatus().equals(Status.FIXED)).toList());
        return agentProfile;
    }

    @Override
    public Agent updateAgent(String agentId, UpdateAgentRequest updateAgentRequest) {
        Agent agent = agentRepository.findById(agentId)
                .orElseThrow(() -> new RuntimeException("Agent not found with id: " + agentId));
        
        if (updateAgentRequest.getPassword() != null && !updateAgentRequest.getPassword().isEmpty()) {
            agent.setPassword(updateAgentRequest.getPassword());
        }
        
        if (updateAgentRequest.getProfilePhotoUrl() != null) {
            agent.setProfilePhotoUrl(updateAgentRequest.getProfilePhotoUrl());
        }
        
        return agentRepository.save(agent);
    }
}

