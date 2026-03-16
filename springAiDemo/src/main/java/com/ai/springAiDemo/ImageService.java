package com.ai.springAiDemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<byte[]> generateImages(String prompt, int width, int height) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.IMAGE_PNG));

        List<byte[]> images = new ArrayList<>();

        for (int i = 0; i < 4; i++) {

            Map<String, Object> body = new HashMap<>();
            body.put("inputs", prompt);

            Map<String, Object> params = new HashMap<>();
            params.put("width", width);
            params.put("height", height);

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

            images.add(response.getBody());
        }

        return images;
    }
}