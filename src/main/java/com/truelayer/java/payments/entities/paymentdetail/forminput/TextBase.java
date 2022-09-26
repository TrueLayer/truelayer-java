package com.truelayer.java.payments.entities.paymentdetail.forminput;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
public abstract class TextBase extends Input {

    Format format;
    boolean sensitive;
    int min_length;
    int max_length;
    List<Regex> regexes;

    @RequiredArgsConstructor
    @Getter
    public enum Format {
        ANY("any"),
        NUMERICAL("numerical"),
        ALPHABETICAL("alphabetical"),
        ALPHANUMERICAL("alphanumerical"),
        EMAIL("email"),
        SORT_CODE("sort_code"),
        ACCOUNT_NUMBER("account_number"),
        IBAN("iban");

        @JsonValue
        private final String format;
    }

    @Value
    @EqualsAndHashCode
    public static class Regex {
        String regex;
        DisplayText message;
    }
}
