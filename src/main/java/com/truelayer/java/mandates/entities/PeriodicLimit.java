package com.truelayer.java.mandates.entities;

import lombok.Value;

@Value
public class PeriodicLimit {
    PeriodicLimitDetail day;

    PeriodicLimitDetail week;

    PeriodicLimitDetail fortnight;

    PeriodicLimitDetail month;

    PeriodicLimitDetail halfYear;

    PeriodicLimitDetail year;
}
