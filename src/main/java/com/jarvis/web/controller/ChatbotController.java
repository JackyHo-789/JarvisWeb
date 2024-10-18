package com.jarvis.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jarvis.web.client.VllmClient;
import com.jarvis.web.model.dto.ChatCompletionRequest;
import com.jarvis.web.model.dto.ChatMessage;
import com.jarvis.web.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/v1/chat/completions")
@CrossOrigin(value = "*")
public class ChatbotController {
    @Autowired
    private VllmClient client;
    @Autowired
    private MessageService service;

    @PostMapping(produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public SseEmitter handleRequest(@RequestBody ChatCompletionRequest request) throws JsonProcessingException {
        try {
            service.process(request);
            System.out.println("sa:" + request.toString());
            SseEmitter emitter = new SseEmitter();
            client.handleRequest(request, emitter);
            return emitter;
        } catch (Exception e) {
            // 如果没有响应，返回500错误
            return null;
        }
    }

    @GetMapping(produces = {MediaType.TEXT_EVENT_STREAM_VALUE})
    public ResponseBodyEmitter handleGRequest() throws JsonProcessingException {
        try {
            ChatCompletionRequest request = new ChatCompletionRequest();
            request.setMaxTokens("1000");
            ChatMessage message = new ChatMessage();
            message.setRole("user");
            message.setContent("Write me 500 words about yourself");
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(message);
            request.setMessages(messages);
            request.setN("1");
            request.setTemperature("1");
            request.setTop_p("1");
            request.setRepetition_penalty("1");
            request.setModel("Qwen/Qwen2.5-14B-Instruct-GPTQ-Int4");
            request.setStream(true);
            service.process(request);
            System.out.println("ss:" + request.getMessages().toString());
            SseEmitter emitter = new SseEmitter();
            client.handleRequest(request, emitter);
            return emitter;
        } catch (Exception e) {
            // 如果没有响应，返回500错误
            return null;
        }
    }
}
