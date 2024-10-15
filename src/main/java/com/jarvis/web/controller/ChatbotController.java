package com.jarvis.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarvis.web.client.VllmClient;
import com.jarvis.web.model.dto.ChatCompletionRequest;
import com.jarvis.web.model.dto.ChatCompletionResponse;
import com.jarvis.web.model.dto.ChatMessage;
import com.jarvis.web.service.MessageService;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@RestController
@RequestMapping("/v1/chat/completions")
public class ChatbotController {
    @Autowired
    private VllmClient client;
    @Autowired
    private MessageService service;

    @PostMapping
    public ResponseBodyEmitter handleRequest(@RequestBody ChatCompletionRequest request) throws JsonProcessingException {
        try {
            service.process(request);
            return client.handleRequest(request);
        } catch (Exception e) {
            // 如果没有响应，返回500错误
            return null;
        }
    }

    @GetMapping
    public ResponseBodyEmitter handleGRequest() throws JsonProcessingException {
        try {
            ChatCompletionRequest request = new ChatCompletionRequest();
            request.setMaxTokens(1000);
            ChatMessage message = new ChatMessage();
            message.setRole("user");
            message.setContent("Write me 200 words about yourself");
            List<ChatMessage> messages = new ArrayList<>();
            messages.add(message);
            request.setMessages(messages);
            request.setN(1);
            request.setTemperature(1);
            request.setTop_p(1);
            request.setRepetition_penalty(1);
            request.setModel("Qwen/Qwen2.5-14B-Instruct-GPTQ-Int4");
            request.setStream(true);
            service.process(request);
            System.out.println("ss:" + request.getMessages().toString());
            return client.handleRequest(request);
        } catch (Exception e) {
            // 如果没有响应，返回500错误
            return null;
        }
    }
}
