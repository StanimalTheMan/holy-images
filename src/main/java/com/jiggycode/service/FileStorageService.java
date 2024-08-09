package com.jiggycode.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class FileStorageService {

    @Autowired
    private AmazonS3 amazonS3;

    private final String bucketName = "your-s3-bucket-name";

//    public FileStorageService() {
//        BasicAWSCredentials awsCreds = new BasicAWSCredentials("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY");
//        this.s3Client = AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.US_EAST_1)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//                .build();
//    }

    public String storeFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();
        amazonS3.putObject(bucketName, fileName, file.getInputStream(), null);
        return "File uploaded successfully";
    }

    public void deleteFile(String fileName) {
        amazonS3.deleteObject(bucketName, fileName);
    }

//    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
//        Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
//        Resource resource = new UrlResource(filePath.toUri());
//        if (resource.exists()) {
//            return resource;
//        } else {
//            throw new FileNotFoundException("File not found " + fileName);
//        }
//    }
}
