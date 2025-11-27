package com.project.citi_aid_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintsSummary {

    private int openComplaints;
    private int assignedComplaints;
    private int inProgressComplaints;
    private int fixedComplaints;
}
