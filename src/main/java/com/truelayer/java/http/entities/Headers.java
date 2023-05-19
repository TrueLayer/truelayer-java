package com.truelayer.java.http.entities;

import lombok.Builder;
import lombok.Getter;

/**
 * Class representing custom TL headers that we allow our users to set.
 */
@Builder
@Getter
public class Headers {
    private String idempotencyKey;

    private String signature;

    private String xForwardedFor;
}
