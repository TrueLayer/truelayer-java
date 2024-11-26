package com.truelayer.java.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResourceType {
    PAYMENT("payments", "payment_id"),
    MANDATE("mandates", "mandate_id"),
    ;

    private final String hppLinkPath;
    private final String hppLinkQueryParameter;
}
