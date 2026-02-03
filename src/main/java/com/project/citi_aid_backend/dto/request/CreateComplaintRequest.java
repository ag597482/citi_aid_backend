package com.project.citi_aid_backend.dto.request;

import com.project.citi_aid_backend.enums.Department;
import com.project.citi_aid_backend.enums.Severity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateComplaintRequest {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Location is required")
    private String location;

    private String beforePhoto; // File path or URL

    @NotNull(message = "Department is required")
    private Department department;

    @NotNull(message = "Severity is required")
    private Severity severity;

    @NotBlank(message = "Customer ID is required")
    private String customerId;

    private Boolean crowdFundingEnabled;
    private Integer targetFund;
}

