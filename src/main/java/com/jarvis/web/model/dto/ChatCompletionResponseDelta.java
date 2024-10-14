package com.jarvis.web.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatCompletionResponseDelta {
    @JsonProperty("content")
    private String content;
    @JsonProperty("role")
    private String role;
}
