package truelayer.java.merchantaccounts.entities.sweeping;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Frequency {
    DAILY("daily"),
    WEEKLY("weekly"),
    FORTNIGHTLY("fortnightly");

    @JsonValue
    private final String frequency;
}
