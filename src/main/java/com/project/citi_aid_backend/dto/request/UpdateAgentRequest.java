package com.project.citi_aid_backend.dto.request;

import lombok.Data;

@Data
public class UpdateAgentRequest {
    private String password;
    private String profilePhotoUrl;
}

