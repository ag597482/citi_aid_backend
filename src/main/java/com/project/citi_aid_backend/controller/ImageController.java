package com.project.citi_aid_backend.controller;

import com.project.citi_aid_backend.dto.response.ImageInfoResponse;
import com.project.citi_aid_backend.dto.response.ImageUploadResponse;
import com.project.citi_aid_backend.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @PostMapping("/upload")
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            ImageUploadResponse response = imageService.uploadImage(file);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ImageUploadResponse(null, null, null, "Error: " + e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> getImageById(@PathVariable String id) {
        try {
            Resource resource = imageService.getImageById(id);
            ImageInfoResponse imageInfo = imageService.getImageInfoById(id);
            
            return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(imageInfo.getContentType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + imageInfo.getOriginalFileName() + "\"")
                .body(resource);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/{id}/info")
    public ResponseEntity<ImageInfoResponse> getImageInfoById(@PathVariable String id) {
        try {
            ImageInfoResponse response = imageService.getImageInfoById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<ImageInfoResponse>> getAllImages() {
        List<ImageInfoResponse> images = imageService.getAllImages();
        return ResponseEntity.ok(images);
    }
}

