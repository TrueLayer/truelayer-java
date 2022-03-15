package com.truelayer.java.recurringpayments.entities.mandatedetail;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Status {
    AUTHORIZATION_REQUIRED("authorization_required"),
    AUTHORIZING("authorizing"),
    AUTHORIZED("authorized"),
    FAILED("failed"),
    REVOKED("revoked");

    @JsonValue
    private final String status;
}
