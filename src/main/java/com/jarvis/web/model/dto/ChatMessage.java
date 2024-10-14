package com.jarvis.web.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatMessage {
    @JsonProperty("role")
    private String role;

    @JsonProperty("content")
    private String content;
}
