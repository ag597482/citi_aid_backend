package com.project.citi_aid_backend.service;

import com.project.citi_aid_backend.dto.request.CreateAdminRequest;
import com.project.citi_aid_backend.model.Admin;

import java.util.List;
import java.util.Optional;

public interface UserService {
    Admin createAdmin(CreateAdminRequest createAdminRequest);
    Optional<Admin> getAdminByName(String name);
    List<Admin> getAllAdmins();
}
