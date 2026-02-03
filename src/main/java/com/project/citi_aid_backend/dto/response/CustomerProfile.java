package com.project.citi_aid_backend.dto.response;

import com.project.citi_aid_backend.model.Complaint;
import com.project.citi_aid_backend.model.Contribution;
import com.project.citi_aid_backend.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerProfile {

    Customer customer;
    List<Complaint> activeComplaints;
    List<Complaint> closedComplaints;
    List<Contribution> contributions;
}
