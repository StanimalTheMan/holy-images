package com.jiggycode.service;

import com.amazonaws.services.rekognition.AmazonRekognition;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsRequest;
import com.amazonaws.services.rekognition.model.DetectModerationLabelsResult;
import com.amazonaws.services.rekognition.model.Image;
import com.amazonaws.services.rekognition.model.S3Object;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class              RekognitionService {

    @Autowired
    private AmazonRekognition amazonRekognition;

    public DetectModerationLabelsResult detectModerationLabels(String bucketName, String key) {
        DetectModerationLabelsRequest request = new DetectModerationLabelsRequest()
                .withImage(new Image()
                        .withS3Object(new S3Object()
                                .withBucket(bucketName)
                                .withName(key)));
        return amazonRekognition.detectModerationLabels(request);
    }
}
