package com.project.citi_aid_backend.service.impl;

import com.project.citi_aid_backend.dto.request.CreateAdminRequest;
import com.project.citi_aid_backend.model.Admin;
import com.project.citi_aid_backend.repository.AdminRepository;
import com.project.citi_aid_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin createAdmin(CreateAdminRequest createAdminRequest) {
        Admin admin = new Admin();
        admin.setName(createAdminRequest.getName());
        admin.setPassword(createAdminRequest.getPassword());
        return adminRepository.save(admin);
    }

    @Override
    public Optional<Admin> getAdminByName(String name) {
        return adminRepository.findByName(name);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
}

