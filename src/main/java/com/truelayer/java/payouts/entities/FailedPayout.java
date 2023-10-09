package com.truelayer.java.payouts.entities;

import java.time.ZonedDateTime;
import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class FailedPayout extends Payout {
    Status status = Status.FAILED;
    ZonedDateTime failedAt;
    String failureReason;
}
