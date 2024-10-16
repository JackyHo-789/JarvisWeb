package com.jarvis.web.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarvis.web.model.dto.*;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@Component
public class VllmClient {

    public void handleRequest(ChatCompletionRequest request, SseEmitter emitter) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost("http://192.168.8.88:8000/v1/chat/completions");
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(request);
//        System.out.println(jsonRequest);
        StringEntity stringEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);

        httpPost.setEntity(stringEntity);
        httpPost.setHeader("Accept", "text/event-stream");

        Executors.newSingleThreadExecutor().execute(() -> {
            try (CloseableHttpResponse response = client.execute(httpPost)) {
                final HttpEntity entity = response.getEntity();
                if (entity != null) {
                    try (InputStream inputStream = entity.getContent()) {
                        new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8)).lines().forEach(line -> {
                            try {
                                if (!line.isEmpty() && !line.equals("data: [DONE]")) {
                                    ChatCompletionResponse dto = mapLineToDTO(line);
                                    emitter.send(dto);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        emitter.complete();
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        // 返回一個空的列表
    }

    private static ChatCompletionResponse mapLineToDTO(String line) {
        try {
            // 将字符串转换为JSON对象
            ChatCompletionResponse dto = new ObjectMapper().readValue(line.replace("data: ", ""), ChatCompletionResponse.class);
            return dto;
        } catch (IOException e) {
            e.printStackTrace();
            return null; // 如果解析失败，返回null
        }
    }

    public static void main(String[] args) throws IOException {
        ChatCompletionRequest request = new ChatCompletionRequest();
        request.setMaxTokens(100);
        ChatMessage message = new ChatMessage();
        message.setRole("user");
        message.setContent("Write me 1000 words about elon musk");
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(message);
        request.setMessages(messages);
        request.setN(1);
        request.setTemperature(1);
        request.setTop_p(1);
        request.setRepetition_penalty(1);
        request.setModel("Qwen/Qwen2.5-14B-Instruct-GPTQ-Int4");
        request.setStream(true);
//        System.out.println(handleRequest(request));
    }
}
