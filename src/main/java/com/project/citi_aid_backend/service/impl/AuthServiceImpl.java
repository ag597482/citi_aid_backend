package com.project.citi_aid_backend.service.impl;

import com.project.citi_aid_backend.dto.request.CreateCustomerRequest;
import com.project.citi_aid_backend.dto.request.CustomerSignupRequest;
import com.project.citi_aid_backend.dto.request.LoginRequest;
import com.project.citi_aid_backend.dto.response.LoginResponse;
import com.project.citi_aid_backend.dto.response.SignupResponse;
import com.project.citi_aid_backend.enums.UserType;
import com.project.citi_aid_backend.model.Admin;
import com.project.citi_aid_backend.model.Agent;
import com.project.citi_aid_backend.model.Customer;
import com.project.citi_aid_backend.service.AuthService;
import com.project.citi_aid_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserService userService;

    @Override
    public LoginResponse loginCustomer(LoginRequest loginRequest) {
        Optional<Customer> customerOpt = userService.getCustomerByEmail(loginRequest.getIdentifier());
        
        if (customerOpt.isEmpty()) {
            return new LoginResponse(null, null, UserType.CUSTOMER, 
                "Customer not found with email: " + loginRequest.getIdentifier(), false);
        }

        Customer customer = customerOpt.get();
        
        if (!customer.getPassword().equals(loginRequest.getPassword())) {
            return new LoginResponse(null, null, UserType.CUSTOMER, 
                "Invalid password", false);
        }

        return new LoginResponse(customer.getId(), customer.getName(), 
            UserType.CUSTOMER, "Login successful", true);
    }

    @Override
    public LoginResponse loginAdmin(LoginRequest loginRequest) {
        Optional<Admin> adminOpt = userService.getAdminByName(loginRequest.getIdentifier());
        
        if (adminOpt.isEmpty()) {
            return new LoginResponse(null, null, UserType.ADMIN, 
                "Admin not found with name: " + loginRequest.getIdentifier(), false);
        }

        Admin admin = adminOpt.get();
        
        if (!admin.getPassword().equals(loginRequest.getPassword())) {
            return new LoginResponse(null, null, UserType.ADMIN, 
                "Invalid password", false);
        }

        return new LoginResponse(admin.getId(), admin.getName(), 
            UserType.ADMIN, "Login successful", true);
    }

    @Override
    public LoginResponse loginAgent(LoginRequest loginRequest) {
        // Try to find by name first
        Optional<Agent> agentOpt = userService.getAgentByName(loginRequest.getIdentifier());
        
        // If not found by name, try by phone
        if (agentOpt.isEmpty()) {
            agentOpt = userService.getAgentByPhone(loginRequest.getIdentifier());
        }
        
        if (agentOpt.isEmpty()) {
            return new LoginResponse(null, null, UserType.AGENT, 
                "Agent not found with name/phone: " + loginRequest.getIdentifier(), false);
        }

        Agent agent = agentOpt.get();
        
        if (!agent.getPassword().equals(loginRequest.getPassword())) {
            return new LoginResponse(null, null, UserType.AGENT, 
                "Invalid password", false);
        }

        return new LoginResponse(agent.getId(), agent.getName(), 
            UserType.AGENT, "Login successful", true);
    }

    @Override
    public SignupResponse signupCustomer(CustomerSignupRequest signupRequest) {
        // Check if customer with same email already exists
        Optional<Customer> existingCustomer = userService.getCustomerByEmail(signupRequest.getEmail());
        
        if (existingCustomer.isPresent()) {
            return new SignupResponse(null, null, null, 
                "Customer with email " + signupRequest.getEmail() + " already exists", false);
        }

        // Convert CustomerSignupRequest to CreateCustomerRequest
        CreateCustomerRequest createCustomerRequest = new CreateCustomerRequest();
        createCustomerRequest.setName(signupRequest.getName());
        createCustomerRequest.setEmail(signupRequest.getEmail());
        createCustomerRequest.setPhone(signupRequest.getPhone());
        createCustomerRequest.setPassword(signupRequest.getPassword());

        // Create new customer using UserService
        Customer savedCustomer = userService.createCustomer(createCustomerRequest);

        return new SignupResponse(savedCustomer.getId(), savedCustomer.getName(), 
            savedCustomer.getEmail(), "Customer registered successfully", true);
    }
}