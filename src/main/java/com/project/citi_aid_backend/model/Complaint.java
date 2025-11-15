package com.project.citi_aid_backend.model;

import com.project.citi_aid_backend.enums.Department;
import com.project.citi_aid_backend.enums.Severity;
import com.project.citi_aid_backend.enums.Status;
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
@Document(collection = "complaints")
public class Complaint {

    @Id
    private String id;
    private String title;
    private String description;
    private String location;
    private String beforePhoto; // File path or URL
    private String afterPhoto; // File path or URL
    private Department department;
    private Severity severity;
    private Status status;
    
    @DBRef
    private Agent agent;
    
    @DBRef
    private Customer customer;
    
    private LocalDateTime createdAt;
    private LocalDateTime assignedAt;
    private LocalDateTime completedAt;
}

