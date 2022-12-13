package com.truelayer.java.mandates.entities;

import lombok.Value;

@Value
public class GetConstraintsResponse {
    String validFrom;

    String validTo;

    Integer maximumIndividualAmount;

    PeriodicLimit periodicLimits;
}
