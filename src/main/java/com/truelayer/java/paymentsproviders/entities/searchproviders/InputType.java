package com.truelayer.java.paymentsproviders.entities.searchproviders;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum InputType {
    TEXT("text"),
    TEXT_WITH_IMAGE("text_with_image"),
    SELECT("select");

    @JsonValue
    private final String inputType;
}
