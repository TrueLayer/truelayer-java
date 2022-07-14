package com.truelayer.java.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ConfigurationStatus {
    SUPPORTED("supported"),
    NOT_SUPPORTED("not_supported");

    @JsonValue
    private final String status;
}
