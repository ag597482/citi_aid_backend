package com.project.citi_aid_backend.model;

import com.project.citi_aid_backend.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "admins")
public class Admin extends User{

    @Id
    private String id;
    private String name;
    private String password;

    @Override
    public UserType getUserType() {
        return UserType.ADMIN;
    }
}
