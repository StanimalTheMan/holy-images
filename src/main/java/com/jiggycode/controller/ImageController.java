package com.jiggycode.controller;

import com.jiggycode.model.ImageAnalysis;
import com.jiggycode.service.ImageAnalysisService;
import com.jiggycode.service.RekognitionService;
import com.jiggycode.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private RekognitionService rekognitionService;

    @Autowired
    private ImageAnalysisService imageAnalysisService;

    private String bucketName = "images-to-moderate";
    private String directory = "images";

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/analyze")
    public ResponseEntity<?> analyzeImage(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            ImageAnalysis result = imageAnalysisService.analyzeImage(inputStream, file.getContentType());
            return ResponseEntity.ok(result);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing image");
        }
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        // Upload the file to S3
        String key = s3Service.uploadFile(file, directory);

        // Moderate the image
        var result = rekognitionService.detectModerationLabels(bucketName, key);
        System.out.println("result" + result);
        // Handle the result (e.g., store labels, delete file if inappropriate, etc.)
//        boolean isExplicit = result.getModerationLabels().stream()
//                .anyMatch(label -> label.getName().equalsIgnoreCase("Explicit Nudity"));
        boolean isUnholy = !result.getModerationLabels().isEmpty();

        if (isUnholy) {
            s3Service.deleteFile(key);
            return "File contains explicit content and was removed.";
        }

        return "File uploaded and moderated successfully.";
    }
}
