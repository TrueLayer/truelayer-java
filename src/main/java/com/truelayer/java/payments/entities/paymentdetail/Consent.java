package com.truelayer.java.payments.entities.paymentdetail;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.net.URI;

@Value
@EqualsAndHashCode(callSuper = false)
public class Consent extends AuthorizationFlowAction {
    Type type = Type.CONSENT;

    SubsequentActionHint subsequentActionHint;

    @RequiredArgsConstructor
    @Getter
    public enum SubsequentActionHint {
        REDIRECT("redirect"),
        FORM("form");

        @JsonValue
        private final String subsequentActionHint;
    }
}
