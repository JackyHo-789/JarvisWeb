package com.jarvis.web.controller;

import com.jarvis.web.model.dto.ChatCompletionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/v1/chat/completions")
public class ChatbotController {
    @PostMapping
    public ResponseEntity<StreamingResponseBody> handleRequest(@RequestBody ChatCompletionRequest request) {
        // 创建HttpClient实例
        CloseableHttpClient httpClient = HttpClients.createDefault();

        // 创建POST请求
        HttpPost requestToTarget = new HttpPost("http://192.168.8.88:8000/v1/chat/completions");
        StringEntity stringEntity = new StringEntity(request.toJson(), ContentType.APPLICATION_JSON);
        requestToTarget.setEntity(stringEntity);
        requestToTarget.setHeader("Content-Type", "application/json");

        try (CloseableHttpResponse responseToTarget = httpClient.execute(requestToTarget)) {
            // 等待一段时间，以便响应能够到达
            responseToTarget.waitForResponse(10, TimeUnit.SECONDS);

            // 获取响应体
            HttpEntity entity = responseToTarget.getEntity();
            if (entity != null) {
                // 创建响应体流
                StreamingResponseBody streamingResponseBody = outputStream -> {
                    try (InputStream inputStream = entity.getContent()) {
                        // 读取输入流，并逐行写入输出流
                        Stream<String> lines = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))
                                .lines();
                        lines.forEach(outputStream::write);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                };

                // 设置响应头，以支持流式传输
                outputStream.getHeaders().set("Content-Type", "application/json");
                outputStream.getHeaders().set("Content-Disposition", "attachment; filename=\"response.json\"");

                // 返回响应
                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(streamingResponseBody);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 如果没有响应，返回500错误
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}
