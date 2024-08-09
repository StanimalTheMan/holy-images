package com.jiggycode.controller;

import com.jiggycode.service.RekognitionService;
import com.jiggycode.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class ImageController {

    @Autowired
    private S3Service s3Service;

    @Autowired
    private RekognitionService rekognitionService;

    private String bucketName = "images-to-moderate";
    private String directory = "images";

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
