package com.jiggycode.service;

import com.amazonaws.services.s3.AmazonS3;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class S3Service {

    @Autowired
    private AmazonS3 amazonS3;

    private String bucketName = "images-to-moderate";

    public String uploadFile(MultipartFile file, String directory) throws IOException {
        String fileName = file.getOriginalFilename();
        String key = directory + "/" + fileName;
        amazonS3.putObject(bucketName, key, file.getInputStream(), null);
        return key;
    }

    public void deleteFile(String key) {
        amazonS3.deleteObject(bucketName, key);
    }
}
