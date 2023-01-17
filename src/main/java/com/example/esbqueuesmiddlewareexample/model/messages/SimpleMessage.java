package com.example.esbqueuesmiddlewareexample.model.messages;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SimpleMessage(
    @JsonProperty("message") String message,
    @JsonProperty("identifier") String identifier
) {}
