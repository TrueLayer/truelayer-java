package com.truelayer.java.payouts.entities;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class AuthorizationRequiredPayoutDetail extends Payout {
    Status status = Status.AUTHORIZATION_REQUIRED;
}
