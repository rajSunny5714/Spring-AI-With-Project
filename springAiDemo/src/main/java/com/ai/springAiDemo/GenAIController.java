package com.ai.springAiDemo;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.ai.image.ImageResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class GenAIController {

    private final ChatService chatService;
    private final ImageService imageService;

    public GenAIController(ChatService chatService, ImageService imageService) {
        this.chatService = chatService;
        this.imageService = imageService;
    }

    @GetMapping("/ask-ai")
    public String getResponse(@RequestParam String prompt){
        return chatService.getResponse(prompt);
    }

    @GetMapping("/ask-ai-options")
    public String getResponseOptions(@RequestParam String prompt){
        return chatService.getResponseOptions(prompt);
    }

    @GetMapping("/ask-ai-hf")
    public String askAIHuggingFace(@RequestParam String prompt){
        return chatService.askAI(prompt);
    }

    @GetMapping("/generate-image")
    public ResponseEntity<byte[]> generateImages(@RequestParam String prompt) {
        byte[] image = imageService.generateImage(prompt);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
    }
}