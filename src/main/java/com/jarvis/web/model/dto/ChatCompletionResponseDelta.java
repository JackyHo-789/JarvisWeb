package com.jarvis.web.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatCompletionResponseDelta {
    @JsonProperty("content")
    private String content;
    @JsonProperty("role")
    private String role;
}
