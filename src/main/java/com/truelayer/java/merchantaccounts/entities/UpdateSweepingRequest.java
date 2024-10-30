package com.truelayer.java.merchantaccounts.entities;

import com.truelayer.java.merchantaccounts.entities.sweeping.Frequency;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class UpdateSweepingRequest {
    private int maxAmountInMinor;

    private Frequency frequency;
}
