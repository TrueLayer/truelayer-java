package com.truelayer.java.payments.entities.paymentdetail.forminput;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Getter
public abstract class TextBase extends Input {

    Format format;
    boolean sensitive;
    int minLength;
    int maxLength;
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
