package com.truelayer.java.payments.entities.retry;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", defaultImpl = StandardRetry.class)
@JsonSubTypes({
    @JsonSubTypes.Type(value = StandardRetry.class, name = "standard"),
    @JsonSubTypes.Type(value = SmartRetry.class, name = "smart")
})
@Getter
@ToString
@EqualsAndHashCode
public abstract class Retry {
    @JsonIgnore
    public abstract Type getType();

    public static StandardRetry.StandardRetryBuilder standard() {
        return StandardRetry.builder();
    }

    public static SmartRetry.SmartRetryBuilder smart() {
        return SmartRetry.builder();
    }

    @RequiredArgsConstructor
    @Getter
    public enum Type {
        STANDARD("standard"),
        SMART("smart");

        @JsonValue
        private final String type;
    }
}
