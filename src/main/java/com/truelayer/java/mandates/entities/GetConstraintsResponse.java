package com.truelayer.java.mandates.entities;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class GetConstraintsResponse {
    String validFrom;

    String validTo;

    Integer maximumIndividualAmount;

    PeriodicLimit periodicLimits;
}
