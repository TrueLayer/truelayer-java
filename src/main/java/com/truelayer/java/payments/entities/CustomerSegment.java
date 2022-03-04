package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CustomerSegment {
    RETAIL("retail"),
    BUSINESS("business"),
    CORPORATE("corporate");

    @JsonValue
    private final String customerSegment;
}
