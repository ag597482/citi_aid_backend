package com.project.citi_aid_backend.controller;

import com.project.citi_aid_backend.dto.request.CreateComplaintRequest;
import com.project.citi_aid_backend.dto.request.UpdateComplaintRequest;
import com.project.citi_aid_backend.dto.response.ComplaintsSummary;
import com.project.citi_aid_backend.dto.response.DeleteResponse;
import com.project.citi_aid_backend.model.Complaint;
import com.project.citi_aid_backend.service.ComplaintService;
import com.project.citi_aid_backend.enums.Department;
import com.project.citi_aid_backend.enums.Severity;
import com.project.citi_aid_backend.enums.Status;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {

    @Autowired
    private ComplaintService complaintService;

    // Create
    @PostMapping
    public ResponseEntity<Complaint> createComplaint(@Valid @RequestBody CreateComplaintRequest createComplaintRequest) {
        Complaint complaint = complaintService.createComplaint(createComplaintRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(complaint);
    }

    // Read - Get by ID
    @GetMapping("/{id}")
    public ResponseEntity<Complaint> getComplaintById(@PathVariable String id) {
        Optional<Complaint> complaint = complaintService.getComplaintById(id);
        return complaint.map(c -> ResponseEntity.ok(c))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Read - Get all
    @GetMapping
    public ResponseEntity<List<Complaint>> getAllComplaints() {
        List<Complaint> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }

    @GetMapping("/summary")
    public ResponseEntity<ComplaintsSummary> getComplaintsSummary() {
        ComplaintsSummary summary = complaintService.getComplaintsSummary();
        return ResponseEntity.ok(summary);
    }

    // Read - Get by department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Complaint>> getComplaintsByDepartment(@PathVariable Department department) {
        List<Complaint> complaints = complaintService.getComplaintsByDepartment(department);
        return ResponseEntity.ok(complaints);
    }

    // Read - Get by severity
    @GetMapping("/severity/{severity}")
    public ResponseEntity<List<Complaint>> getComplaintsBySeverity(@PathVariable Severity severity) {
        List<Complaint> complaints = complaintService.getComplaintsBySeverity(severity);
        return ResponseEntity.ok(complaints);
    }

    // Read - Get by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Complaint>> getComplaintsByStatus(@PathVariable Status status) {
        List<Complaint> complaints = complaintService.getComplaintsByStatus(status);
        return ResponseEntity.ok(complaints);
    }

    // Read - Get by agent ID
    @GetMapping("/agent/{agentId}")
    public ResponseEntity<List<Complaint>> getComplaintsByAgentId(@PathVariable String agentId) {
        List<Complaint> complaints = complaintService.getComplaintsByAgentId(agentId);
        return ResponseEntity.ok(complaints);
    }

    // Read - Get by customer ID
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<Complaint>> getComplaintsByCustomerId(@PathVariable String customerId) {
        List<Complaint> complaints = complaintService.getComplaintsByCustomerId(customerId);
        return ResponseEntity.ok(complaints);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<Complaint> updateComplaint(
            @PathVariable String id,
            @Valid @RequestBody UpdateComplaintRequest updateComplaintRequest) {
        try {
            Complaint complaint = complaintService.updateComplaint(id, updateComplaintRequest);
            return ResponseEntity.ok(complaint);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> deleteComplaint(@PathVariable String id) {
        try {
            complaintService.deleteComplaint(id);
            DeleteResponse response = new DeleteResponse("Complaint deleted successfully", true);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            DeleteResponse response = new DeleteResponse("Complaint not found with id: " + id, false);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}

