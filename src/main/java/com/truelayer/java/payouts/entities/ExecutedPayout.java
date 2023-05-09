package com.truelayer.java.payouts.entities;

import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExecutedPayout extends Payout {
    Status status = Status.EXECUTED;
    ZonedDateTime executedAt;
}
