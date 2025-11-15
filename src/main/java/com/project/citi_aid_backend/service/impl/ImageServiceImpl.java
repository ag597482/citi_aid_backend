package com.project.citi_aid_backend.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.citi_aid_backend.dto.response.ImageInfoResponse;
import com.project.citi_aid_backend.dto.response.ImageUploadResponse;
import com.project.citi_aid_backend.model.Image;
import com.project.citi_aid_backend.service.ImageService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ImageServiceImpl implements ImageService {

    @Value("${app.image.storage.path:target/images}")
    private String imageStoragePath;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024; // 10MB in bytes
    private static final String[] ALLOWED_CONTENT_TYPES = {
        "image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp", "image/bmp"
    };
    private final ObjectMapper objectMapper;
    private String metadataFilePath;

    {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public ImageServiceImpl() {
        // Constructor initialization will be done after @Value injection
    }

    @PostConstruct
    private void initialize() {
        // Set metadata file path based on storage path
        metadataFilePath = imageStoragePath + "/metadata.json";
        
        // Create directory if it doesn't exist
        try {
            Path imageDir = Paths.get(imageStoragePath);
            if (!Files.exists(imageDir)) {
                Files.createDirectories(imageDir);
            }
            // Initialize metadata file if it doesn't exist
            Path metadataPath = Paths.get(metadataFilePath);
            if (!Files.exists(metadataPath)) {
                Files.createFile(metadataPath);
                objectMapper.writeValue(metadataPath.toFile(), new ArrayList<Image>());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize image storage directory", e);
        }
    }

    @Override
    public ImageUploadResponse uploadImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        // Check file size
        if (file.getSize() > MAX_FILE_SIZE) {
            throw new RuntimeException("File size exceeds maximum allowed size of " + (MAX_FILE_SIZE / (1024 * 1024)) + "MB");
        }

        // Check content type
        String contentType = file.getContentType();
        if (contentType == null || !isAllowedContentType(contentType)) {
            throw new RuntimeException("Invalid file type. Only image files (JPEG, PNG, GIF, WEBP, BMP) are allowed");
        }

        try {
            // Generate unique ID
            String id = UUID.randomUUID().toString();
            
            // Get original filename and extension
            String originalFileName = file.getOriginalFilename();
            String extension = "";
            if (originalFileName != null && originalFileName.contains(".")) {
                extension = originalFileName.substring(originalFileName.lastIndexOf("."));
            }
            
            // Create unique filename
            String fileName = id + extension;
            
            // Save file
            Path targetPath = Paths.get(imageStoragePath, fileName);
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            
            // Create image metadata
            Image image = new Image();
            image.setId(id);
            image.setFileName(fileName);
            image.setOriginalFileName(originalFileName);
            image.setContentType(file.getContentType());
            image.setFileSize(file.getSize());
            image.setUrl("/api/images/" + id);
            image.setUploadedAt(LocalDateTime.now());
            
            // Save metadata
            saveImageMetadata(image);
            
            return new ImageUploadResponse(
                id,
                fileName,
                image.getUrl(),
                "Image uploaded successfully"
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image: " + e.getMessage(), e);
        }
    }

    @Override
    public Resource getImageById(String id) {
        try {
            Image image = getImageMetadataById(id);
            if (image == null) {
                throw new RuntimeException("Image not found with id: " + id);
            }
            
            Path imagePath = Paths.get(imageStoragePath, image.getFileName());
            File file = imagePath.toFile();
            
            if (!file.exists()) {
                throw new RuntimeException("Image file not found: " + image.getFileName());
            }
            
            return new FileSystemResource(file);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve image: " + e.getMessage(), e);
        }
    }

    @Override
    public ImageInfoResponse getImageInfoById(String id) {
        Image image = getImageMetadataById(id);
        if (image == null) {
            throw new RuntimeException("Image not found with id: " + id);
        }
        
        return new ImageInfoResponse(
            image.getId(),
            image.getFileName(),
            image.getOriginalFileName(),
            image.getContentType(),
            image.getFileSize(),
            image.getUrl(),
            image.getUploadedAt()
        );
    }

    @Override
    public List<ImageInfoResponse> getAllImages() {
        List<Image> images = loadAllImageMetadata();
        return images.stream()
            .map(image -> new ImageInfoResponse(
                image.getId(),
                image.getFileName(),
                image.getOriginalFileName(),
                image.getContentType(),
                image.getFileSize(),
                image.getUrl(),
                image.getUploadedAt()
            ))
            .collect(Collectors.toList());
    }

    private void saveImageMetadata(Image image) {
        try {
            List<Image> images = loadAllImageMetadata();
            images.add(image);
            objectMapper.writeValue(Paths.get(metadataFilePath).toFile(), images);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image metadata", e);
        }
    }

    private List<Image> loadAllImageMetadata() {
        try {
            Path metadataPath = Paths.get(metadataFilePath);
            if (!Files.exists(metadataPath) || Files.size(metadataPath) == 0) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(
                metadataPath.toFile(),
                new TypeReference<List<Image>>() {}
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to load image metadata", e);
        }
    }

    private Image getImageMetadataById(String id) {
        List<Image> images = loadAllImageMetadata();
        return images.stream()
            .filter(img -> img.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    private boolean isAllowedContentType(String contentType) {
        for (String allowedType : ALLOWED_CONTENT_TYPES) {
            if (allowedType.equalsIgnoreCase(contentType)) {
                return true;
            }
        }
        return false;
    }
}

