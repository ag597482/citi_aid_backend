package com.project.citi_aid_backend.dto.request;

import lombok.Data;

@Data
public class CreateAdminRequest {

    private String name;
    private String password;
}
