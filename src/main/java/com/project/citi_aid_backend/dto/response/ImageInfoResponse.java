package com.project.citi_aid_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageInfoResponse {
    private String id;
    private String fileName;
    private String originalFileName;
    private String contentType;
    private long fileSize;
    private String url;
    private LocalDateTime uploadedAt;
}

