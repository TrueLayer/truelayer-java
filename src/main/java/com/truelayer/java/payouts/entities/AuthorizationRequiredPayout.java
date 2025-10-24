package com.truelayer.java.payouts.entities;

import com.truelayer.java.payouts.entities.beneficiary.Beneficiary;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class AuthorizationRequiredPayout extends CreatePayoutResponse {
    private final Status status = Status.AUTHORIZATION_REQUIRED;
    private String resourceToken;
    private PayoutUser user;
    private Beneficiary beneficiary;
}
