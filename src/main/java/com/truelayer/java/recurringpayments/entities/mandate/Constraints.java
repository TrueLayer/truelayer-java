package com.truelayer.java.recurringpayments.entities.mandate;

import com.fasterxml.jackson.annotation.JsonValue;
import java.util.List;
import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Constraints {

    private String validFrom;

    private String validTo;

    private Integer maximumIndividualAmount;

    private List<PeriodicLimit> periodicLimits;

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class PeriodicLimit {
        private int maximumAmount;

        private PeriodicAlignment periodicAlignment;

        private PeriodicType periodicType;

        @RequiredArgsConstructor
        @Getter
        public enum PeriodicAlignment {
            CONSENT("consent"),
            CALENDAR("calendar");

            @JsonValue
            private final String value;
        }

        @RequiredArgsConstructor
        @Getter
        public enum PeriodicType {
            DAY("day"),
            WEEK("week"),
            FORTNIGHT("fortnight"),
            MONTH("month"),
            HALF_YEAR("half_year"),
            YEAR("year");

            @JsonValue
            private final String value;
        }
    }
}
