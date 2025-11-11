package com.project.citi_aid_backend.controller;

import com.project.citi_aid_backend.dto.request.CreateAdminRequest;
import com.project.citi_aid_backend.model.Admin;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {


    @PostMapping("/createAdmin")
    public ResponseEntity<Admin> createAdmin(@RequestBody CreateAdminRequest createAdminRequest) {
        return ResponseEntity.status(200).body(new Admin("1", "132", "321"));
    }


}
