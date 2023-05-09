package com.truelayer.java.payouts.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class PendingPayout extends Payout {
    Status status = Status.PENDING;
}
