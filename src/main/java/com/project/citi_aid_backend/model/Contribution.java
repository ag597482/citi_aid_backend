package com.project.citi_aid_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "contributions")
public class Contribution {

    @Id
    private String id;
    
    @DBRef
    private Customer contributor;
    
    @DBRef
    private Complaint complaint;
    
    private int amount;
    private LocalDateTime createdAt;
}
