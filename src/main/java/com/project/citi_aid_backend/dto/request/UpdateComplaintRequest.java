package com.project.citi_aid_backend.dto.request;

import com.project.citi_aid_backend.enums.Department;
import com.project.citi_aid_backend.enums.Severity;
import com.project.citi_aid_backend.enums.Status;
import lombok.Data;

@Data
public class UpdateComplaintRequest {
    private String title;
    private String description;
    private String location;
    private String beforePhoto;
    private String afterPhoto;
    private Department department;
    private Severity severity;
    private Status status;
    private String agentId; // ID of the agent to assign
    private Boolean crowdFundingEnabled;
    private Integer targetFund;
}

