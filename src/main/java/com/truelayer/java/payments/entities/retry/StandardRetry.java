package com.truelayer.java.payments.entities.retry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class StandardRetry extends Retry {
    private final Type type = Type.STANDARD;

    @JsonProperty("for")
    private final String forDuration;
}
