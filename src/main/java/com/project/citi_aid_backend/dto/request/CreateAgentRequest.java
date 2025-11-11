package com.project.citi_aid_backend.dto.request;

import com.project.citi_aid_backend.enums.Department;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateAgentRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    private String document; // File path or URL

    @NotNull(message = "Department is required")
    private Department department;
}

