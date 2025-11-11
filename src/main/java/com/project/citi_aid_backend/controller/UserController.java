package com.project.citi_aid_backend.controller;

import com.project.citi_aid_backend.dto.request.CreateAdminRequest;
import com.project.citi_aid_backend.model.Admin;
import com.project.citi_aid_backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/createAdmin")
    public ResponseEntity<Admin> createAdmin(@RequestBody CreateAdminRequest createAdminRequest) {
        Admin admin = userService.createAdmin(createAdminRequest);
        return ResponseEntity.status(200).body(admin);
    }

    @GetMapping("/admin/{name}")
    public ResponseEntity<Admin> getAdminByName(@PathVariable String name) {
        Optional<Admin> admin = userService.getAdminByName(name);
        return admin.map(a -> ResponseEntity.ok(a))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping("/admins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admins = userService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

}
