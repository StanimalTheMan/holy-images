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
                        .text("You are an assistant that analyzes images.")
                        .text("If the image contains one or more human faces, describe the emotions or expressions shown.")
                        .text("If there are no faces, provide a general description of what is in the image.")
                                .text("Describe facial features")
//                        .text("Always return a short summary in the `description` field.")
                )
                .user(userMessage -> userMessage
                        .text("Analyze this image and provide a description.  Describe emotions or expressions of person if face is available.")
                        .media(MimeTypeUtils.parseMimeType(contentType), new InputStreamResource(imageInputStream))
                )
                .call()
                .entity(ImageAnalysis.class);
    }
}
