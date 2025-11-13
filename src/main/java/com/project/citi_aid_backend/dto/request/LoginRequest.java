package com.project.citi_aid_backend.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email/Name/Phone is required")
    private String identifier; // Can be email for customer, name for admin/agent, or phone for agent

    @NotBlank(message = "Password is required")
    private String password;
}

