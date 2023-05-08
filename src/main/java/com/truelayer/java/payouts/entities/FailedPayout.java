package com.truelayer.java.payouts.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@EqualsAndHashCode(callSuper = false)
public class FailedPayout extends Payout{
    Status status = Status.FAILED;
    ZonedDateTime failedAt;
    String failureReason;
}
