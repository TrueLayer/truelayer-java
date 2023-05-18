package com.truelayer.java.http.entities;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Headers {
    private String idempotencyKey;

    private String signature;

    private String xForwardedFor;
}
