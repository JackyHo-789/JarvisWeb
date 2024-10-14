package com.jarvis.web.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChatCompletionRequest {
    @JsonProperty("model")
    private String model;

    @JsonProperty("messages")
    private List<ChatMessage> messages;

    @JsonProperty("n")
    private int n;

    @JsonProperty("temperature")
    private double temperature;

    @JsonProperty("max_tokens")
    private int maxTokens;

    @JsonProperty("stream")
    private boolean stream;

    @JsonProperty("top_p")
    private boolean top_p;

    @JsonProperty("repetition_penalty")
    private double repetition_penalty;
}
