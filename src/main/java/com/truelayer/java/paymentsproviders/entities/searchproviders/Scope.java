package com.truelayer.java.paymentsproviders.entities.searchproviders;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Scope {
    ACCOUNTS("accounts"),
    BALANCE("balance");

    @JsonValue
    private final String scope;
}
