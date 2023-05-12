package com.truelayer.java.payouts.entities.beneficiary;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BusinessAccount extends Beneficiary {
    private final Type type = Type.BUSINESS_ACCOUNT;
    private String reference;
}
