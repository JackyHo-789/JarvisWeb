package com.jarvis.web.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jarvis.web.model.dto.ChatCompletionRequest;
import com.jarvis.web.model.dto.ChatCompletionResponse;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class StreamRequestUtil {
    public static <T, K> void handleRequest(String url, T request) throws IOException {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        ObjectMapper mapper = new ObjectMapper();
        String jsonRequest = mapper.writeValueAsString(request);
        System.out.println(jsonRequest);
        StringEntity stringEntity = new StringEntity(jsonRequest, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);

        try (CloseableHttpResponse response = client.execute(httpPost)) {
            System.out.println(response);
            final HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream inputStream = entity.getContent()) {
                    // do something useful with the stream
                    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                        String line;
//                        int count = 0;
                        while ((line = reader.readLine()) != null) {
//                            System.out.println("cc:" + count + " " + line + line.isEmpty() + line.equals(" "));
                            if (!line.isEmpty() && !line.equals("data: [DONE]")) {
                                ChatCompletionResponse dto = mapLineToDTO(line);
                                assert dto != null;
                                System.out.println(dto.getChoices().getFirst().getDelta().getContent()); // 打印每一行
//                                count++;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("end");
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
}
