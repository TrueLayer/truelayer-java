package com.truelayer.java.mandates.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class PeriodicLimit {
    PeriodicLimitDetail day;

    PeriodicLimitDetail week;

    PeriodicLimitDetail fortnight;

    PeriodicLimitDetail month;

    PeriodicLimitDetail halfYear;

    PeriodicLimitDetail year;
}
