package com.truelayer.java.signupplus.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Sex {
    Male("M"),
    Female("F");

    @JsonValue
    private final String value;
}
