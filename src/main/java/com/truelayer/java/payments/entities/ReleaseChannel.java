package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ReleaseChannel {
    GENERAL_AVAILABILITY("general_availability"),
    PUBLIC_BETA("public_beta"),
    PRIVATE_BETA("private_beta"),
    ALPHA("alpha");

    @JsonValue
    private final String releaseChannel;
}
