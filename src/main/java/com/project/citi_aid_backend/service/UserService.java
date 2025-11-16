package com.project.citi_aid_backend.service;

import com.project.citi_aid_backend.dto.request.CreateAdminRequest;
import com.project.citi_aid_backend.dto.request.CreateAgentRequest;
import com.project.citi_aid_backend.dto.request.CreateCustomerRequest;
import com.project.citi_aid_backend.dto.response.CustomerProfile;
import com.project.citi_aid_backend.model.Admin;
import com.project.citi_aid_backend.model.Agent;
import com.project.citi_aid_backend.model.Customer;
import com.project.citi_aid_backend.enums.Department;

import java.util.List;
import java.util.Optional;

public interface UserService {
    // Admin methods
    Admin createAdmin(CreateAdminRequest createAdminRequest);
    Optional<Admin> getAdminByName(String name);
    List<Admin> getAllAdmins();
    
    // Customer methods
    Customer createCustomer(CreateCustomerRequest createCustomerRequest);
    Optional<Customer> getCustomerByName(String name);
    Optional<Customer> getCustomerByEmail(String email);
    List<Customer> getAllCustomers();
    CustomerProfile getCustomerProfile(String customerId);
    
    // Agent methods
    Agent createAgent(CreateAgentRequest createAgentRequest);
    Optional<Agent> getAgentByName(String name);
    Optional<Agent> getAgentByPhone(String phone);
    List<Agent> getAllAgents();
    List<Agent> getAgentsByDepartment(Department department);
}
