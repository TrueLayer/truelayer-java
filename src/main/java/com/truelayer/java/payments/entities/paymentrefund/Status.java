package com.truelayer.java.payments.entities.paymentrefund;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    PENDING("pending"),
    AUTHORIZED("authorized"),
    EXECUTED("executed"),
    FAILED("failed");

    @JsonValue
    private final String status;
}
