package com.truelayer.java.payments.entities.retry;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SmartRetry extends Retry {
    private final Type type = Type.SMART;

    @JsonProperty("for")
    private final String forDuration;

    private int ensureMinimumBalanceInMinor;
}
