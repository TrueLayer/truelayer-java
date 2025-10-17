package com.truelayer.java.payouts.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class CreatedPayout extends CreatePayoutResponse {
    Status status = Status.CREATED;
}
