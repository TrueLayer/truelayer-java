package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    AUTHORIZATION_REQUIRED("authorization_required"),
    AUTHORIZING("authorizing"),
    AUTHORIZED("authorized"),
    EXECUTED("executed"),
    FAILED("failed"),
    SETTLED("settled"),
    ATTEMPT_FAILED("attempt_failed");

    @JsonValue
    private final String status;
}
