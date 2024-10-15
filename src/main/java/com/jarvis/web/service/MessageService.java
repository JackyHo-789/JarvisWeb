package com.jarvis.web.service;

import com.jarvis.web.model.dto.ChatCompletionRequest;
import com.jarvis.web.model.dto.ChatMessage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MessageService {
    @Value("${system.prompt}")
    private String systemPrompt;

    public void process(ChatCompletionRequest request) {
        if (request.getMessages().size() == 1) {
            initialize(request);
        }
    }

    public void initialize(ChatCompletionRequest request) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("system");
        chatMessage.setContent(systemPrompt);
        request.getMessages().addFirst(chatMessage);
    }
}
