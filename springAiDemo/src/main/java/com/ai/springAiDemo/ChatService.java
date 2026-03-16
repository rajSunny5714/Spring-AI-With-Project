package com.ai.springAiDemo;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ChatModel;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Value("${huggingface.api.key}")
    private String apiKey;

    private final String URL = "https://router.huggingface.co/v1/chat/completions";

    public String askAI(String prompt) {

        RestTemplate restTemplate = new RestTemplate();

        String url = "https://router.huggingface.co/v1/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "meta-llama/Meta-Llama-3-8B-Instruct");

        List<Map<String, String>> messages = new ArrayList<>();

        Map<String, String> msg = new HashMap<>();
        msg.put("role", "user");
        msg.put("content", prompt);

        messages.add(msg);

        body.put("messages", messages);

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.exchange(url, HttpMethod.POST, request, String.class);

        ObjectMapper mapper = new ObjectMapper();

        try {
            JsonNode root = mapper.readTree(response.getBody());

            return root.path("choices")
                    .get(0)
                    .path("message")
                    .path("content")
                    .asText();

        } catch (Exception e) {
            return "Error parsing AI response";
        }
    }

    private final ChatModel chatModel;

    public ChatService(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String getResponse(String prompt) {
        return chatModel.call(prompt);
    }

    public String getResponseOptions(String prompt) {
        ChatResponse response = chatModel.call(
                new Prompt(
                        prompt,
                        OllamaChatOptions.builder()
                                .model("phi3:mini")
                                .temperature(0.4)
                                .build()
                )
        );

        return response.getResult().getOutput().getText();
    }
}