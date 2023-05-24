package com.truelayer.java.http.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * Class representing custom TL headers that we allow our users to set.
 */
@Builder
@Getter
@ToString
@EqualsAndHashCode
public class Headers {
    private String idempotencyKey;

    private String signature;

    private String xForwardedFor;
}
