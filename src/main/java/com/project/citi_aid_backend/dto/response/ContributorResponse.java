package com.project.citi_aid_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContributorResponse {

    private String contributorId;
    private String contributorName;
    private String contributorEmail;
    private int amount;
    private LocalDateTime contributedAt;
}
