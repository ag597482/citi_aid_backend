package com.project.citi_aid_backend.dto.response;

import com.project.citi_aid_backend.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private String id;
    private String name;
    private UserType userType;
    private String message;
    private boolean success;
}

