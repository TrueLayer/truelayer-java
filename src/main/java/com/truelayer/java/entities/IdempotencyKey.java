package com.truelayer.java.entities;

import lombok.Getter;

@Getter
public class IdempotencyKey {
    private final String value;

    public IdempotencyKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
