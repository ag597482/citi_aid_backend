package com.project.citi_aid_backend.dto.response;

import com.project.citi_aid_backend.model.Agent;
import com.project.citi_aid_backend.model.Complaint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgentProfile {

    Agent agent;
    List<Complaint> activeComplaints;
    List<Complaint> closedComplaints;
}

