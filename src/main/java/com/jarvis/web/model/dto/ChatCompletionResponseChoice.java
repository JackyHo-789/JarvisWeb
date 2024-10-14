package com.jarvis.web.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChatCompletionResponseChoice {
    @JsonProperty("index")
    private int index;

    @JsonProperty("delta")
    private ChatCompletionResponseDelta delta;

    @JsonProperty("logprobs")
    private Object logprobs;

    @JsonProperty("finish_reason")
    private String finishReason;

    @JsonProperty("stop_reason")
    private String stopReason;
}
