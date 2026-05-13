package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UseCase {
    CHARITY("charity"),
    FINANCIAL_SERVICES("financial_services"),
    GOVERNMENT("government"),
    UTILITIES("utilities");

    @JsonValue
    private final String useCase;
}
