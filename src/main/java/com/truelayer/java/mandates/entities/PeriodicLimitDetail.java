package com.truelayer.java.mandates.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class PeriodicLimitDetail {
    String startDate;

    String endDate;

    Integer currentAmount;

    Integer maximumIndividualAmount;

    String periodAlignment;
}
