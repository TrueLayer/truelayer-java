package com.truelayer.java.mandates.entities;

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

        private PeriodAlignment periodAlignment;

        private PeriodType periodType;

        @RequiredArgsConstructor
        @Getter
        public enum PeriodAlignment {
            CONSENT("consent"),
            CALENDAR("calendar");

            @JsonValue
            private final String value;
        }

        @RequiredArgsConstructor
        @Getter
        public enum PeriodType {
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
