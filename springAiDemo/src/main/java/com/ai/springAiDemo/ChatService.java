package com.ai.springAiDemo;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.api.OllamaChatOptions;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.model.ChatModel;

@Service
public class ChatService {

    private final ChatModel chatModel;

    public ChatService(@Qualifier("ollamaChatModel") ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    public String getResponse(String prompt) {
        return chatModel.call(prompt);
    }

    public String getResponseOptions(String prompt){
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