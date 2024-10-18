package com.jarvis.web.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionRequest {
    @JsonProperty("model")
    private String model = "Qwen/Qwen2.5-14B-Instruct-GPTQ-Int4";

    @JsonProperty("messages")
    private List<ChatMessage> messages;

    @JsonProperty("n")
    private String n = null;

    @JsonProperty("temperature")
    private String temperature = "0.7";

    @JsonProperty("max_tokens")
    private String maxTokens = "2048";

    @JsonProperty("stream")
    private boolean stream = true;

    @JsonProperty("top_p")
    private String top_p = "0.8";

    @JsonProperty("repetition_penalty")
    private String repetition_penalty = "1.05";
}
