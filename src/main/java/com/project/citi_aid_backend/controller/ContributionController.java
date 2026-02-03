package com.project.citi_aid_backend.controller;

import com.project.citi_aid_backend.dto.request.ContributeRequest;
import com.project.citi_aid_backend.model.Contribution;
import com.project.citi_aid_backend.service.ContributionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contribute")
public class ContributionController {

    @Autowired
    private ContributionService contributionService;

    @PostMapping("/{customerID}/{complaintID}")
    public ResponseEntity<Contribution> contribute(
            @PathVariable String customerID,
            @PathVariable String complaintID,
            @Valid @RequestBody ContributeRequest contributeRequest) {
        try {
            Contribution contribution = contributionService.addContribution(
                    customerID, complaintID, contributeRequest.getAmount());
            return ResponseEntity.status(HttpStatus.CREATED).body(contribution);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
