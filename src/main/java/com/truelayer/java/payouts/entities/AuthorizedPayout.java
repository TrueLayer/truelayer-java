package com.truelayer.java.payouts.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizedPayout extends Payout {
    Status status = Status.AUTHORIZED;
}
