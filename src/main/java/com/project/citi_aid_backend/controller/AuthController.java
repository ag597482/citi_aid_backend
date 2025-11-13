package com.project.citi_aid_backend.controller;

import com.project.citi_aid_backend.dto.request.CustomerSignupRequest;
import com.project.citi_aid_backend.dto.request.LoginRequest;
import com.project.citi_aid_backend.dto.response.LoginResponse;
import com.project.citi_aid_backend.dto.response.SignupResponse;
import com.project.citi_aid_backend.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/customer/login")
    public ResponseEntity<LoginResponse> loginCustomer(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.loginCustomer(loginRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/admin/login")
    public ResponseEntity<LoginResponse> loginAdmin(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.loginAdmin(loginRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/agent/login")
    public ResponseEntity<LoginResponse> loginAgent(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.loginAgent(loginRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/customer/signup")
    public ResponseEntity<SignupResponse> signupCustomer(@Valid @RequestBody CustomerSignupRequest signupRequest) {
        SignupResponse response = authService.signupCustomer(signupRequest);
        
        if (response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }
}

