package com.truelayer.java.mandates.entities;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.*;

@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Constraints {

    private String validFrom;

    private String validTo;

    private Integer maximumIndividualAmount;

    private PeriodicLimits periodicLimits;

    @Builder
    @Getter
    @EqualsAndHashCode
    @ToString
    public static class PeriodicLimits {

        private Limit day;

        private Limit week;

        private Limit fortnight;

        private Limit month;

        private Limit halfYear;

        private Limit year;

        @Builder
        @Getter
        @EqualsAndHashCode
        @ToString
        public static class Limit {
            private int maximumAmount;

            private PeriodAlignment periodAlignment;

            @RequiredArgsConstructor
            @Getter
            public enum PeriodAlignment {
                CONSENT("consent"),
                CALENDAR("calendar");

                @JsonValue
                private final String value;
            }
        }
    }
}
