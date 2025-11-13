package com.project.citi_aid_backend.service;

import com.project.citi_aid_backend.dto.request.CustomerSignupRequest;
import com.project.citi_aid_backend.dto.request.LoginRequest;
import com.project.citi_aid_backend.dto.response.LoginResponse;
import com.project.citi_aid_backend.dto.response.SignupResponse;

public interface AuthService {
    LoginResponse loginCustomer(LoginRequest loginRequest);
    LoginResponse loginAdmin(LoginRequest loginRequest);
    LoginResponse loginAgent(LoginRequest loginRequest);
    SignupResponse signupCustomer(CustomerSignupRequest signupRequest);
}

