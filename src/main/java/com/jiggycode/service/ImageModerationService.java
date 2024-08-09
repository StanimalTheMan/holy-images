package com.jiggycode.service;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.AmazonRekognitionClientBuilder;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.ModerationLabel;
import com.amazonaws.services.rekognition.model.S3Object;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageModerationService {
    private final AmazonRekognition rekognitionClient;

    public ImageModerationService() {
        BasicAWSCredentials awsCreds = new BasicAWSCredentials("YOUR_ACCESS_KEY", "YOUR_SECRET_KEY");
        this.rekognitionClient = AmazonRekognitionClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

    public boolean isExplicit(String bucketName, String key) {
        DetectModerationLabelsRequest request = new DetectModerationLabelsRequest()
                .withImage(new Image().withS3Object(new S3Object().withBucket(bucketName).withName(key)));

        DetectModerationLabelsResult result = rekognitionClient.detectModerationLabels(request);
        List<ModerationLabel> labels = result.getModerationLabels();

        for (ModerationLabel label : labels) {
            if (label.getName().equalsIgnoreCase("Explicit Nudity") || label.getName().equalsIgnoreCase("Adult Content")) {
                return true;
            }
        }
        return false;
    }
}
