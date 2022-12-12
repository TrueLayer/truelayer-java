package com.truelayer.java.mandates.entities;

import lombok.Value;

@Value
public class PeriodicLimitDetail {
    String startDate;

    String endDate;

    Integer currentAmount;

    Integer maximumAvailableAmount;

    String periodAlignment;
}
