package com.project.citi_aid_backend.model;

import com.project.citi_aid_backend.enums.Department;
import com.project.citi_aid_backend.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "agents")
public class Agent extends User {

    @Id
    private String id;
    private String name;
    private String phone;
    private String password;
    private String document; // File path or URL
    private Department department;

    @Override
    public UserType getUserType() {
        return UserType.AGENT;
    }
}
