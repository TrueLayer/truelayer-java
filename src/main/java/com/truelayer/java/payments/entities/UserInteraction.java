package com.truelayer.java.payments.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserInteraction {
    PRESENT("present"),
    NOT_PRESENT("not_present");

    @JsonValue
    private final String userInteraction;
}
