package com.project.citi_aid_backend.controller;

import com.project.citi_aid_backend.dto.request.CreateAdminRequest;
import com.project.citi_aid_backend.dto.request.CreateAgentRequest;
import com.project.citi_aid_backend.dto.request.CreateCustomerRequest;
import com.project.citi_aid_backend.dto.request.UpdateAgentRequest;
import com.project.citi_aid_backend.dto.response.AgentProfile;
import com.project.citi_aid_backend.dto.response.CustomerProfile;
import com.project.citi_aid_backend.dto.response.SignupResponse;
import com.project.citi_aid_backend.model.Admin;
import com.project.citi_aid_backend.model.Agent;
import com.project.citi_aid_backend.model.Customer;
import com.project.citi_aid_backend.service.UserService;
import com.project.citi_aid_backend.enums.Department;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    // Admin endpoints
    @PostMapping("/admin/create")
    public ResponseEntity<Admin> createAdmin(@Valid @RequestBody CreateAdminRequest createAdminRequest) {
        Admin admin = userService.createAdmin(createAdminRequest);
        return ResponseEntity.status(200).body(admin);
    }

    @GetMapping("/admin/{name}")
    public ResponseEntity<Admin> getAdminByName(@PathVariable String name) {
        Optional<Admin> admin = userService.getAdminByName(name);
        return admin.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/admins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = userService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    // Customer endpoints
    @PostMapping("/customer/create")
    public ResponseEntity<Customer> createCustomer(@Valid @RequestBody CreateCustomerRequest createCustomerRequest) {
        Customer customer = userService.createCustomer(createCustomerRequest);
        return ResponseEntity.status(200).body(customer);
    }

    @GetMapping("/customer/name/{name}")
    public ResponseEntity<Customer> getCustomerByName(@PathVariable String name) {
        Optional<Customer> customer = userService.getCustomerByName(name);
        return customer.map(c -> ResponseEntity.ok(c))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/customer/profile/{customerId}")
    public ResponseEntity<CustomerProfile> getCustomerProfile(@PathVariable String customerId) {
        CustomerProfile customerProfile = userService.getCustomerProfile(customerId);
        return ResponseEntity.status(200).body(customerProfile);
    }

    @GetMapping("/customer/email/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email) {
        Optional<Customer> customer = userService.getCustomerByEmail(email);
        return customer.map(c -> ResponseEntity.ok(c))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/customers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        List<Customer> customers = userService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    // Agent endpoints
    @PostMapping("/agent/create")
    public ResponseEntity<SignupResponse> createAgent(@Valid @RequestBody CreateAgentRequest createAgentRequest) {
        SignupResponse response = userService.createAgent(createAgentRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @GetMapping("/agent/name/{name}")
    public ResponseEntity<Agent> getAgentByName(@PathVariable String name) {
        Optional<Agent> agent = userService.getAgentByName(name);
        return agent.map(a -> ResponseEntity.ok(a))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/agent/phone/{phone}")
    public ResponseEntity<Agent> getAgentByPhone(@PathVariable String phone) {
        Optional<Agent> agent = userService.getAgentByPhone(phone);
        return agent.map(a -> ResponseEntity.ok(a))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/agents")
    public ResponseEntity<List<Agent>> getAllAgents() {
        List<Agent> agents = userService.getAllAgents();
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/agents/department/{department}")
    public ResponseEntity<List<Agent>> getAgentsByDepartment(@PathVariable Department department) {
        List<Agent> agents = userService.getAgentsByDepartment(department);
        return ResponseEntity.ok(agents);
    }

    @GetMapping("/agent/profile/{agentId}")
    public ResponseEntity<AgentProfile> getAgentProfile(@PathVariable String agentId) {
        AgentProfile agentProfile = userService.getAgentProfile(agentId);
        return ResponseEntity.status(200).body(agentProfile);
    }

    @PutMapping("/agent/update/{agentId}")
    public ResponseEntity<Agent> updateAgent(@PathVariable String agentId, @Valid @RequestBody UpdateAgentRequest updateAgentRequest) {
        Agent updatedAgent = userService.updateAgent(agentId, updateAgentRequest);
        return ResponseEntity.status(200).body(updatedAgent);
    }

}
