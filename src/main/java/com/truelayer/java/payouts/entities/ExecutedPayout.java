package com.truelayer.java.payouts.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@EqualsAndHashCode(callSuper = false)
public class ExecutedPayout extends Payout{
    Status status = Status.EXECUTED;
    ZonedDateTime executedAt;
}
