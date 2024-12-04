package com.truelayer.java.paymentsproviders.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum IconType {
    DEFAULT("default"),
    EXTENDED("extended"),
    EXTENDED_SMALL("extended_small"),
    EXTENDED_MEDIUM("extended_medium"),
    EXTENDED_LARGE("extended_large");

    @JsonValue
    private final String iconType;
}
