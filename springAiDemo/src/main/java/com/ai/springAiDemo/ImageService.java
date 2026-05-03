package com.ai.springAiDemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class ImageService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    private final String URL = "https://router.huggingface.co/hf-inference/models/stabilityai/stable-diffusion-xl-base-1.0";

//    public byte[] generateImage(String prompt) {
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(apiKey);
//
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        headers.setAccept(List.of(MediaType.IMAGE_PNG));
//
//        Map<String, String> body = new HashMap<>();
//        body.put("inputs", prompt);
//
//        HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);
//
//        ResponseEntity<byte[]> response = restTemplate.exchange(
//                URL,
//                HttpMethod.POST,
//                request,
//                byte[].class
//        );
//
//        return response.getBody();
//    }

    private final List<byte[]> imageStore = new ArrayList<>();

    public List<String> generateImages(String prompt, int width, int height, int n, String quality) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.IMAGE_PNG));

        imageStore.clear();

        List<String> urls = new ArrayList<>();

        if (n > 5) n = 5;

        for (int i = 0; i < n; i++) {

            Map<String, Object> body = new HashMap<>();
            body.put("inputs", prompt);

            Map<String, Object> params = new HashMap<>();

            if ("low".equalsIgnoreCase(quality)) {
                params.put("width", 256);
                params.put("height", 256);
            } else if ("medium".equalsIgnoreCase(quality)) {
                params.put("width", 512);
                params.put("height", 512);
            } else {
                params.put("width", width);
                params.put("height", height);
            }

            body.put("parameters", params);

            HttpEntity<Map<String, Object>> request =
                    new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> response =
                    restTemplate.exchange(
                            URL,
                            HttpMethod.POST,
                            request,
                            byte[].class
                    );

            imageStore.add(response.getBody());
            urls.add("http://localhost:8080/image/" + i);
        }

        return urls;
    }

    public byte[] getImage(int index) {
        return imageStore.get(index);
    }
}