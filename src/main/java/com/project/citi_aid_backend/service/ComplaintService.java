package com.project.citi_aid_backend.service;

import com.project.citi_aid_backend.dto.request.CreateComplaintRequest;
import com.project.citi_aid_backend.dto.request.UpdateComplaintRequest;
import com.project.citi_aid_backend.dto.response.ComplaintsSummary;
import com.project.citi_aid_backend.model.Complaint;
import com.project.citi_aid_backend.enums.Department;
import com.project.citi_aid_backend.enums.Severity;
import com.project.citi_aid_backend.enums.Status;

import java.util.List;
import java.util.Optional;

public interface ComplaintService {
    // Create
    Complaint createComplaint(CreateComplaintRequest createComplaintRequest);
    
    // Read
    Optional<Complaint> getComplaintById(String id);
    List<Complaint> getAllComplaints();
    ComplaintsSummary getComplaintsSummary();
    List<Complaint> getComplaintsByDepartment(Department department);
    List<Complaint> getComplaintsBySeverity(Severity severity);
    List<Complaint> getComplaintsByStatus(Status status);
    List<Complaint> getComplaintsByAgentId(String agentId);
    List<Complaint> getComplaintsByCustomerId(String customerId);
    
    // Update
    Complaint updateComplaint(String id, UpdateComplaintRequest updateComplaintRequest);
    Complaint assignAgentToComplaint(String complaintId, String agentId);
    Complaint discardComplaint(String complaintId);
    
    // Delete
    void deleteComplaint(String id);
}

