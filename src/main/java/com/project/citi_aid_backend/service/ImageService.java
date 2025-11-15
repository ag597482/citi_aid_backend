package com.project.citi_aid_backend.service;

import com.project.citi_aid_backend.dto.response.ImageInfoResponse;
import com.project.citi_aid_backend.dto.response.ImageUploadResponse;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageService {
    ImageUploadResponse uploadImage(MultipartFile file);
    Resource getImageById(String id);
    ImageInfoResponse getImageInfoById(String id);
    List<ImageInfoResponse> getAllImages();
}

