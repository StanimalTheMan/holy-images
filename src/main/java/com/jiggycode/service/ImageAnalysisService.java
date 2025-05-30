package com.jiggycode.service;

import com.jiggycode.model.ImageAnalysis;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

import java.io.InputStream;

@Service
public class ImageAnalysisService {
    private final ChatClient chatClient;

    public ImageAnalysisService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public ImageAnalysis analyzeImage(InputStream imageInputStream, String contentType) {
        return chatClient.prompt()
                .system(systemMessage -> systemMessage
                        .text("You are an assistant that analyzes images. Given an image, extract the following:")
                        .text("1. A short overall description of the image.")
                        .text("2. If faces are detected, describe for each: facial expressions, age group, and gender (if apparent).")
                        .text("3. If no faces, describe the setting, objects, or scene.")
                        .text("Respond in JSON format like this:")
                        .text("{")
                        .text("  \"description\": \"A woman smiling in a park.\",")
                        .text("  \"faces\": [")
                        .text("    { \"expression\": \"smiling\", \"ageGroup\": \"adult\", \"gender\": \"female\" }")
                        .text("  ]")
                        .text("}")
                )
                .user(userMessage -> userMessage
                        .text("Analyze this image and return a detailed description in the format above.")
                        .media(MimeTypeUtils.parseMimeType(contentType), new InputStreamResource(imageInputStream))
                )
                .call()
                .entity(ImageAnalysis.class); // You’ll need to update this class to support the new fields
    }

}
